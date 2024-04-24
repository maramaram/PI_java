package Entities;

import Utils.DataBase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class User {
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private String pwd;
    private LocalDate date_N;
    private String role;
    private String adress;
    private String photo;
    private String status = "inactive";
    private String num_tel;

    public User(String nom, String prenom, String email, String pwd, LocalDate date_N, String role, String adress, String photo, String num_tel) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.pwd = pwd;
        this.date_N = date_N;
        this.role = role;
        this.adress = adress;
        this.photo = photo;
        this.num_tel = num_tel;
    }

    public User(String id, String nom, String prenom, String email, LocalDate date_N, String role, String adress, String photo, String status, String num_tel) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.date_N = date_N;
        this.role = role;
        this.adress = adress;
        this.photo = photo;
        this.status = status;
        this.num_tel = num_tel;
    }

    public User() {
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public LocalDate getDate_N() {
        return date_N;
    }

    public void setDate_N(LocalDate date_N) {
        this.date_N = date_N;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNum_tel() {
        return num_tel;
    }

    public void setNum_tel(String num_tel) {
        this.num_tel = num_tel;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", pwd='" + pwd + '\'' +
                ", date_N=" + date_N +
                ", role='" + role + '\'' +
                ", adress='" + adress + '\'' +
                ", photo='" + photo + '\'' +
                ", status='" + status + '\'' +
                ", num_tel='" + num_tel + '\'' +
                '}';
    }


}
