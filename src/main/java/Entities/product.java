package Entities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class product {
    private int id,quantite_stock;
    ImageView img;
    private String nom,description,categories,image;
    private float prix;

    public product(int id, String nom, String description,int quantite_stock, String categories, float prix ,String image) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.quantite_stock = quantite_stock;
        this.categories = categories;
        this.prix = prix;
        this.image = image;
        //this.img=new ImageView(new Image(this.getClass().getResourceAsStream("/"+image)));
       // this.img.setFitHeight(125);
       // this.img.setFitWidth(125);
        if (this.getClass().getResourceAsStream("/"+image) != null) {
            this.img = new ImageView(new Image(this.getClass().getResourceAsStream("/"+image)));
            this.img.setFitHeight(125);
            this.img.setFitWidth(125);
        } else {
            System.out.println("Image not found: " + image);
            // ou lancez une exception ou affichez un message d'erreur
        }


    }

    public product(String nom, String description,int quantite_stock, String categories, float prix,  String image) {

        this.nom = nom;
        this.description = String.valueOf(description);
        this.quantite_stock = quantite_stock;
        this.categories = categories;
        this.prix = Float.parseFloat(String.valueOf(prix));
        this.image = String.valueOf(image);

        //this.img=new ImageView(new Image(this.getClass().getResourceAsStream(image)));
       /// this.img.setFitHeight(125);
        //this.img.setFitWidth(75);
        if (this.getClass().getResourceAsStream("/"+image) != null) {
            this.img = new ImageView(new Image(this.getClass().getResourceAsStream("/"+image)));
            this.img.setFitHeight(125);
            this.img.setFitWidth(125);
        } else {
            System.out.println("Image not found: " + image);
            // ou lancez une exception ou affichez un message d'erreur
        }

    }



    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getQuantite_stock() {
        return quantite_stock;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public String getCategories() {
        return categories;
    }

    public String getImage() {
        return image;
    }

    public float getPrix() {
        return prix;
    }

    public void setQuantite_stock(int quantite_stock) {
        this.quantite_stock = quantite_stock;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }
    @Override
    public String toString() {
        return "product{" +
                "id=" + id +
                ", quantite_stock=" + quantite_stock +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", categories='" + categories + '\'' +
                ", image='" + image + '\'' +
                ", prix=" + prix +
                '}';
    }
}

