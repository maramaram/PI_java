package Entities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Livreur {
    private int id;
    private String nom;
    private String prenom;
    private String disponibilite;
    private String image;
    private Integer note; // Utiliser Integer pour autoriser les valeurs null
    private ImageView img;
    public Livreur() {
    }

    public Livreur(int id, String nom, String prenom, String disponibilite, String image, Integer note) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.disponibilite = disponibilite;
        this.image = image;
        this.note = note;
        img = new ImageView(new Image(this.getClass().getResourceAsStream("/"+image)));
        img.setFitWidth(125); // Définissez la largeur souhaitée
        img.setFitHeight(100); // Définissez la hauteur souhaitée
    }

    public Livreur(String nom, String prenom, String disponibilite, String image, Integer note) {
        this.nom = nom;
        this.prenom = prenom;
        this.disponibilite = disponibilite;
        this.image = image;
        this.note = note;
        img = new ImageView(new Image(this.getClass().getResourceAsStream("/"+image)));
        img.setFitWidth(125); // Définissez la largeur souhaitée
        img.setFitHeight(100); // Définissez la hauteur souhaitée
    }
    // Getters and setters

    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(String disponibilite) {
        this.disponibilite = disponibilite;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Livreur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", disponibilite='" + disponibilite + '\'' +
                ", image='" + image + '\'' +
                ", note=" + note +
                '}';
    }
}
