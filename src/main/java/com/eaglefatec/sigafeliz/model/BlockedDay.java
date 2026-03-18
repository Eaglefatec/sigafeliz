package com.eaglefatec.sigafeliz.model;

public class BlockedDay {
    private int id;
    private int semesterId;
    private String blockedDate; // ISO yyyy-MM-dd
    private String description;
    private DayType dayType;

    public enum DayType {
        HOLIDAY, EVENT, SPRINT_REVIEW, SABADO_LETIVO
    }

    public BlockedDay() {
    }

    public BlockedDay(int semesterId, String blockedDate, String description, DayType dayType) {
        this.semesterId = semesterId;
        this.blockedDate = blockedDate;
        this.description = description;
        this.dayType = dayType;
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

    public String getBlockedDate() {
        return blockedDate;
    }

    public void setBlockedDate(String blockedDate) {
        this.blockedDate = blockedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DayType getDayType() {
        return dayType;
    }

    public void setDayType(DayType dayType) {
        this.dayType = dayType;
    }
}
