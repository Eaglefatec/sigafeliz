package com.sigafeliz.model;

import java.time.LocalDate;

public class SabadoLetivo {
    private Semestre semestre;
    private LocalDate data;

    public SabadoLetivo(Semestre semestre, LocalDate data) {
        this.semestre = semestre;
        this.data = data;
    }

    public Semestre getSemestre() { return semestre; }
    public void setSemestre(Semestre semestre) { this.semestre = semestre; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
}