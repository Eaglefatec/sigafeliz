package com.sigafeliz.model;

public class Tema {
    private Disciplina disciplina;
    private String titulo;
    private int cargaMinima;
    private int cargaMaxima;
    private Prioridade prioridade;
    private boolean eAvaliacao;
    private int ordem;

    public Tema(Disciplina disciplina, String titulo, int cargaMinima, int cargaMaxima, Prioridade prioridade, boolean eAvaliacao, int ordem) {
        this.disciplina = disciplina;
        this.titulo = titulo;
        this.cargaMinima = cargaMinima;
        this.cargaMaxima = cargaMaxima;
        this.prioridade = prioridade;
        this.eAvaliacao = eAvaliacao;
        this.ordem = ordem;
    }

    public Disciplina getDisciplina() { return disciplina; }
    public void setDisciplina(Disciplina disciplina) { this.disciplina = disciplina; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public int getCargaMinima() { return cargaMinima; }
    public void setCargaMinima(int cargaMinima) { this.cargaMinima = cargaMinima; }

    public int getCargaMaxima() { return cargaMaxima; }
    public void setCargaMaxima(int cargaMaxima) { this.cargaMaxima = cargaMaxima; }

    public Prioridade getPrioridade() { return prioridade; }
    public void setPrioridade(Prioridade prioridade) { this.prioridade = prioridade; }

    public boolean isEAvaliacao() { return eAvaliacao; }
    public void setEAvaliacao(boolean eAvaliacao) { this.eAvaliacao = eAvaliacao; }

    public int getOrdem() { return ordem; }
    public void setOrdem(int ordem) { this.ordem = ordem; }
}