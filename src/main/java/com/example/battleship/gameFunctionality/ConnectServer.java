package com.example.battleship.gameFunctionality;
import com.example.battleship.roomConnection.Client;
import com.example.battleship.roomConnection.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ConnectServer {

    @FXML
    TextField usernameField;

    @FXML
    protected void connectToServer(ActionEvent actionEvent) throws IOException{
        for(Client client : Server.getInstance().getAllClients()){
            if(client.getUsername().equals(usernameField.getText())){
                showError("Username is already taken!");
                return;
            }
        }
        new Client(this.usernameField.getText()).start();
    }

    private void showError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}

