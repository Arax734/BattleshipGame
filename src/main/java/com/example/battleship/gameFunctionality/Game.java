package com.example.battleship.gameFunctionality;

import com.example.battleship.roomConnection.Client;
import com.example.battleship.roomConnection.Room;
import com.example.battleship.roomConnection.Server;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class Game {
    @FXML
    private Button goMenu;
    @FXML
    private Label timerLabel;
    @FXML
    private Label moveCount;
    @FXML
    private Pane myHolder;
    @FXML
    private Pane opponentHolder;
    @FXML
    private Label whoseTurn;
    private boolean[][] myField;
    private ArrayList<Ship> myShips;
    private boolean[][] opponentField;
    private ArrayList<Ship> opponentShips;
    private Client client;
    private Room room;
    private int remainingFields;
    private int moveCounter;
    private MediaPlayer mediaPlayer;
    private void playClickSound() {
        String MP3_FILE_PATH = "/click.wav";
        Media media = new Media(getClass().getResource(MP3_FILE_PATH).toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    private void playPlaceSound(){
        String MP3_FILE_PATH = "/place.wav";
        Media media = new Media(getClass().getResource(MP3_FILE_PATH).toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    private void playExplosionSound(){
        String MP3_FILE_PATH = "/explosion.wav";
        Media media = new Media(getClass().getResource(MP3_FILE_PATH).toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }
    public void loadData() {
        this.room = this.getClient().getRoom();
        this.moveCounter = 0;
        this.moveCount.setText(String.valueOf(this.getMoveCounter()));
        if(this.getRoom().getClientTurn().equals(this.getClient())){
            this.whoseTurn.setText("It's your turn");
        }else{
            this.whoseTurn.setText("It's your opponent's turn");
        }
        this.remainingFields = 20;
        if(this.getClient().getOrder() == 1){
            this.setOpponentField(this.getRoom().getPlayer2());
            this.setOpponentShips(this.getRoom().getPlayer2Ships());
            this.setMyField(this.getRoom().getPlayer1());
            this.setMyShips(this.getRoom().getPlayer1Ships());
        }else{
            this.setOpponentField(this.getRoom().getPlayer1());
            this.setOpponentShips(this.getRoom().getPlayer1Ships());
            this.setMyField(this.getRoom().getPlayer2());
            this.setMyShips(this.getRoom().getPlayer2Ships());
        }
        for(Node button : this.getMyHolder().getChildren()){
            button.setDisable(true);
            String buttonID = button.getId();
            char xChar = buttonID.charAt(buttonID.length() - 1);
            int x = Character.getNumericValue(xChar);
            char yChar = buttonID.charAt(buttonID.length() - 2);
            int y = Character.getNumericValue(yChar);
            String backgroundColorStyle = "-fx-background-color: cyan";
            if(this.getMyField()[y][x]){
                button.setStyle(backgroundColorStyle);
            }
        }
    }

    @FXML
    protected void squareClicked(ActionEvent event) throws SQLException {
        playPlaceSound();
        if(this.getRoom().getClientTurn().equals(this.getClient())){
            if(this.getRoom().timeline.getStatus() != Timeline.Status.RUNNING){
                this.getRoom().startTimer();
            }
            this.setMoveCounter(this.getMoveCounter() + 1);
            this.moveCount.setText(String.valueOf(this.getMoveCounter()));
            Client opponent;
            if(this.getRoom().getClients().get(0).equals(this.getClient())){
                opponent = this.getRoom().getClients().get(1);
            }else{
                opponent = this.getRoom().getClients().getFirst();
            }
            String buttonID = ((Button)(event.getSource())).getId();
            char xChar = buttonID.charAt(buttonID.length() - 1);
            int x = Character.getNumericValue(xChar);
            char yChar = buttonID.charAt(buttonID.length() - 2);
            int y = Character.getNumericValue(yChar);
            String backgroundColorStyle;
            Pane pane = this.getOpponentHolder();
            if(this.getPlacement(x,y,pane)){
                opponent.getPlayerGUI().setRemainingFields(opponent.getPlayerGUI().getRemainingFields() - 1);
                backgroundColorStyle = "-fx-background-color: orange";
                this.getButton(x,y,pane).setStyle(backgroundColorStyle);
                this.getButton(x,y,pane).setDisable(true);

                opponent.getPlayerGUI().getButton(x,y,opponent.getPlayerGUI().getMyHolder()).setStyle(backgroundColorStyle);

                for(Ship ship : this.getOpponentShips()){
                    for(ShipElement shipElement : ship.getShipElements()){
                        if(shipElement.getButtonID().equals(buttonID)){
                            shipElement.setHit(true);
                            if(ship.getShipElements().size() < 2){
                                String buttonShip = shipElement.getButtonID();
                                char xShipString = buttonShip.charAt(buttonShip.length() - 1);
                                int xShip = Character.getNumericValue(xShipString);
                                char yShipString = buttonShip.charAt(buttonShip.length() - 2);
                                int yShip = Character.getNumericValue(yShipString);
                                this.getButton(xShip,yShip,getOpponentHolder()).setStyle("-fx-background-color: red");
                                opponent.getPlayerGUI().getButton(xShip,yShip,opponent.getPlayerGUI().getMyHolder()).setStyle("-fx-background-color: red");
                                ship.setSunk(true);
                                playExplosionSound();
                                opponent.getPlayerGUI().playExplosionSound();
                            }
                        }
                    }
                }

                for(Ship ship : this.getOpponentShips()){
                    if(!ship.isSunk()){
                        boolean finish = false;
                        for(ShipElement shipElement : ship.getShipElements()){
                            if(!shipElement.isHit()){
                                finish = true;
                            }
                        }
                        if(!finish) {
                            for (ShipElement shipElement : ship.getShipElements()) {
                                String buttonShip = shipElement.getButtonID();
                                char xShipString = buttonShip.charAt(buttonShip.length() - 1);
                                int xShip = Character.getNumericValue(xShipString);
                                char yShipString = buttonShip.charAt(buttonShip.length() - 2);
                                int yShip = Character.getNumericValue(yShipString);
                                this.getButton(xShip, yShip, getOpponentHolder()).setStyle("-fx-background-color: red");
                                opponent.getPlayerGUI().getButton(xShip, yShip, opponent.getPlayerGUI().getMyHolder()).setStyle("-fx-background-color: red");
                                ship.setSunk(true);
                                playExplosionSound();
                                opponent.getPlayerGUI().playExplosionSound();
                            }
                        }
                    }
                }

                if(opponent.getPlayerGUI().getRemainingFields() <= 0){
                    this.getRoom().pauseTimer();
                    this.whoseTurn.setText(this.getClient().getUsername()+" wins!");
                    opponent.getPlayerGUI().whoseTurn.setText(this.getClient().getUsername()+" wins!");
                    this.whoseTurn.setStyle("-fx-text-fill: 'lime'");
                    opponent.getPlayerGUI().whoseTurn.setStyle("-fx-text-fill: 'lime'");
                    for(Node button : this.getOpponentHolder().getChildren()){
                        button.setDisable(true);
                    }
                    for(Node button : opponent.getPlayerGUI().getOpponentHolder().getChildren()){
                        button.setDisable(true);
                    }
                    this.goMenu.setDisable(false);
                    this.goMenu.setOpacity(1);

                    String url = "jdbc:mysql://localhost/battleships";
                    String user = "root";
                    String password = "";

                    Connection connection = DriverManager.getConnection(url, user, password);
                    String addDataQuery = "INSERT INTO leaderboard (username, moves, time) VALUES (?, ?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(addDataQuery)) {
                        preparedStatement.setString(1, this.getClient().getUsername());
                        preparedStatement.setInt(2, this.getMoveCounter());
                        preparedStatement.setString(3, this.timerLabel.getText());
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    connection.close();
                    return;
                }
            }else{
                this.getRoom().setClientTurn(opponent);
                this.whoseTurn.setText("It's your opponent's turn");
                opponent.getPlayerGUI().whoseTurn.setText("It's your turn");
                backgroundColorStyle = "-fx-background-color: black";
                this.getButton(x,y,pane).setStyle(backgroundColorStyle);
                this.getButton(x,y,pane).setDisable(true);
                opponent.getPlayerGUI().getButton(x,y,opponent.getPlayerGUI().getMyHolder()).setStyle(backgroundColorStyle);
            }

        }
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Room getRoom() {
        return room;
    }

    public Button getButton(int xTarget, int yTarget, Pane pane){
        for (Node node : pane.getChildren()) {
            if (node instanceof Button button) {
                String buttonID = button.getId();
                char xChar = buttonID.charAt(buttonID.length() - 1);
                int x = Character.getNumericValue(xChar);
                char yChar = buttonID.charAt(buttonID.length() - 2);
                int y = Character.getNumericValue(yChar);
                if(x == xTarget && y == yTarget){
                    return button;
                }
            }
        }
        return null;
    }

    public boolean getPlacement(int x, int y, Pane pane){
        if(this.getButton(x,y,pane) == null){
            return false;
        }
        if(pane.getId().equals("myHolder")){
            return this.getMyField()[y][x];
        }else{
            return this.getOpponentField()[y][x];
        }
    }

    public int getRemainingFields() {
        return remainingFields;
    }

    public void setRemainingFields(int remainingFields) {
        this.remainingFields = remainingFields;
    }

    public Pane getMyHolder() {
        return myHolder;
    }

    public Pane getOpponentHolder() {
        return opponentHolder;
    }

    public boolean[][] getMyField() {
        return myField;
    }

    public void setMyField(boolean[][] myField) {
        this.myField = myField;
    }

    public boolean[][] getOpponentField() {
        return opponentField;
    }

    public void setOpponentField(boolean[][] opponentField) {
        this.opponentField = opponentField;
    }

    public int getMoveCounter() {
        return moveCounter;
    }

    public void setMoveCounter(int moveCounter) {
        this.moveCounter = moveCounter;
    }

    public Label getTimerLabel() {
        return timerLabel;
    }

    @FXML
    protected void leaveGame(){
        playClickSound();
        for(Client client : this.getRoom().getClients()){
            client.endGame();
        }
        this.getRoom().getClients().clear();
        Server.getInstance().getRooms().remove(this.getRoom().getRoomId());
    }


    public ArrayList<Ship> getMyShips() {
        return myShips;
    }

    public void setMyShips(ArrayList<Ship> myShips) {
        this.myShips = myShips;
    }

    public ArrayList<Ship> getOpponentShips() {
        return opponentShips;
    }

    public void setOpponentShips(ArrayList<Ship> opponentShips) {
        this.opponentShips = opponentShips;
    }
}
