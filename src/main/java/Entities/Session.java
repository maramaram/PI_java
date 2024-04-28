package Entities;

import java.sql.Date;

public class Session {

    private int id, cap;
    private String date;
    private String type;
    private String coach;

    public Session(int id, int cap, String type, Date date, String coach) {
        this.id = id;
        this.cap = cap;
        this.type = type;
        this.date = date.toString();
        this.coach = coach;
    }

    public Session(int cap, String type, Date date, String coach) {
        this.cap = cap;
        this.type = type;
        this.date = String.valueOf(date);
        this.coach = coach;
    }

    public Session() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCap() {
        return cap;
    }

    public void setCap(int cap) {
        this.cap = cap;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }
}