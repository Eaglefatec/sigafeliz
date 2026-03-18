package com.eaglefatec.sigafeliz.model;

public class Tema {
    private int id;
    private int planningUnitId;
    private String title;
    private int minAulas;
    private int maxAulas;
    private Priority priority;
    private boolean isEvaluation;

    public enum Priority {
        ALTO, MEDIO, BAIXO
    }

    public Tema() {
    }

    public Tema(String title, int minAulas, int maxAulas, Priority priority, boolean isEvaluation) {
        this.title = title;
        this.minAulas = minAulas;
        this.maxAulas = maxAulas;
        this.priority = priority;
        this.isEvaluation = isEvaluation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlanningUnitId() {
        return planningUnitId;
    }

    public void setPlanningUnitId(int planningUnitId) {
        this.planningUnitId = planningUnitId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMinAulas() {
        return minAulas;
    }

    public void setMinAulas(int minAulas) {
        this.minAulas = minAulas;
    }

    public int getMaxAulas() {
        return maxAulas;
    }

    public void setMaxAulas(int maxAulas) {
        this.maxAulas = maxAulas;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public boolean isEvaluation() {
        return isEvaluation;
    }

    public void setEvaluation(boolean evaluation) {
        isEvaluation = evaluation;
    }
}
