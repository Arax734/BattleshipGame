package com.example.battleship.gameFunctionality;

import com.example.battleship.roomConnection.Client;
import com.example.battleship.roomConnection.Room;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class Game {
    @FXML
    private Pane myHolder;
    @FXML
    private Pane opponentHolder;
    @FXML
    private Label whoseTurn;
    private boolean[][] myField;
    private boolean[][] opponentField;
    private Client client;
    private Room room;
    private int remainingFields;

    public void loadData() {
        this.room = this.getClient().getRoom();
        if(this.getRoom().getClientTurn().equals(this.getClient())){
            this.whoseTurn.setText("It's your turn");
        }else{
            this.whoseTurn.setText("It's your opponent's turn");
        }
        this.remainingFields = 10;
        if(this.getClient().getOrder() == 1){
            this.setOpponentField(this.getRoom().getPlayer2());
            this.setMyField(this.getRoom().getPlayer1());
        }else{
            this.setOpponentField(this.getRoom().getPlayer1());
            this.setMyField(this.getRoom().getPlayer2());
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
    protected void squareClicked(ActionEvent event){
        if(this.getRoom().getClientTurn().equals(this.getClient())){
            Client opponent;
            if(this.getRoom().getClients().get(0).equals(this.getClient())){
                opponent = this.getRoom().getClients().get(1);
            }else{
                opponent = this.getRoom().getClients().get(0);
            }
            String buttonID = ((Button)(event.getSource())).getId();
            char xChar = buttonID.charAt(buttonID.length() - 1);
            int x = Character.getNumericValue(xChar);
            char yChar = buttonID.charAt(buttonID.length() - 2);
            int y = Character.getNumericValue(yChar);
            String backgroundColorStyle;
            Pane pane = this.getOpponentHolder();
            if(this.getPlacement(x,y,pane)){
                this.setRemainingFields(this.getRemainingFields() - 1);
                backgroundColorStyle = "-fx-background-color: orange";
                this.getButton(x,y,pane).setStyle(backgroundColorStyle);

                backgroundColorStyle = "-fx-background-color: red";
                opponent.getPlayerGUI().getButton(x,y,opponent.getPlayerGUI().getMyHolder()).setStyle(backgroundColorStyle);
            }else{
                backgroundColorStyle = "-fx-background-color: black";
                this.getButton(x,y,pane).setStyle(backgroundColorStyle);
                this.getButton(x,y,pane).setDisable(true);
                opponent.getPlayerGUI().getButton(x,y,opponent.getPlayerGUI().getMyHolder()).setStyle(backgroundColorStyle);
            }
            this.getRoom().setClientTurn(opponent);
            this.whoseTurn.setText("It's your opponent's turn");
            opponent.getPlayerGUI().whoseTurn.setText("It's your turn");
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

    public void setRoom(Room room) {
        this.room = room;
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
    public void setPlacement(int x, int y, boolean state, Pane pane){
        boolean[][] copiedBattleField;
        if(pane.getId().equals("myHolder")){
            copiedBattleField = this.getMyField();
            copiedBattleField[y][x] = state;
            this.setMyField(copiedBattleField);
        }else{
            copiedBattleField = this.getOpponentField();
            copiedBattleField[y][x] = state;
            this.setOpponentField(copiedBattleField);
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

    public void setMyHolder(Pane myHolder) {
        this.myHolder = myHolder;
    }

    public Pane getOpponentHolder() {
        return opponentHolder;
    }

    public void setOpponentHolder(Pane opponentHolder) {
        this.opponentHolder = opponentHolder;
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
}
