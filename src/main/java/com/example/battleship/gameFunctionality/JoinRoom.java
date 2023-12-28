package com.example.battleship.gameFunctionality;
import com.example.battleship.roomConnection.Client;
import com.example.battleship.roomConnection.Room;
import com.example.battleship.roomConnection.Server;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class JoinRoom implements Initializable {
    @FXML
    private ListView<String> leaderboard;
    @FXML
    private TextField roomName;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button confirmPlacementButton;
    @FXML
    private Label waitingMessage;

    public void setUsernameLabel(String username) {
        usernameLabel.setText(username);
    }

    @FXML
    protected void confirm(){
        if(!this.roomName.getText().isEmpty()){
            if(Server.getInstance().getRoom(this.roomName.getText()) != null){
                if(Server.getInstance().getRoom(this.roomName.getText()).getClients().size() >= 2){
                    this.showError();
                    return;
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
        this.confirmPlacementButton.setDisable(true);
        this.confirmPlacementButton.setOpacity(0);
        this.waitingMessage.setDisable(false);
        this.waitingMessage.setOpacity(1);
    }

    private void showError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Room is full!");
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            String urldb = "jdbc:mysql://localhost";
            String user = "root";
            String password = "";
            Connection connection = DriverManager.getConnection(urldb+"/battleships", user, password);

            String query = "SELECT concat(username, \" \", moves, \" \", time) FROM leaderboard ORDER BY moves,time ASC";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            ObservableList<String> items = FXCollections.observableArrayList();
            while (resultSet.next()) {
                items.add(resultSet.getString("concat(username, \" \", moves, \" \", time)"));
            }
            this.leaderboard.setItems(items);

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

