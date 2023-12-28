package com.example.battleship.gameFunctionality;
import com.example.battleship.roomConnection.Client;
import com.example.battleship.roomConnection.Server;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ConnectServer {

    @FXML
    TextField usernameField;

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

