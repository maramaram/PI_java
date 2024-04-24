package Entities;

import java.util.ArrayList;
import java.util.List;


public class panier {
    private int id,prix_tot,quantite;

    private List<product> productList;

    public panier(int id, int prix_tot, int quantite) {
        this.id = id;
        this.prix_tot = prix_tot;
        this.quantite = quantite;
       // this.products =products != null ?products : new ArrayList<>();
    }

    public panier(int prix_tot, int quantite) {
        this.prix_tot = prix_tot;
        this.quantite = quantite;
    }

    public List<product> getproductList() {
        return productList;
    }

    public void setproductList(List<product> productList) {
        this.productList = productList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrix_tot() {
        return prix_tot;
    }

    public void setPrix_tot(int prix_tot) {
        this.prix_tot = prix_tot;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    @Override
    public String toString() {
        return "panier{" +
                "id=" + id +
                ", prix_tot=" + prix_tot +
                ", quantite=" + quantite +
                '}';
    }
}
