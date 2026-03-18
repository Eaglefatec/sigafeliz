package com.eaglefatec.sigafeliz.model;

public class Semester {
    private int id;
    private String name;
    private String startDate;   // ISO format yyyy-MM-dd
    private String endDate;
    private String kickoffDate; // ISO format yyyy-MM-dd — Kickoff do Projeto Integrador

    public Semester() {}

    public Semester(String name, String startDate, String endDate, String kickoffDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.kickoffDate = kickoffDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getKickoffDate() { return kickoffDate; }
    public void setKickoffDate(String kickoffDate) { this.kickoffDate = kickoffDate; }

    @Override
    public String toString() { return name; }
}
