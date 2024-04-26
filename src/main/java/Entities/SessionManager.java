package Entities;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SessionManager {
    private static SessionManager instance;
    private String userId;

    private String userfront ;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserFront(String userfront) {
        this.userfront = userfront;
    }
    public String getUserFront() {
        return userfront;
    }


    public String getUserId()
    {
        return userId;
    }
    public void cleanUserSessionAdmin() {
        userId= " " ;
    }
    public void cleanUserSessionFront() {
        userfront="";
    }
}