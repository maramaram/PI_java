package Service;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Entities.Post;
import Entities.Comment;
import Utils.MyDatabase;


public class PostService implements ICRUD<Post> {

    private Connection conx;
    private Statement stm;
    private PreparedStatement pstm;

    public PostService() {
        conx = MyDatabase.getInstance().getConx();
    }

    @Override
    public void add(Post post) throws SQLException {
        String req = "INSERT INTO `Post`(`title`, `contenu`, `date`, `image`, `views`) VALUES (?, ?, ?, ?, ?)";
        pstm = conx.prepareStatement(req);
        pstm.setString(1, post.getTitle());
        pstm.setString(2, post.getContenu());
        pstm.setDate(3, new java.sql.Date(post.getDate().getTime()));
        pstm.setString(4, post.getImage());
        pstm.setInt(5, post.getViews());

        pstm.executeUpdate();
        System.out.println("Post added successfully.");
    }

    @Override
    public void addMeth2(Post post) throws SQLException {

    }

    public void modifier(Post post) throws SQLException {
        String req = "UPDATE `Post` SET `title`=?, `contenu`=?, `date`=?, `image`=?, `views`=? WHERE `id`=?";
        pstm = conx.prepareStatement(req);
        pstm.setString(1, post.getTitle());
        pstm.setString(2, post.getContenu());
        pstm.setDate(3, new java.sql.Date(post.getDate().getTime()));
        pstm.setString(4, post.getImage());
        pstm.setInt(5, post.getViews());
        pstm.setInt(6, post.getId());

        pstm.executeUpdate();
        System.out.println("Post modified successfully.");
    }

    @Override
    public void delete(Post post) throws SQLException {
        String req = "DELETE FROM `Post` WHERE `id`=?";
        pstm = conx.prepareStatement(req);
        pstm.setInt(1, post.getId());

        pstm.executeUpdate();
        System.out.println("Post deleted successfully.");
    }

    @Override
    public List<Post> afficherList() throws SQLException {
        String req = "SELECT * FROM `Post`";

        stm = conx.createStatement();
        ResultSet res = stm.executeQuery(req);

        List<Post> posts = new ArrayList<>();

        while (res.next()) {
            int id = res.getInt("id");
            String title = res.getString("title");
            String contenu = res.getString("contenu");
            Date date = res.getDate("date");
            String image = res.getString("image");
            int views = res.getInt("views");

            // Create a new Post object and add it to the list
            posts.add(new Post(id, title, contenu, date, image, views, new ArrayList<>()));
        }

        return posts;
    }

    @Override
    public List<Post> afficherListSearch(String s) throws SQLException {
        String req = "SELECT * FROM `Post` WHERE `title` LIKE '"+"%"+s+"%"+"' OR `views` LIKE '"+"%"+s+"%'";

        stm = conx.createStatement();
        ResultSet res = stm.executeQuery(req);

        List<Post> posts = new ArrayList<>();

        while (res.next()) {
            int id = res.getInt("id");
            String title = res.getString("title");
            String contenu = res.getString("contenu");
            Date date = res.getDate("date");
            String image = res.getString("image");
            int views = res.getInt("views");

            // Create a new Post object and add it to the list
            posts.add(new Post(id, title, contenu, date, image, views, new ArrayList<>()));
        }

        return posts;
    }


    public void addCommentToPost(int postId, Comment comment) throws SQLException {
        // Ajoutez d'abord le commentaire à la base de données
        String commentQuery = "INSERT INTO Comment (contenu, nblike, post_id) VALUES (?, ?, ?)";
        pstm = conx.prepareStatement(commentQuery);
        pstm.setString(1, comment.getContenu());
        pstm.setInt(2, comment.getNblikes());
        pstm.setInt(3, postId); // Spécifiez l'identifiant du post pour le commentaire
        pstm.executeUpdate();

        // Ensuite, récupérez le post correspondant à l'identifiant donné
        String postQuery = "SELECT * FROM Post WHERE id = ?";
        pstm = conx.prepareStatement(postQuery);
        pstm.setInt(1, postId);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()) {
            // Extraire les détails du post de la base de données
            int id = resultSet.getInt("id");
            String title = resultSet.getString("title");
            String contenu = resultSet.getString("contenu");
            Date date = resultSet.getDate("date");
            String image = resultSet.getString("image");
            int views = resultSet.getInt("views");

            // Créez un objet Post avec les détails récupérés
            Post post = new Post(id, title, contenu, date, image, views, new ArrayList<>());

            // Récupérez la liste des commentaires actuels du post
            List<Comment> comments = post.getComments();

            // Ajoutez le nouveau commentaire à la liste des commentaires du post
            comments.add(comment);
            post.setComments(comments); // Mettez à jour la liste des commentaires du post
        } else {
            throw new SQLException("No post found with the specified ID.");
        }

        System.out.println("Comment added to the post successfully.");
    }

    public List<Comment> afficherList2(int postId) throws SQLException {
        String req = "SELECT * FROM `comment` WHERE `post_id` = ?";
        pstm = conx.prepareStatement(req);
        pstm.setInt(1, postId);
        ResultSet res = pstm.executeQuery();

        List<Comment> comments = new ArrayList<>();

        while (res.next()) {
            int id = res.getInt("id"); // Assuming "id" is the first column
            int nbLikes = res.getInt("nblike"); // Assuming "nblikes" is the second column
            String contenu = res.getString("contenu"); // Assuming "contenu" is the third column
            int post_id = res.getInt("post_id"); // Assuming "id" is the first column

            // Create a new Comment object using retrieved data
            Comment comment = new Comment(id, nbLikes, contenu , post_id);
            comments.add(comment);
        }

        return comments;
    }




}

