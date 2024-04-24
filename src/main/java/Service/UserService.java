package Service;

import Controller.AdminPannelController;
import Utils.DataBase;
import Entities.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserService  {

    Connection conn;

    public boolean addUser(User user) {
        DataBase dataBase = new DataBase();
        Connection connection = dataBase.getConnect();

        if (connection != null) {
            String sql = "INSERT INTO user (nom, prenom, email, num_tel, pwd, status, role, date_n, photo, adress) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, user.getNom());
                stmt.setString(2, user.getPrenom());
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getNum_tel());
                stmt.setString(5, user.getPwd());
                stmt.setString(6, "inactive");
                stmt.setString(7, user.getRole());
                stmt.setDate(8, Date.valueOf(user.getDate_N())); // Convert LocalDate to java.sql.Date
                stmt.setString(9, user.getPhoto());
                stmt.setString(10, user.getAdress());

                int rowsInserted = stmt.executeUpdate();
                return rowsInserted > 0;
            } catch (SQLException e) {
                e.printStackTrace(); // Handle exception appropriately
                return false;
            } finally {
                try {
                    if (connection != null) {
                        connection.close(); // Close the connection
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return false;
    }


    public User login(String email, String password) {
        DataBase dataBase = new DataBase();
        Connection connection = dataBase.getConnect();

        if (connection != null) {
            String sql = "SELECT * FROM user WHERE email = ? AND pwd = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setNom(rs.getString("nom"));
                    user.setPrenom(rs.getString("prenom"));
                    user.setEmail(rs.getString("email"));
                    user.setNum_tel(rs.getString("num_tel"));
                    user.setPwd(rs.getString("pwd"));
                    user.setStatus(rs.getString("status"));
                    user.setRole(rs.getString("role"));
                    user.setDate_N(rs.getDate("date_n").toLocalDate()); // Convert java.sql.Date to LocalDate
                    user.setPhoto(rs.getString("photo"));
                    user.setAdress(rs.getString("adress"));
                    return user; // User found, return the user object
                } else {
                    return null; // User not found
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle exception appropriately
                return null;
            } finally {
                try {
                    connection.close(); // Close the connection
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    public User afficher(String userId) {
        DataBase dataBase = new DataBase();
        Connection connection = dataBase.getConnect();

        if (connection != null) {
            String sql = "SELECT * FROM user WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setNom(rs.getString("nom"));
                    user.setPrenom(rs.getString("prenom"));
                    user.setEmail(rs.getString("email"));
                    user.setNum_tel(rs.getString("num_tel"));
                    user.setPwd(rs.getString("pwd"));
                    user.setStatus(rs.getString("status"));
                    user.setRole(rs.getString("role"));
                    user.setDate_N(rs.getDate("date_n").toLocalDate()); // Convert java.sql.Date to LocalDate
                    user.setPhoto(rs.getString("photo"));
                    user.setAdress(rs.getString("adress"));
                    return user; // Return the User object with user details
                } else {
                    return null; // User not found
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle exception appropriately
                return null;
            } finally {
                try {
                    if (connection != null) {
                        connection.close(); // Close the connection
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }
    public boolean updateUser(User user) {
        DataBase dataBase = new DataBase();
        Connection connection = dataBase.getConnect();

        if (connection != null) {
            String sql = "UPDATE user SET nom=?, prenom=?, email=?, num_tel=?, pwd=?, status=?, role=?, date_n=?, photo=?, adress=? WHERE id=?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, user.getNom());
                stmt.setString(2, user.getPrenom());
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getNum_tel());
                stmt.setString(5, user.getPwd());
                stmt.setString(6, user.getStatus());
                stmt.setString(7, user.getRole());
                stmt.setDate(8, Date.valueOf(user.getDate_N())); // Convert LocalDate to java.sql.Date
                stmt.setString(9, user.getPhoto());
                stmt.setString(10, user.getAdress());
                stmt.setString(11, user.getId()); // Assuming id is the primary key

                int rowsUpdated = stmt.executeUpdate();
                return rowsUpdated > 0;
            } catch (SQLException e) {
                e.printStackTrace(); // Handle exception appropriately
                return false;
            } finally {
                try {
                    if (connection != null) {
                        connection.close(); // Close the connection
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return false;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        DataBase dataBase = new DataBase();
        Connection connection = dataBase.getConnect();

        if (connection != null) {
            String sql = "SELECT * FROM user";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setNom(rs.getString("nom"));
                    user.setPrenom(rs.getString("prenom"));
                    user.setEmail(rs.getString("email"));
                    user.setNum_tel(rs.getString("num_tel"));
                    user.setPwd(rs.getString("pwd"));
                    user.setStatus(rs.getString("status"));
                    user.setRole(rs.getString("role"));
                    user.setDate_N(rs.getDate("date_n").toLocalDate()); // Convert java.sql.Date to LocalDate
                    user.setPhoto(rs.getString("photo"));
                    user.setAdress(rs.getString("adress"));
                    userList.add(user); // Add user to the list
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle exception appropriately
            } finally {
                try {
                    if (connection != null) {
                        connection.close(); // Close the connection
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return userList;
    }
    /*public boolean logIn(User user) {
        DataBase dataBase = new DataBase();
        Connection connection = dataBase.getConnect();

        if (connection != null) {
            String sql = "INSERT INTO user (nom, prenom, email, num_tel, pwd, status, role, date_n, photo, adress) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, user.getNom());
                stmt.setString(2, user.getPrenom());
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getNum_tel());
                stmt.setString(5, user.getPwd());
                stmt.setString(6, "inactive");
                stmt.setString(7, user.getRole());
                stmt.setDate(8, Date.valueOf(user.getDate_N())); // Convert LocalDate to java.sql.Date
                stmt.setString(9, user.getPhoto());
                stmt.setString(10, user.getAdress());

                int rowsInserted = stmt.executeUpdate();
                return rowsInserted > 0;
            } catch (SQLException e) {
                e.printStackTrace(); // Handle exception appropriately
                return false;
            } finally {
                try {
                    if (connection != null) {
                        connection.close(); // Close the connection
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return false;
    }*/
    public void activateUser(String userId) {
        DataBase dataBase = new DataBase();
        try (Connection connection = dataBase.getConnect()) {
            String sql = "UPDATE user SET status = 'active' WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, userId);
                stmt.executeUpdate();
                AdminPannelController adminPannelController = new AdminPannelController();
                adminPannelController.showList();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deactivateUser(String userId) {
        DataBase dataBase = new DataBase();
        try (Connection connection = dataBase.getConnect()) {
            String sql = "UPDATE user SET status = 'inactive' WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, userId);
                stmt.executeUpdate();
                AdminPannelController adminPannelController = new AdminPannelController();
                adminPannelController.showList();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

