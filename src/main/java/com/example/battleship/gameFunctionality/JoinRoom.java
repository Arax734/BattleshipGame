package com.example.battleship.gameFunctionality;
import com.example.battleship.roomConnection.Room;
import com.example.battleship.roomConnection.Server;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class JoinRoom {
    @FXML
    private TextField roomName;
    @FXML
    private Label usernameLabel;

    public void setUsernameLabel(String username) {
        usernameLabel.setText(username);
    }

    @FXML
    protected void confirm(){
        if(!this.roomName.getText().isEmpty()){
            if(Server.getInstance().getRoom(this.roomName.getText()) != null){
                if(Server.getInstance().getRoom(this.roomName.getText()).getClients().size() >= 2){
                    System.out.println("Room is full");
                }
                else{
                    Server.getInstance().getRoom(this.roomName.getText()).getClients().add(Server.getInstance().getClient(this.usernameLabel.getText()));
                    System.out.println(this.usernameLabel.getText()+" joined "+this.roomName.getText());
                    System.out.println("Players: "+Server.getInstance().getRoom(this.roomName.getText()).getClients().size());
                }
            }
            else{
                System.out.println("Creating new room");
                Server.getInstance().getRooms().put(this.roomName.getText(), new Room("room1"));
                Server.getInstance().getRoom(this.roomName.getText()).getClients().add(Server.getInstance().getClient(this.usernameLabel.getText()));
                System.out.println(this.usernameLabel.getText()+" joined "+this.roomName.getText());
                System.out.println("Players: "+Server.getInstance().getRoom(this.roomName.getText()).getClients().size());
            }
        }
    }
}

