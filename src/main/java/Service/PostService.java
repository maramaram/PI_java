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
        String req = "INSERT INTO `Post`(`title`, `contenu`, `date`, `image`, `views`, `user_id`) VALUES (?, ?, ?, ?, ?, ?)";
        pstm = conx.prepareStatement(req);
        pstm.setString(1, post.getTitle());
        pstm.setString(2, post.getContenu());
        pstm.setDate(3, new java.sql.Date(post.getDate().getTime()));
        pstm.setString(4, post.getImage());
        pstm.setInt(5, post.getViews());
        pstm.setInt(6, post.getUser_id());

        pstm.executeUpdate();
        System.out.println("Post added successfully.");
    }

    @Override
    public void addMeth2(Post post) throws SQLException {

    }

    public void modifier(Post post) throws SQLException {
        String req = "UPDATE `Post` SET `title`=?, `contenu`=?, `date`=?, `image`=?, `views`=? , `user_id`=? WHERE `id`=?";
        pstm = conx.prepareStatement(req);
        pstm.setString(1, post.getTitle());
        pstm.setString(2, post.getContenu());
        pstm.setDate(3, new java.sql.Date(post.getDate().getTime()));
        pstm.setString(4, post.getImage());
        pstm.setInt(5, post.getViews());
        pstm.setInt(6, post.getUser_id());
        pstm.setInt(7, post.getId());


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

    public int countComments(int postId) throws SQLException {
        int commentCount = 0;
        String query = "SELECT COUNT(*) FROM Comment WHERE post_id = ?";
        try (PreparedStatement statement = conx.prepareStatement(query)) {
            statement.setInt(1, postId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                commentCount = resultSet.getInt(1);
            }
        }
        return commentCount;
    }


    public void incrementViews(Post post) throws SQLException {
        // Incrémenter le nombre de vues actuel du post
        post.setViews(post.getViews() + 1);

        // Exécuter la mise à jour dans la base de données
        String req = "UPDATE `Post` SET `views`=? WHERE `id`=?";
        try (PreparedStatement pstm = conx.prepareStatement(req)) {
            pstm.setInt(1, post.getViews());
            pstm.setInt(2, post.getId());
            pstm.executeUpdate();
        }

        System.out.println("Nombre de vues du post incrémenté avec succès.");
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
            int user_id = res.getInt("user_id");

            // Create a new Post object and add it to the list
            posts.add(new Post(id, title, contenu, date, image, views, user_id, new ArrayList<>()));
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
            int user_id = res.getInt("user_id");

            // Create a new Post object and add it to the list
            posts.add(new Post(id, title, contenu, date, image, views, user_id, new ArrayList<>()));
        }

        return posts;
    }






    public int getTotalPostsCount() throws SQLException {
        int totalCount = 0;
        String query = "SELECT COUNT(*) FROM Post";
        try (PreparedStatement statement = conx.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalCount = resultSet.getInt(1);
            }
        }
        return totalCount;
    }

    // Méthode pour obtenir le nombre total de vues de tous les posts
    public int getTotalViewsCount() throws SQLException {
        int totalViews = 0;
        String query = "SELECT SUM(views) FROM Post";
        try (PreparedStatement statement = conx.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalViews = resultSet.getInt(1);
            }
        }
        return totalViews;
    }

    // Méthode pour obtenir le nombre total de commentaires de tous les posts
    public int getTotalCommentsCount() throws SQLException {
        int totalComments = 0;
        String query = "SELECT COUNT(*) FROM Comment";
        try (PreparedStatement statement = conx.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalComments = resultSet.getInt(1);
            }
        }
        return totalComments;
    }



}

