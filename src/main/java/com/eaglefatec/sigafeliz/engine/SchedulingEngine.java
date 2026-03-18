package com.eaglefatec.sigafeliz.engine;

import com.eaglefatec.sigafeliz.model.*;
import com.eaglefatec.sigafeliz.model.BlockedDay.DayType;
import com.eaglefatec.sigafeliz.model.Tema.Priority;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Core scheduling engine that distributes aulas across working days
 * according to the business rules defined in projeto.md and backlog.md.
 */
public class SchedulingEngine {

    private static final Map<DayOfWeek, String> DAY_NAMES = Map.of(
            DayOfWeek.MONDAY, "Segunda",
            DayOfWeek.TUESDAY, "Terça",
            DayOfWeek.WEDNESDAY, "Quarta",
            DayOfWeek.THURSDAY, "Quinta",
            DayOfWeek.FRIDAY, "Sexta",
            DayOfWeek.SATURDAY, "Sábado",
            DayOfWeek.SUNDAY, "Domingo");

    /**
     * Generates the full schedule.
     *
     * @param semester       the active semester (includes kickoffDate)
     * @param blockedDays    list of blocked days (holidays, events, sabados letivos)
     * @param weeklySchedule map from DayOfWeek to number of aulas on that day
     * @param temas          list of temas with min/max/priority/evaluation
     * @param workload       target number of aulas (40 or 80)
     * @return ScheduleResult with the full list of ScheduledAula or errors
     */
    public ScheduleResult generate(Semester semester,
            List<BlockedDay> blockedDays,
            Map<DayOfWeek, Integer> weeklySchedule,
            List<Tema> temas,
            int workload) {

        ScheduleResult result = new ScheduleResult();

        // Step 0: Validate inputs
        if (temas.isEmpty()) {
            result.getErrors().add("Nenhum tema definido. Adicione pelo menos um tema.");
            return result;
        }

        int sumMin = temas.stream().mapToInt(Tema::getMinAulas).sum();
        int sumMax = temas.stream().mapToInt(Tema::getMaxAulas).sum();

        if (sumMin > workload) {
            result.getErrors().add(String.format(
                    "A soma mínima de aulas dos temas (%d) excede a carga horária (%d). Reduza os mínimos.", sumMin,
                    workload));
            return result;
        }

        // Step 1: Generate regular working days (excluding blocked holidays/events)
        Set<LocalDate> blockedSet = blockedDays.stream()
                .filter(bd -> bd.getDayType() == DayType.HOLIDAY || bd.getDayType() == DayType.EVENT
                        || bd.getDayType() == DayType.SPRINT_REVIEW)
                .map(bd -> LocalDate.parse(bd.getBlockedDate()))
                .collect(Collectors.toSet());

        // Get available sabados letivos
        List<LocalDate> sabadosLetivos = blockedDays.stream()
                .filter(bd -> bd.getDayType() == DayType.SABADO_LETIVO)
                .map(bd -> LocalDate.parse(bd.getBlockedDate()))
                .sorted()
                .collect(Collectors.toList());

        LocalDate start = LocalDate.parse(semester.getStartDate());
        LocalDate end = LocalDate.parse(semester.getEndDate());
        Set<DayOfWeek> activeDays = weeklySchedule.keySet();

        // Build initial working days (regular weekdays only, not Saturdays from sabados letivos)
        List<LocalDate> workingDays = new ArrayList<>();
        LocalDate current = start;
        while (!current.isAfter(end)) {
            if (activeDays.contains(current.getDayOfWeek()) && !blockedSet.contains(current)) {
                workingDays.add(current);
            }
            current = current.plusDays(1);
        }

        // Calculate regular capacity
        int regularCapacity = 0;
        for (LocalDate day : workingDays) {
            regularCapacity += weeklySchedule.getOrDefault(day.getDayOfWeek(), 0);
        }

        // Step 2: Saturday Compensation (US05)
        // If regular capacity < workload, activate sabados letivos in reverse chronological order
        List<LocalDate> activatedSaturdays = new ArrayList<>();
        if (regularCapacity < workload && !sabadosLetivos.isEmpty()) {
            int deficit = workload - regularCapacity;
            // Assume each Saturday gives the same number of aulas as the Saturday setting,
            // or default to 2 aulas per Saturday
            int aulasPerSaturday = weeklySchedule.getOrDefault(DayOfWeek.SATURDAY, 2);

            // Process sabados in reverse chronological order (end of semester first)
            List<LocalDate> reverseSabados = new ArrayList<>(sabadosLetivos);
            Collections.reverse(reverseSabados);

            for (LocalDate sabado : reverseSabados) {
                if (deficit <= 0) break;
                if (!sabado.isBefore(start) && !sabado.isAfter(end) && !blockedSet.contains(sabado)) {
                    activatedSaturdays.add(sabado);
                    deficit -= aulasPerSaturday;
                }
            }

            if (!activatedSaturdays.isEmpty()) {
                // Add activated Saturdays to working days and sort chronologically
                workingDays.addAll(activatedSaturdays);
                workingDays.sort(Comparator.naturalOrder());

                // Ensure Saturday is in the weekly schedule for capacity calculation
                if (!weeklySchedule.containsKey(DayOfWeek.SATURDAY)) {
                    weeklySchedule.put(DayOfWeek.SATURDAY, 2);
                }

                result.getWarnings().add(String.format(
                        "⚠ Dias regulares insuficientes. %d sábado(s) letivo(s) ativado(s) para compensação de carga horária.",
                        activatedSaturdays.size()));
            }
        }

        // Recalculate total capacity with Saturdays
        int totalCapacity = 0;
        for (LocalDate day : workingDays) {
            totalCapacity += weeklySchedule.getOrDefault(day.getDayOfWeek(), 0);
        }

        if (totalCapacity < workload) {
            result.getErrors().add(String.format(
                    "O calendário possui apenas %d vagas de aulas (incluindo %d sábado(s) letivo(s)). Não é possível alocar %d aulas.",
                    totalCapacity, activatedSaturdays.size(), workload));
            return result;
        }

        // Step 3: Allocate minimums
        Map<Tema, Integer> allocation = new LinkedHashMap<>();
        for (Tema t : temas) {
            allocation.put(t, t.getMinAulas());
        }

        int allocated = sumMin;

        // Step 4: Distribute surplus by priority (ALTO > MEDIO > BAIXO)
        int remaining = workload - allocated;
        if (remaining > 0) {
            remaining = distributeSurplus(temas, allocation, remaining);
        }

        // Step 5: Fill with "Fechamento" if still remaining
        if (remaining > 0) {
            Tema fechamento = new Tema("Fechamento", remaining, remaining, Priority.BAIXO, false);
            allocation.put(fechamento, remaining);
            result.getWarnings().add(String.format(
                    "%d aula(s) extra(s) foram preenchidas com 'Fechamento' por falta de margem nos temas.",
                    remaining));
        }

        // Step 6: Build the aula sequence
        List<String> aulaSequence = buildAulaSequence(temas, allocation, workload);

        // Build a set of evaluation tema titles for marking
        Set<String> evalTitles = temas.stream()
                .filter(Tema::isEvaluation)
                .map(Tema::getTitle)
                .collect(Collectors.toSet());

        // Build holiday/event descriptions map for observations
        Map<LocalDate, String> blockedDescriptions = new HashMap<>();
        for (BlockedDay bd : blockedDays) {
            LocalDate d = LocalDate.parse(bd.getBlockedDate());
            String desc = bd.getDescription() != null && !bd.getDescription().isBlank()
                    ? bd.getDescription()
                    : bd.getDayType().name();
            blockedDescriptions.put(d, desc);
        }

        // Step 7: Assign dates with enriched data
        List<ScheduledAula> scheduledAulas = assignDates(aulaSequence, workingDays, weeklySchedule,
                evalTitles, activatedSaturdays, semester);
        result.getAulas().addAll(scheduledAulas);

        // Step 8: Sprint validation — BLOCKING errors for evaluations in sprint weeks 3-4 (US06)
        if (semester.getKickoffDate() != null && !semester.getKickoffDate().isBlank()) {
            validateSprintConstraints(result, semester, evalTitles);
        }

        return result;
    }

