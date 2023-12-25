package com.example.battleship.gameFunctionality;
import com.example.battleship.roomConnection.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ConnectServer {

    @FXML
    TextField usernameField;

    @FXML
    protected void connectToServer(ActionEvent actionEvent) throws IOException{
        new Client(this.usernameField.getText()).start();
    }
}

