package com.eaglefatec.sigafeliz.model;

import java.time.LocalDate;

/**
 * Represents a single scheduled aula (class) with its assigned date and
 * details.
 */
public class ScheduledAula {
    private LocalDate date;
    private int aulaNumber; // Sequential number (1..40 or 1..80)
    private String temaTitle;
    private int aulasOnThisDay; // How many aulas happen on this date
    private String hora; // Time slot description
    private boolean isEvaluation; // Marcador de prova
    private String observation; // Observações (feriado, sprint, etc.)
    private String dayOfWeek; // Dia da semana (Segunda, Terça, etc.)

    public ScheduledAula() {
    }

    public ScheduledAula(LocalDate date, int aulaNumber, String temaTitle, int aulasOnThisDay) {
        this.date = date;
        this.aulaNumber = aulaNumber;
        this.temaTitle = temaTitle;
        this.aulasOnThisDay = aulasOnThisDay;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getAulaNumber() {
        return aulaNumber;
    }

    public void setAulaNumber(int aulaNumber) {
        this.aulaNumber = aulaNumber;
    }

    public String getTemaTitle() {
        return temaTitle;
    }

    public void setTemaTitle(String temaTitle) {
        this.temaTitle = temaTitle;
    }

    public int getAulasOnThisDay() {
        return aulasOnThisDay;
    }

    public void setAulasOnThisDay(int aulasOnThisDay) {
        this.aulasOnThisDay = aulasOnThisDay;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public boolean isEvaluation() {
        return isEvaluation;
    }

    public void setEvaluation(boolean evaluation) {
        isEvaluation = evaluation;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
