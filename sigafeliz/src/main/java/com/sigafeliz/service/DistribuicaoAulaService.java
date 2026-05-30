package com.sigafeliz.service;

import com.sigafeliz.dao.DiaRestritoDAO;
import com.sigafeliz.dao.GradeDAO;
import com.sigafeliz.dao.SemestreDAO;
import com.sigafeliz.dao.TemaDAO;
import com.sigafeliz.model.DiaRestrito;
import com.sigafeliz.model.Prioridade;
import com.sigafeliz.model.Semestre;
import com.sigafeliz.model.Tema;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class DistribuicaoAulaService {

    private final TemaDAO temaDAO;
    private final GradeDAO gradeDAO;
    private final DiaRestritoDAO diaRestritoDAO;
    private final SemestreDAO semestreDAO;

    public DistribuicaoAulaService() {
        this.temaDAO = new TemaDAO();
        this.gradeDAO = new GradeDAO();
        this.diaRestritoDAO = new DiaRestritoDAO();
        this.semestreDAO = new SemestreDAO();
    }

    // ------------------------------------------------------------------
    // Ponto de entrada: busca do banco e retorna resultado (sem salvar)
    // ------------------------------------------------------------------
    public List<Tema> distribuir(String disciplinaNome, String semestreNome) throws SQLException {
        Semestre semestre = semestreDAO.buscarPorNome(semestreNome);
        if (semestre == null) throw new SQLException("Semestre não encontrado: " + semestreNome);

        List<Tema> temas = temaDAO.buscarPorDisciplina(disciplinaNome);
        Map<DayOfWeek, Integer> gradeAulas = gradeDAO.buscarGradePorDisciplina(disciplinaNome);
        List<DiaRestrito> diasRestritos = diaRestritoDAO.listarPorSemestre(semestre);

        int capacidadeTotal = calcularCapacidadeReal(semestre, gradeAulas, diasRestritos);

        return calcularDistribuicao(temas, capacidadeTotal);
    }

    // ------------------------------------------------------------------
    // Calcula o total real de aulas descontando feriados
    // ------------------------------------------------------------------
    public int calcularCapacidadeReal(Semestre semestre,
                                      Map<DayOfWeek, Integer> gradeAulas,
                                      List<DiaRestrito> diasRestritos) {
        // Coleta as datas restritas num Set para lookup O(1)
        Set<LocalDate> datasRestritas = new HashSet<>();
        for (DiaRestrito dr : diasRestritos) {
            datasRestritas.add(dr.getData());
        }

        int total = 0;
        LocalDate data = semestre.getDataInicio();
        LocalDate fim = semestre.getDataFim();

        while (!data.isAfter(fim)) {
            DayOfWeek dia = data.getDayOfWeek();
            // Se esse dia da semana tem aula e não é feriado/restrito
            if (gradeAulas.containsKey(dia) && !datasRestritas.contains(data)) {
                total += gradeAulas.get(dia);
            }
            data = data.plusDays(1);
        }

        return total;
    }

    // ------------------------------------------------------------------
    // Lógica pura — recebe capacidade já calculada
    // ------------------------------------------------------------------
    public List<Tema> calcularDistribuicao(List<Tema> temas, int capacidadeTotal) {
        if (temas == null || temas.isEmpty()) return new ArrayList<>();

        // PASSO 1: aloca mínimos
        int totalMinimos = temas.stream().mapToInt(Tema::getCargaMinima).sum();

        // PASSO 2: vagas restantes
        int vagasRestantes = capacidadeTotal - totalMinimos;

        // PASSO 3: preenche por prioridade
        if (vagasRestantes > 0) {
            List<Tema> porPrioridade = new ArrayList<>(temas);
            porPrioridade.sort(Comparator.comparingInt(t -> prioridadeParaInt(t.getPrioridade())));

            for (Tema t : porPrioridade) {
                if (vagasRestantes <= 0) break;
                if (t.getVagasRestantes() <= 0) continue;

                int adicionar = Math.min(t.getVagasRestantes(), vagasRestantes);
                t.setAulasAlocadas(t.getAulasAlocadas() + adicionar);
                vagasRestantes -= adicionar;
            }
        }

        return temas;
    }

    private int prioridadeParaInt(Prioridade prioridade) {
        if (prioridade == null) return 99;
        return switch (prioridade) {
            case ALTA  -> 1;
            case MEDIA -> 2;
            case BAIXA -> 3;
        };
    }
}