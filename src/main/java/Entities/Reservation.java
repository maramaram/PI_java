package Entities;

public class Reservation {
    int id;
    String date,session,client,action;

    public Reservation(int id, String date, String session, String client, String action) {
        this.id = id;
        this.date = date;
        this.session = session;
        this.client = client;
        this.action = action;
    }
    public Reservation(String date, String session, String client, String action) {
        this.date = date;
        this.session = session;
        this.client = client;
        this.action = action;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", session='" + session + '\'' +
                ", client='" + client + '\'' +
                ", action='" + action + '\'' +
                '}';
    }
}
