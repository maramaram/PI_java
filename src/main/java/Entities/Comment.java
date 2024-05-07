package Entities;
import java.util.ArrayList;
import java.util.List;

public class Comment {

    private int id;
    private int nblikes;
    private String contenu;
    private int post_id;

    private int user_id;



    public Comment(int id, int nblikes, String contenu, int post_id, int user_id) {
        this.id = id;
        this.nblikes = nblikes;
        this.contenu = contenu;
        this.post_id = post_id;
        this.user_id = user_id;
    }

    public Comment(String contenu,int post_id,int user_id) {
        this.contenu = contenu;

        this.nblikes = 0;
        this.post_id=post_id;
        this.user_id=user_id;

    }


/*    public Comment(String contenu, int nblikes) {
        this.contenu = contenu;
        this.nblikes = nblikes;
    }*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNblikes() {
        return nblikes;
    }

    public void setNblikes(int nblikes) {
        this.nblikes = nblikes;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", nblikes=" + nblikes +
                ", contenu='" + contenu + '\'' +
                '}';
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }


}


