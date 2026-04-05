package com.sigafeliz.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Semestre {
    
    private String nome;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private LocalDate dataKickoff;

    private List<SabadoLetivo> sabadosLetivos;
    private List<DiaRestrito> diasRestritos;

    public Semestre(String nome, LocalDate dataInicio, LocalDate dataFim, LocalDate dataKickoff) {
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.dataKickoff = dataKickoff;
        this.sabadosLetivos = new ArrayList<>();
        this.diasRestritos = new ArrayList<>();
    }

    public void addSabadoLetivo(SabadoLetivo sabado) {
        this.sabadosLetivos.add(sabado);
        sabado.setSemestre(this);
    }

    public void addDiaRestrito(DiaRestrito dia) {
        this.diasRestritos.add(dia);
        dia.setSemestre(this);
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public LocalDate getDataKickoff() { return dataKickoff; }
    public void setDataKickoff(LocalDate dataKickoff) { this.dataKickoff = dataKickoff; }

    public List<SabadoLetivo> getSabadosLetivos() { return sabadosLetivos; }
    public List<DiaRestrito> getDiasRestritos() { return diasRestritos; }
}