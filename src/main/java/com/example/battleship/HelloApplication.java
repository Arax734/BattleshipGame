package com.example.battleship;

import com.example.battleship.roomConnection.Client;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application{
    private Button connectButton;
    private TextField usernameField;
    private Stage primaryStage;


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Battleship Client");

        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        connectButton = new Button("Connect to Server");
        connectButton.setOnAction(e -> {
            try {
                String username = usernameField.getText();
                connectToServer(username);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(usernameField, connectButton);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void connectToServer(String username) throws IOException {
        new Client(username).start();
    }
}