    /**
     * Distributes surplus aulas to temas by priority: ALTO first, then MEDIO, then
     * BAIXO.
     * Each tema receives up to (maxAulas - minAulas) additional aulas.
     * 
     * @return remaining aulas that couldn't be allocated
     */
    private int distributeSurplus(List<Tema> temas, Map<Tema, Integer> allocation, int remaining) {
        List<Tema> sorted = temas.stream()
                .sorted(Comparator.comparingInt(t -> priorityOrder(t.getPriority())))
                .collect(Collectors.toList());

        boolean changed = true;
        while (remaining > 0 && changed) {
            changed = false;
            for (Tema t : sorted) {
                if (remaining <= 0)
                    break;
                int current = allocation.get(t);
                if (current < t.getMaxAulas()) {
                    allocation.put(t, current + 1);
                    remaining--;
                    changed = true;
                }
            }
        }
        return remaining;
    }

    private int priorityOrder(Priority p) {
        return switch (p) {
            case ALTO -> 0;
            case MEDIO -> 1;
            case BAIXO -> 2;
        };
    }

    /**
     * Builds an ordered sequence of tema titles based on the allocations.
     */
    private List<String> buildAulaSequence(List<Tema> temas, Map<Tema, Integer> allocation, int workload) {
        List<String> sequence = new ArrayList<>();
        for (Map.Entry<Tema, Integer> entry : allocation.entrySet()) {
            int count = entry.getValue();
            for (int i = 0; i < count; i++) {
                sequence.add(entry.getKey().getTitle());
            }
        }
        while (sequence.size() > workload) {
            sequence.remove(sequence.size() - 1);
        }
        return sequence;
    }

