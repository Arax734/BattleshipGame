package com.example.battleship.gameFunctionality;

import com.example.battleship.roomConnection.Room;
import com.example.battleship.roomConnection.Server;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class JoinRoom {
    @FXML
    private TextField roomName;
    @FXML
    private Button confirmButton;

    @FXML
    protected void confirm(){
        if(!this.roomName.getText().isEmpty()){
            System.out.println("Room: "+this.roomName.getText());
        }
    }
}
