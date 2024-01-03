package com.example.battleship.gameFunctionality;
import com.example.battleship.roomConnection.Client;
import com.example.battleship.roomConnection.Server;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;

public class ConnectServer {
    private MediaPlayer mediaPlayer;
    @FXML
    TextField usernameField;
    private void playClickSound() {
        String MP3_FILE_PATH = "/click.wav";
        Media media = new Media(getClass().getResource(MP3_FILE_PATH).toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }
    @FXML
    protected void connectToServer() throws IOException{
        for(Client client : Server.getInstance().getAllClients()){
            if(client.getUsername().equals(usernameField.getText())){
                showError();
                return;
            }
        }
        new Client(this.usernameField.getText()).start();
    }

    private void showError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Username is already taken!");
        alert.showAndWait();
    }
}