    /**
     * Assigns each aula in the sequence to a working day, respecting daily
     * capacity. Enriches each aula with day-of-week, evaluation marker, and
     * observations.
     */
    private List<ScheduledAula> assignDates(List<String> aulaSequence,
            List<LocalDate> workingDays,
            Map<DayOfWeek, Integer> weeklySchedule,
            Set<String> evalTitles,
            List<LocalDate> activatedSaturdays,
            Semester semester) {
        List<ScheduledAula> result = new ArrayList<>();
        int aulaIndex = 0;
        int aulaNumber = 1;

        Set<LocalDate> saturdaySet = new HashSet<>(activatedSaturdays);

        // Parse kickoff for sprint observation
        LocalDate kickoff = null;
        if (semester.getKickoffDate() != null && !semester.getKickoffDate().isBlank()) {
            kickoff = LocalDate.parse(semester.getKickoffDate());
        }

        for (LocalDate day : workingDays) {
            if (aulaIndex >= aulaSequence.size())
                break;

            int capacity = weeklySchedule.getOrDefault(day.getDayOfWeek(), 0);
            int aulasThisDay = Math.min(capacity, aulaSequence.size() - aulaIndex);

            // Build observation for this day
            String observation = buildObservation(day, saturdaySet, kickoff);

            for (int i = 0; i < aulasThisDay; i++) {
                String temaTitle = aulaSequence.get(aulaIndex++);
                ScheduledAula sa = new ScheduledAula();
                sa.setDate(day);
                sa.setAulaNumber(aulaNumber++);
                sa.setTemaTitle(temaTitle);
                sa.setAulasOnThisDay(aulasThisDay);
                sa.setHora(String.format("Aula %d/%d", (i + 1), aulasThisDay));
                sa.setDayOfWeek(DAY_NAMES.getOrDefault(day.getDayOfWeek(), day.getDayOfWeek().name()));
                sa.setEvaluation(evalTitles.contains(temaTitle));
                sa.setObservation(observation);
                result.add(sa);
            }
        }

        return result;
    }

