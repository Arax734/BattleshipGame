package com.example.battleship.gameFunctionality;
import com.example.battleship.roomConnection.Client;
import com.example.battleship.roomConnection.Room;
import com.example.battleship.roomConnection.Server;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
                    this.showError("Room is full!");
                }
                else{
                    Server.getInstance().getRoom(this.roomName.getText()).getClients().add(Server.getInstance().getClient(this.usernameLabel.getText()));
                    Server.getInstance().getClient(this.usernameLabel.getText()).setRoom(Server.getInstance().getRoom(this.roomName.getText()));
                }
            }
            else{
                Server.getInstance().getRooms().put(this.roomName.getText(), new Room(this.roomName.getText()));
                Server.getInstance().getRoom(this.roomName.getText()).getClients().add(Server.getInstance().getClient(this.usernameLabel.getText()));
                Server.getInstance().getClient(this.usernameLabel.getText()).setRoom(Server.getInstance().getRoom(this.roomName.getText()));
            }
        }

        if(Server.getInstance().getRoom(this.roomName.getText()).getClients().size() == 2){
            for(Client client : Server.getInstance().getRoom(this.roomName.getText()).getClients()){
                client.prepareField();
            }
        }
    }
    public Room getRoom(){
        return Server.getInstance().getClient(this.usernameLabel.getText()).getRoom();
    }

    private void showError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}

