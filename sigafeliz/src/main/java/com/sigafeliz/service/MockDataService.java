package com.sigafeliz.service;

import com.sigafeliz.model.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MockDataService {

    // Listas internas
    private static final List<Professor> professoresMock = new ArrayList<>(List.of(
            new Professor("mineda@fatec.com", "Prof. Mineda"),
            new Professor("bertoti@fatec.com", "Prof. Bertoti"),
            new Professor("sabha@fatec.com", "Prof. Sabha")
    ));

    private static final List<Disciplina> disciplinasMock = new ArrayList<>();
    private static final List<Semestre> semestresMock = new ArrayList<>();

    private static Professor professorLogado;
    private static Disciplina disciplinaSelecionada;

    static {
        // 1. Mock do Semestre (Seria feito pelac oordenação)
        Semestre s1 = new Semestre("2025.1",
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 7, 31),
                LocalDate.of(2025, 2, 1));

        Stream.of(LocalDate.of(2025, 4, 18), LocalDate.of(2025, 5, 1))
                .forEach(date -> s1.addDiaRestrito(new DiaRestrito(null, date, "Feriado")));

        semestresMock.add(s1);
        // 2. Mock das Disciplinas (Também coordenação)
        Disciplina bd1 = new Disciplina("BD1", professoresMock.get(0), 80);
        Disciplina es1 = new Disciplina("ES1", professoresMock.get(1), 80);
        // Grade semanal
        Stream.of(2, 3, 4).map(DayOfWeek::of).forEach(day -> {
            bd1.addAulaPorDia(new AulasPorDia(null, day, 2));
            es1.addAulaPorDia(new AulasPorDia(null, day, 2));
        });

        // 3. Mock dos Temas (Seria o trabalho do professor)
        bd1.addTema(new Tema(null, "Introdução a SGBDs", 10, 30, Prioridade.ALTA, false, 1));
        bd1.addTema(new Tema(null, "Modelagem DER", 15, 20, Prioridade.MEDIA, false, 2));
        es1.addTema(new Tema(null, "Avaliação", 5, 5, Prioridade.BAIXA, true, 3));

        disciplinasMock.add(bd1);
        disciplinasMock.add(es1);
    }

    // API para os Controllers
    public static List<Professor> getAllProfessores() { return professoresMock; }

    public static List<Semestre> getAllSemestres() { return semestresMock; }

    public static List<Disciplina> getDisciplinasPorProfessor(Professor professor) {
        return disciplinasMock.stream()
                .filter(d -> d.getProfessor().getEmail().equals(professor.getEmail()))
                .collect(Collectors.toList());
    }

    public static Professor getProfessorPorNome(String nome) {
        return professoresMock.stream()
                .filter(p -> p.getNome().equalsIgnoreCase(nome))
                .findFirst().orElse(null);
    }

    // Getters e Setters de Sessão
    public static void setProfessorLogado(Professor professor) { professorLogado = professor; }
    public static Professor getProfessorLogado() { return professorLogado; }

    public static void setDisciplinaSelecionada(Disciplina disciplina) { disciplinaSelecionada = disciplina; }
    public static Disciplina getDisciplinaSelecionada() { return disciplinaSelecionada; }
}