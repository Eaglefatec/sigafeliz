package com.sigafeliz.model;

import java.time.DayOfWeek;

public class AulasPorDia {
    private Disciplina disciplina;
    private DayOfWeek diaSemana;
    private int quantidadeAulas;

    public AulasPorDia(Disciplina disciplina, DayOfWeek diaSemana, int quantidadeAulas) {
        this.disciplina = disciplina;
        this.diaSemana = diaSemana;
        this.quantidadeAulas = quantidadeAulas;
    }

    public Disciplina getDisciplina() { return disciplina; }
    public void setDisciplina(Disciplina disciplina) { this.disciplina = disciplina; }

    public DayOfWeek getDiaSemana() { return diaSemana; }
    public void setDiaSemana(DayOfWeek diaSemana) { this.diaSemana = diaSemana; }

    public int getQuantidadeAulas() { return quantidadeAulas; }
    public void setQuantidadeAulas(int quantidadeAulas) { this.quantidadeAulas = quantidadeAulas; }
}