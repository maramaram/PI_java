package Entities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.Objects;

public class Exercice {

private ImageView etat, image, giif;
    private int id ;
    private String nom,des,mc,nd,img,gif ;
private boolean test;

    public Exercice(int id, String nom, String des, String mc, String nd, String img, String gif) {
        this.id = id;
        this.nom = nom;
        this.des = des;
        this.img = img;
        this.gif = gif;
        this.mc = mc;
        this.nd = nd;
        image = new ImageView(new Image(this.getClass().getResourceAsStream("/"+img)));
        image.setFitWidth(125); // Définissez la largeur souhaitée
        image.setFitHeight(100); // Définissez la hauteur souhaitée

        giif = new ImageView(new Image(this.getClass().getResourceAsStream("/"+gif)));
        giif.setFitWidth(125); // Définissez la largeur souhaitée
        giif.setFitHeight(100); // Définissez la hauteur souhaitée


    }



    public Exercice(String nom, String des, String mc, String nd, String img, String gif) {
        this.nom = nom;
        this.des = des;
        this.img = img;
        this.gif = gif;
        this.mc = mc;
        this.nd = nd;
        image = new ImageView(new Image(this.getClass().getResourceAsStream("/"+img)));
        image.setFitWidth(125); // Définissez la largeur souhaitée
        image.setFitHeight(100); // Définissez la hauteur souhaitée

        giif = new ImageView(new Image(this.getClass().getResourceAsStream("/"+gif)));
        giif.setFitWidth(125); // Définissez la largeur souhaitée
        giif.setFitHeight(100); // Définissez la hauteur souhaitée
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public ImageView getGiif() {
        return giif;
    }

    public void setGiif(ImageView giif) {
        this.giif = giif;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ImageView getImage() {
        return image;
    }

    public ImageView getEtat() {
        return etat;
    }

    public void setEtat(ImageView etat) {
        this.etat = etat;
    }


    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getDes() {
        return des;
    }

    public String getImg() {
        return img;
    }

    public String getGif() {
        return gif;
    }

    public String getMc() {
        return mc;
    }

    public String getNd() {
        return nd;
    }


    public void setid(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setGif(String gif) {
        this.gif = gif;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public void setNd(String nd) {
        this.nd = nd;
    }

    @Override
    public String toString() {
        return "Exercice{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", des='" + des + '\'' +
                ", img='" + img + '\'' +
                ", gif='" + gif + '\'' +
                ", mc=" + mc +
                ", nd=" + nd +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercice exercice = (Exercice) o;
        return id == exercice.id && Objects.equals(nom, exercice.nom) && Objects.equals(des, exercice.des) && Objects.equals(mc, exercice.mc) && Objects.equals(nd, exercice.nd) && Objects.equals(img, exercice.img) && Objects.equals(gif, exercice.gif);
    }

}
