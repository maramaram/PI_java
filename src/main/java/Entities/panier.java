package Entities;

import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class panier {
    private int id,prix_tot,quantite;
private String nomx;
    private List<product> productList;

    public panier(int id, int prix_tot, int quantite) {
        this.id = id;
        this.prix_tot = prix_tot;
        this.quantite = quantite;
       // this.products =products != null ?products : new ArrayList<>();
    }

    public panier(int id, int prix_tot, int quantite, ArrayList<product> productList) {
        this.id = id;
        this.prix_tot = prix_tot;
        this.quantite = quantite;
        this.productList = productList;
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
        this.nomx = productList.stream()
                .map(product::getNom)
                .collect(Collectors.joining(" / "));

        System.out.println(nomx);
    }

    public String getNomx() {
        return nomx;
    }

    public void setNomx(String nomx) {
        this.nomx = nomx;
    }

    public String getProduitsAsString() {
        if (productList == null || productList.isEmpty()) {
            return "";
        }

        StringBuilder produitsString = new StringBuilder();
        for (product p : productList) {
            produitsString.append(p.getNom()).append(", ");
        }
        produitsString.setLength(produitsString.length() - 2); // Supprime la virgule et l'espace en trop à la fin
        return produitsString.toString();
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
                ", productList=" + productList +
                '}';
    }

    public void ajouterProduit(product product) {
        productList.add(product);
        System.out.println("Produit ajouté au panier : " + product.getNom());
    }
}
