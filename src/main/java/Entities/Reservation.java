package Entities;

import java.time.LocalDate;
import java.util.Date;

public class Reservation {
    private int id;
    private String session;
    private String date;
    private String etat;
    private String client;

    public Reservation() {
        // Constructeur par défaut
    }

    public Reservation(int id, String session, Date date, String etat, String client) {
        this.id = id;
        this.session = session;
        this.date = date.toString();
        this.etat = etat;
        this.client = client;
    }

    public Reservation(String session, Date date, String etat, String client ) {
        this.session = session;
        this.date = String.valueOf(date);
        this.etat = etat;
        this.client = client;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", sessionId='" + session + '\'' +
                ", date=" + date +
                ", etat='" + etat + '\'' +
                ", client='" + client + '\'' +
                '}';
    }
}