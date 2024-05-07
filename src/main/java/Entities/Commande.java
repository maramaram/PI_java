package Entities;

public class Commande {
    private int id;
    private int livreurId;
    private int userId;
    private String statut;
    private int prixTotal;

    public Commande() {

    }
    public Commande(int id, int livreurId, int userId, String statut, int prixTotal) {
        this.id = id;
        this.livreurId = livreurId;
        this.userId = userId;
        this.statut = statut;
        this.prixTotal = prixTotal;
    }

    public Commande(int livreurId, int userId, String statut, int prixTotal) {
        this.livreurId = livreurId;
        this.userId = userId;
        this.statut = statut;
        this.prixTotal = prixTotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLivreurId() {
        return livreurId;
    }

    public void setLivreurId(int livreurId) {
        this.livreurId = livreurId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public int getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(int prixTotal) {
        this.prixTotal = prixTotal;
    }

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", livreurId=" + livreurId +
                ", userId=" + userId +
                ", statut='" + statut + '\'' +
                ", prixTotal=" + prixTotal +
                '}';
    }
}
