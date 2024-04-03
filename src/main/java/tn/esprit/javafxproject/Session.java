package tn.esprit.javafxproject;

import javafx.scene.chart.PieChart;

import java.util.Date;

public class Session {

    private int Cap,ID;
    private String Des;

    public int getId() {
        return ID;
    }

    public void setId(int id) {
        this.ID = id;
    }

    public int getCap() {
        return Cap;
    }

    public void setCap(int cap) {
        Cap = cap;
    }

    public String getDes() {
        return Des;
    }

    public void setDes(String des) {
        Des = des;
    }}