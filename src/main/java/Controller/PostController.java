package Controller;

import Entities.Post;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PostController {

    @FXML
    private Label dateC;

    @FXML
    private ImageView imageC;

    @FXML
    private Label titleC;

    @FXML
    private Label viewsC;


    public void setDate(Post post){

        titleC.setText(post.getTitle());

    }



}
