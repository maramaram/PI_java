package Controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MessageBubble extends HBox {
    private static final double MAX_LINE_WIDTH = 250; // Set the maximum width of a line

    public MessageBubble(String message, boolean isUser) {
        setSpacing(10);
        setPadding(new Insets(10));
        setStyle(isUser ? "-fx-background-color: #dcf8c6;" : "-fx-background-color: #d9edf7;");


        Text responseText = new Text(message);
        responseText.setWrappingWidth(MAX_LINE_WIDTH); // Set the wrapping width of the response text
        responseText.setFill(isUser ? Color.BLACK : Color.DODGERBLUE);
        responseText.setFont(Font.font("Arial", 14));

        if (isUser) {
            setAlignment(Pos.CENTER_RIGHT);
            getChildren().addAll(responseText);
        } else {
            setAlignment(Pos.CENTER_LEFT);
            getChildren().addAll(responseText);
        }
    }
}
