package Service;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Entities.Post;
import Entities.Comment;
import Utils.MyDatabase;



public class CommentService implements ICRUD<Comment> {

    private Connection conx;

    public CommentService() {
        conx = MyDatabase.getInstance().getConx();
    }

    @Override
    public void add(Comment comment) throws SQLException {
        String req = "INSERT INTO `comment` (`contenu`, `nblikes`, `post_id`) VALUES (?, 0, ?)";
        try (PreparedStatement pstm = conx.prepareStatement(req)) {
            pstm.setString(1, comment.getContenu());
            pstm.setInt(2, comment.getPost_id());
            pstm.executeUpdate();
            System.out.println("Comment added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void addMeth2(Comment comment) {
        // Not implemented; consider implementing or removing
    }

    @Override
    public void modifier(Comment comment) throws SQLException {
        String req = "UPDATE `comment` SET `contenu` = ?, `nblikes` = ? WHERE `id` = ?";
        try (PreparedStatement pstm = conx.prepareStatement(req)) {
            pstm.setString(1, comment.getContenu());
            pstm.setInt(2, comment.getNblikes());
            pstm.setInt(3, comment.getId());
            pstm.executeUpdate();
            System.out.println("Comment modified successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void delete(Comment comment) throws SQLException {
        String req = "DELETE FROM `comment` WHERE `id` = ?";
        try (PreparedStatement pstm = conx.prepareStatement(req)) {
            pstm.setInt(1, comment.getId());
            pstm.executeUpdate();
            System.out.println("Comment deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Comment> afficherList() throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String req = "SELECT * FROM `comment`";

        try (Statement stm = conx.createStatement();
             ResultSet res = stm.executeQuery(req)) {

            while (res.next()) {
                int id = res.getInt("id");
                int nblike = res.getInt("nblike");
                String contenu = res.getString("contenu");
                int post_id = res.getInt("post_id");
                comments.add(new Comment(id, nblike, contenu, post_id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return comments;
    }

    @Override
    public List<Comment> afficherListSearch(String s) throws SQLException {
        // Not implemented; consider implementing or removing
        return null;
    }

    public List<Comment> afficherListByPostId(int postId) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        // Préparez la requête SQL pour récupérer les commentaires liés à un ID de post spécifique
        String req = "SELECT * FROM `comment` WHERE `post_id` = ?";

        try (PreparedStatement pstm = conx.prepareStatement(req)) {
            // Configurez le paramètre `post_id` de la requête
            pstm.setInt(1, postId);

            try (ResultSet res = pstm.executeQuery()) {
                // Parcourez les résultats de la requête
                while (res.next()) {
                    // Récupérez les données de commentaires
                    int id = res.getInt("id");
                    int nblike = res.getInt("nblike");
                    String contenu = res.getString("contenu");
                    // Créez un objet `Comment` et ajoutez-le à la liste
                    comments.add(new Comment(id, nblike, contenu, postId));
                }
            }
        } catch (SQLException e) {
            // Affichez la trace d'exception en cas d'erreur
            e.printStackTrace();
            throw e;
        }

        // Retournez la liste des commentaires
        return comments;
    }


}