    /**
     * Builds an observation string for a given day.
     */
    private String buildObservation(LocalDate day, Set<LocalDate> activatedSaturdays, LocalDate kickoff) {
        List<String> obs = new ArrayList<>();
        if (activatedSaturdays.contains(day)) {
            obs.add("Sábado Letivo");
        }
        if (kickoff != null) {
            long daysSinceKickoff = ChronoUnit.DAYS.between(kickoff, day);
            if (daysSinceKickoff >= 0) {
                int sprintNumber = (int) (daysSinceKickoff / 28) + 1;
                int dayInSprint = (int) (daysSinceKickoff % 28) + 1;
                int weekInSprint = (dayInSprint - 1) / 7 + 1;
                if (weekInSprint == 3 || weekInSprint == 4) {
                    obs.add(String.format("Sprint %d (Sem. %d - Restrição)", sprintNumber, weekInSprint));
                }
            }
        }
        return String.join("; ", obs);
    }

    /**
     * Checks if any evaluation aula falls in sprint weeks 3 or 4.
     * Per US06, this produces BLOCKING ERRORS (not just warnings).
     */
    private void validateSprintConstraints(ScheduleResult result, Semester semester, Set<String> evalTitles) {
        if (evalTitles.isEmpty()) return;

        LocalDate kickoff = LocalDate.parse(semester.getKickoffDate());
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (ScheduledAula aula : result.getAulas()) {
            if (!evalTitles.contains(aula.getTemaTitle()))
                continue;

            long daysSinceKickoff = ChronoUnit.DAYS.between(kickoff, aula.getDate());
            if (daysSinceKickoff >= 0) {
                int dayInSprint = (int) (daysSinceKickoff % 28) + 1;
                // Days 15-28 = weeks 3-4 of the sprint cycle
                if (dayInSprint >= 15 && dayInSprint <= 28) {
                    int sprintNumber = (int) (daysSinceKickoff / 28) + 1;
                    int weekInSprint = (dayInSprint - 1) / 7 + 1;
                    result.getErrors().add(String.format(
                            "Avaliação '%s' em %s cai na semana %d da Sprint %d (Kickoff: %s). Ajuste necessário.",
                            aula.getTemaTitle(),
                            aula.getDate().format(fmt),
                            weekInSprint,
                            sprintNumber,
                            kickoff.format(fmt)));
                }
            }
        }
    }

    /**
     * Parses a simple JSON string of the form {"MONDAY":2,"WEDNESDAY":3,...}
     * into a Map<DayOfWeek, Integer>.
     */
    public static Map<DayOfWeek, Integer> parseWeeklySchedule(String json) {
        Map<DayOfWeek, Integer> map = new LinkedHashMap<>();
        if (json == null || json.isBlank())
            return map;

        String clean = json.replace("{", "").replace("}", "").replace("\"", "").trim();
        if (clean.isEmpty())
            return map;

        for (String pair : clean.split(",")) {
            String[] kv = pair.trim().split(":");
            if (kv.length == 2) {
                DayOfWeek dow = DayOfWeek.valueOf(kv[0].trim());
                int count = Integer.parseInt(kv[1].trim());
                map.put(dow, count);
            }
        }
        return map;
    }

    /**
     * Serializes a Map<DayOfWeek, Integer> to JSON string.
     */
    public static String serializeWeeklySchedule(Map<DayOfWeek, Integer> schedule) {
        StringBuilder sb = new StringBuilder("{");
        int i = 0;
        for (Map.Entry<DayOfWeek, Integer> entry : schedule.entrySet()) {
            if (i > 0)
                sb.append(",");
            sb.append("\"").append(entry.getKey().name()).append("\":").append(entry.getValue());
            i++;
        }
        sb.append("}");
        return sb.toString();
    }
}
