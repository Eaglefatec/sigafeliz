package com.sigafeliz.model;

import java.time.LocalDate;

public class DiaRestrito {
    private Semestre semestre;
    private LocalDate data;
    private String descricao;

    public DiaRestrito(Semestre semestre, LocalDate data, String descricao) {
        this.semestre = semestre;
        this.data = data;
        this.descricao = descricao;
    }

    public Semestre getSemestre() { return semestre; }
    public void setSemestre(Semestre semestre) { this.semestre = semestre; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
