package Entities;

import java.util.Date;

public class Session {

    private int id,cap;
    private String date;
    private String des;
    private String type;
    private String coach;

    public Session() {}

    public Session(int id, int cap, String des, String type , Date date, String coach) {
       this.id = id;
       this.cap = cap;
       this.des = des;
       this.type = type;
       this.date = date.toString();
       this.coach = coach;
    }

    public Session(Integer cap, String des, String type, Date date, String coach) {
        this.cap = cap;
        this.des = des;
       this.type = type;
       this.date = String.valueOf(date);
       this.coach = coach;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Integer getCap() {
        return cap;
    }

    public void setCap(Integer cap) {
       this.cap = cap;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
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

    public String getDate() {
        return  date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}