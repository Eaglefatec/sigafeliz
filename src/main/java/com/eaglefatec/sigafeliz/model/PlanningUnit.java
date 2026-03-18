package com.eaglefatec.sigafeliz.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlanningUnit {
    private int id;
    private int semesterId;
    private String subjectName;
    private int workload; // 40 or 80
    private String weeklyScheduleJson; // JSON map: {"MONDAY":2,"WEDNESDAY":2,...}
    private String createdAt;
    private List<Tema> temas = new ArrayList<>();

    public PlanningUnit() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(int semesterId) {
        this.semesterId = semesterId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getWorkload() {
        return workload;
    }

    public void setWorkload(int workload) {
        this.workload = workload;
    }

    public String getWeeklyScheduleJson() {
        return weeklyScheduleJson;
    }

    public void setWeeklyScheduleJson(String weeklyScheduleJson) {
        this.weeklyScheduleJson = weeklyScheduleJson;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<Tema> getTemas() {
        return temas;
    }

    public void setTemas(List<Tema> temas) {
        this.temas = temas;
    }
}
