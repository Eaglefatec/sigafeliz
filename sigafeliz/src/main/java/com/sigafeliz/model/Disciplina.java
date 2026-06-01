package com.sigafeliz.model;

import java.util.ArrayList;
import java.util.List;


public class Disciplina {
    private String curso;
    private String nome;
    private Professor professor;
    private int cargaHorariaTotal;

    private final List<AulasPorDia> aulasPorDia;
    private final List<Tema> temas;

    // Construtor completo com curso
    public Disciplina(String curso, String nome, Professor professor, int cargaHorariaTotal) {
        this.curso = curso;
        this.nome = nome;
        this.professor = professor;
        this.cargaHorariaTotal = cargaHorariaTotal;
        this.aulasPorDia = new ArrayList<>();
        this.temas = new ArrayList<>();
    }

    // Construtor antigo para retrocompatibilidade
    public Disciplina(String nome, Professor professor, int cargaHorariaTotal) {
        this("", nome, professor, cargaHorariaTotal);
    }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    public void addAulaPorDia(AulasPorDia aula) {
        this.aulasPorDia.add(aula);
        aula.setDisciplina(this);
    }

    public void addTema(Tema tema) {
        this.temas.add(tema);
        tema.setDisciplina(this);
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Professor getProfessor() { return professor; }
    public void setProfessor(Professor professor) { this.professor = professor; }

    public int getCargaHorariaTotal() { return cargaHorariaTotal; }
    public void setCargaHorariaTotal(int cargaHorariaTotal) { this.cargaHorariaTotal = cargaHorariaTotal; }

    public List<AulasPorDia> getAulasPorDia() { return aulasPorDia; }
    public List<Tema> getTemas() { return temas; }
}