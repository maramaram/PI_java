package Entities;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Defi {
    private int id ;
    private String nom,des,nd,nomx;
    private int nbj;
    private ArrayList<Exercice> lex = new ArrayList<Exercice>();

    public Defi(int id, String nom, String des, String nd, int nbj, ArrayList<Exercice> lex) {
        this.id = id;
        this.nom = nom;
        this.des = des;
        this.nd = nd;
        this.nbj = nbj;
        this.lex = lex;

    }

    public Defi(String nom, String des, String nd, int nbj, ArrayList<Exercice> lex) {
        this.nom = nom;
        this.des = des;
        this.nd = nd;
        this.nbj = nbj;
        this.lex = lex;

    }

    public void setNomx(String nomx) {
        this.nomx = nomx;
    }

    public String getNomx() {
        return nomx;
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

    public String getNd() {
        return nd;
    }

    public int getNbj() {
        return nbj;
    }

    public ArrayList<Exercice> getLex() {
        return lex;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public void setNd(String nd) {
        this.nd = nd;
    }

    public void setNbj(int nbj) {
        this.nbj = nbj;
    }

    public void setLex(ArrayList<Exercice> lex) {
        this.lex = lex;
        this.nomx = lex.stream()
                .map(Exercice::getNom)
                .collect(Collectors.joining(" / "));
    }

    @Override
    public String toString() {
        return "Defi{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", des='" + des + '\'' +
                ", nd='" + nd + '\'' +
                ", nbj=" + nbj +
                ", lex=" + lex +
                '}';
    }
}
