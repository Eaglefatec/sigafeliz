package com.sigafeliz.model;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Semestre {
    private String nome;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private LocalDate dataKickoff;

    // A existência dos dias restritos e sabados letivos está fortemente acoplada ao semestre a qual pertencem.
    // Nesse caso, definimos uma referência à lista dentro da classe para ambos serem persistidos juntos.
    // final é usado para que a referencia (variável) das listas sejam sempre constantes.
    private final List<SabadoLetivo> sabadosLetivos;
    private final List<DiaRestrito> diasRestritos;

    public Semestre(String nome, LocalDate dataInicio, LocalDate dataFim, LocalDate dataKickoff) {
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.dataKickoff = dataKickoff;
        sabadosLetivos = new ArrayList<>();
        diasRestritos = new ArrayList<>();
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public LocalDate getDataKickoff() { return dataKickoff; }
    public void setDataKickoff(LocalDate dataKickoff) { this.dataKickoff = dataKickoff; }

    // Ao invés de setters normais, somente queremos adicionar ou remover itens da lista.
    // Um setter definiria uma nova lista toda vez que chamassemos.
    public List<SabadoLetivo> getSabadosLetivos() { return sabadosLetivos; }
    public void addSabadoLetivo(SabadoLetivo sabadoLetivo) {
        this.sabadosLetivos.add(sabadoLetivo);
    }

    public List<DiaRestrito> getDiasRestritos() { return diasRestritos; }
    public void addDiaRestrito(DiaRestrito diaRestrito) {
        this.diasRestritos.add(diaRestrito);
    }
}
