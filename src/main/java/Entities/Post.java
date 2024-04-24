package Entities;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

import java.util.Date;


public class Post {

    private ImageView image1;
    private int id;
    private String title;
    private String contenu;
    private Date date;
    private String image;
    private int views;
    private List<Comment> comments;


    public Post(int id, String title, String contenu, Date date, String image, int views, List<Comment> comments) {
        this.id = id;
        this.title = title;
        this.contenu = contenu;
        this.date = date;
        this.image = image;
        this.views = views;
        image1 = new ImageView(new Image(this.getClass().getResourceAsStream("/"+image)));
        image1.setFitWidth(125); // Définissez la largeur souhaitée
        image1.setFitHeight(100); // Définissez la hauteur souhaitée

        // Initialize comments list to an empty ArrayList if not provided
        this.comments = comments != null ? comments : new ArrayList<>();
    }

    public ImageView getImage1() {
        return image1;
    }

    public void setImage1(ImageView image1) {
        this.image1 = image1;
    }

    public Post(String title, String contenu, Date date, String image, int views, List<Comment> comments) {

        this.title = title;
        this.contenu = contenu;
        this.date = date;
        this.image = image;
        this.views = views;
        image1 = new ImageView(new Image(this.getClass().getResourceAsStream("/"+image)));
        image1.setFitWidth(125); // Définissez la largeur souhaitée
        image1.setFitHeight(100); // Définissez la hauteur souhaitée


        // Initialize comments list to an empty ArrayList if not provided
        this.comments = comments != null ? comments : new ArrayList<>();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contenu='" + contenu + '\'' +
                ", date=" + date +
                ", image='" + image + '\'' +
                ", views=" + views +
                ", comments=[");

        for (Comment comment : comments) {
            stringBuilder.append(comment.toString()).append(", ");
        }

        // Supprimer la virgule et l'espace en trop à la fin
        if (!comments.isEmpty()) {
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }

        stringBuilder.append("]}");
        return stringBuilder.toString();
    }



}
