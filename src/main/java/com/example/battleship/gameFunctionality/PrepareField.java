package com.example.battleship.gameFunctionality;

import com.example.battleship.roomConnection.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class PrepareField implements Initializable{
    private boolean[][] battleField;
    @FXML
    private Label waitingMessage;
    @FXML
    private Button confirmPlacementButton;
    public Label singleShipLabel;
    public Label doubleShipLabel;
    public Label tripleShipLabel;
    public Label quadrupleShipLabel;
    private Button selectedButton;
    public Button singleShip;
    public Button doubleShip;
    public Button tripleShip;
    public Button quadrupleShip;

    public ArrayList<Button> tempChanged;

    public Button recentHover;

    public boolean canBePlaced;

    @FXML
    private Pane gameHolder;

    private Client client;
    @FXML
    private Button resetPlacementButton;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.selectedButton = null;
        this.singleShip.setUserData(4);
        this.doubleShip.setUserData(3);
        this.tripleShip.setUserData(2);
        this.quadrupleShip.setUserData(1);
        this.singleShipLabel.setText("4");
        this.doubleShipLabel.setText("3");
        this.tripleShipLabel.setText("2");
        this.quadrupleShipLabel.setText("1");
        this.battleField = new boolean[10][10];
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                this.battleField[i][j] = false;
            }
        }
        this.tempChanged = new ArrayList<Button>();
        this.recentHover = null;
        gameHolder.setOnKeyPressed(this::handleKeyPress);
        gameHolder.requestFocus();
        this.canBePlaced = true;
    }
    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.R) {
            onRKeyPressed();
        }
    }
    @FXML
    protected void confirmPlacement(){
        playClickSound();
        if(!Objects.equals(this.singleShipLabel.getText(), "0") || !Objects.equals(this.doubleShipLabel.getText(), "0")
        || !Objects.equals(this.tripleShipLabel.getText(), "0") || !Objects.equals(this.quadrupleShipLabel.getText(), "0")){
            showError();
            return;
        }
        this.resetPlacementButton.setDisable(true);
        this.waitingMessage.setOpacity(1);
        this.waitingMessage.setDisable(false);
        this.confirmPlacementButton.setOpacity(0);
        this.confirmPlacementButton.setDisable(true);
        boolean[][] firstCheck = this.getClient().getRoom().getPlayer1();
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                if(firstCheck[i][j]){
                    this.getClient().getRoom().setPlayer2(this.getBattleField());
                    this.getClient().setPlacementDone(true);
                    this.getClient().setOrder(2);
                    int playersReady = 0;
                    for(Client client : this.getClient().getRoom().getClients()){
                        if(client.isPlacementDone()){
                            playersReady++;
                        }
                    }
                    if(playersReady == 2){
                        for(Client client : this.getClient().getRoom().getClients()){
                            client.startGame();
                        }
                    }
                    return;
                }
            }
        }
        this.getClient().getRoom().setPlayer1(this.getBattleField());
        this.getClient().getRoom().setClientTurn(this.getClient());
        this.getClient().setPlacementDone(true);
        this.getClient().setOrder(1);
        int playersReady = 0;
        for(Client client : this.getClient().getRoom().getClients()){
            if(client.isPlacementDone()){
                playersReady++;
            }
        }
        if(playersReady == 2){
            for(Client client : this.getClient().getRoom().getClients()){
                client.startGame();
            }
        }
    }

    private void onRKeyPressed() {
        playClickSound();
        this.canBePlaced = true;
        String backgroundColorStyleAdd = "-fx-background-color: rgb(107,255,151)";
        String backgroundColorStyle = "-fx-background-color: rgb(128,128,128);";
        if (this.recentHover != null && !this.tempChanged.isEmpty()) {
            this.recentHover.setStyle(backgroundColorStyleAdd);
            Button pattern = this.tempChanged.getFirst();
            String buttonID = pattern.getId();
            int x = Character.getNumericValue(buttonID.charAt(buttonID.length() - 1));
            int y = Character.getNumericValue(buttonID.charAt(buttonID.length() - 2));

            Button recent = this.recentHover;
            String recentID = recent.getId();
            int xRecent = Character.getNumericValue(recentID.charAt(recentID.length() - 1));
            int yRecent = Character.getNumericValue(recentID.charAt(recentID.length() - 2));
            for (Button button : this.tempChanged) {
                button.setStyle(backgroundColorStyle);
            }
            this.tempChanged.clear();
            if ("doubleShip".equals(this.getSelectedButton().getId())) {
                Button rotated = null;
                while(rotated == null){
                    if( x == xRecent + 1){
                        if(this.getButton(xRecent, yRecent + 1) == null ||
                                this.getPlacement(xRecent, yRecent + 1)){
                            x -= 1;
                            y += 1;
                        }else{
                            rotated = this.getButton(xRecent, yRecent + 1);
                        }
                    }
                    else if (y == yRecent + 1){
                        if(this.getButton(xRecent - 1, yRecent) == null ||
                                this.getPlacement(xRecent - 1, yRecent)){
                            x -= 1;
                            y -= 1;
                        }else{
                            rotated = this.getButton(xRecent - 1, yRecent);
                        }
                    }
                    else if (x == xRecent - 1) {
                        if(this.getButton(xRecent, yRecent - 1) == null ||
                                this.getPlacement(xRecent, yRecent - 1)){
                            x += 1;
                            y -= 1;
                        }else{
                            rotated = this.getButton(xRecent, yRecent - 1);
                        }
                    }
                    else {
                        if(this.getButton(xRecent + 1, yRecent) == null ||
                                this.getPlacement(xRecent + 1, yRecent)){
                            x += 1;
                            y += 1;
                        }else{
                            rotated = this.getButton(xRecent + 1, yRecent);
                        }
                    }
                }
                rotated.setStyle(backgroundColorStyleAdd);
                this.tempChanged.add(rotated);
            }
            else if("tripleShip".equals(this.getSelectedButton().getId())){
                Button rotated = null;
                Button rotated2 = null;
                while(rotated == null || rotated2 == null){
                    if( x == xRecent + 1){
                        if(this.getButton(xRecent, yRecent + 1) == null ||
                                this.getButton(xRecent, yRecent + 2) == null ||
                                this.getPlacement(xRecent, yRecent + 1) ||
                                this.getPlacement(xRecent, yRecent + 2)){
                            x -= 1;
                            y += 1;
                        }else{
                            rotated = this.getButton(xRecent, yRecent + 1);
                            rotated2 = this.getButton(xRecent, yRecent + 2);
                        }
                    }
                    else if (y == yRecent + 1){
                        if(this.getButton(xRecent - 1, yRecent) == null ||
                                this.getButton(xRecent - 2, yRecent) == null ||
                                this.getPlacement(xRecent - 1, yRecent) ||
                                this.getPlacement(xRecent - 2, yRecent)){
                            x -= 1;
                            y -= 1;
                        }else{
                            rotated = this.getButton(xRecent - 1, yRecent);
                            rotated2 = this.getButton(xRecent - 2, yRecent);
                        }
                    }
                    else if (x == xRecent - 1) {
                        if(this.getButton(xRecent, yRecent - 1) == null ||
                                this.getButton(xRecent, yRecent - 2) == null ||
                                this.getPlacement(xRecent, yRecent - 1) ||
                                this.getPlacement(xRecent, yRecent - 2)){
                            x += 1;
                            y -= 1;
                        }else{
                            rotated = this.getButton(xRecent, yRecent - 1);
                            rotated2 = this.getButton(xRecent, yRecent - 2);
                        }
                    }
                    else {
                        if(this.getButton(xRecent + 1, yRecent) == null ||
                                this.getButton(xRecent + 2, yRecent) == null ||
                                this.getPlacement(xRecent + 1, yRecent) ||
                                this.getPlacement(xRecent + 2, yRecent)){
                            x += 1;
                            y += 1;
                        }else{
                            rotated = this.getButton(xRecent + 1, yRecent);
                            rotated2 = this.getButton(xRecent + 2, yRecent);
                        }
                    }
                }
                rotated.setStyle(backgroundColorStyleAdd);
                rotated2.setStyle(backgroundColorStyleAdd);
                this.tempChanged.add(rotated);
                this.tempChanged.add(rotated2);
            }
            else if("quadrupleShip".equals(this.getSelectedButton().getId())){
                Button rotated = null;
                Button rotated2 = null;
                Button rotated3 = null;
                while(rotated == null || rotated2 == null || rotated3 == null){
                    if( x == xRecent + 1){
                        if(this.getButton(xRecent, yRecent + 1) == null ||
                                this.getButton(xRecent, yRecent + 2) == null ||
                                this.getButton(xRecent, yRecent + 3) == null ||
                                this.getPlacement(xRecent, yRecent + 1) ||
                                this.getPlacement(xRecent, yRecent + 2) ||
                                this.getPlacement(xRecent, yRecent + 3)){
                            x -= 1;
                            y += 1;
                        }else{
                            rotated = this.getButton(xRecent, yRecent + 1);
                            rotated2 = this.getButton(xRecent, yRecent + 2);
                            rotated3 = this.getButton(xRecent, yRecent + 3);
                        }
                    }
                    else if (y == yRecent + 1){
                        if(this.getButton(xRecent - 1, yRecent) == null ||
                                this.getButton(xRecent - 2, yRecent) == null ||
                                this.getButton(xRecent - 3, yRecent) == null ||
                                this.getPlacement(xRecent - 1, yRecent) ||
                                this.getPlacement(xRecent - 2, yRecent) ||
                                this.getPlacement(xRecent - 3, yRecent)){
                            x -= 1;
                            y -= 1;
                        }else{
                            rotated = this.getButton(xRecent - 1, yRecent);
                            rotated2 = this.getButton(xRecent - 2, yRecent);
                            rotated3 = this.getButton(xRecent - 3, yRecent);
                        }
                    }
                    else if (x == xRecent - 1) {
                        if(this.getButton(xRecent, yRecent - 1) == null ||
                                this.getButton(xRecent, yRecent - 2) == null ||
                                this.getButton(xRecent, yRecent - 3) == null ||
                                this.getPlacement(xRecent, yRecent - 1) ||
                                this.getPlacement(xRecent, yRecent - 2) ||
                                this.getPlacement(xRecent, yRecent - 3)){
                            x += 1;
                            y -= 1;
                        }else{
                            rotated = this.getButton(xRecent, yRecent - 1);
                            rotated2 = this.getButton(xRecent, yRecent - 2);
                            rotated3 = this.getButton(xRecent, yRecent - 3);
                        }
                    }
                    else {
                        if(this.getButton(xRecent + 1, yRecent) == null ||
                                this.getButton(xRecent + 2, yRecent) == null ||
                                this.getButton(xRecent + 3, yRecent) == null ||
                                this.getPlacement(xRecent + 1, yRecent) ||
                                this.getPlacement(xRecent + 2, yRecent) ||
                                this.getPlacement(xRecent + 3, yRecent)){
                            x += 1;
                            y += 1;
                        }else{
                            rotated = this.getButton(xRecent + 1, yRecent);
                            rotated2 = this.getButton(xRecent + 2, yRecent);
                            rotated3 = this.getButton(xRecent + 3, yRecent);
                        }
                    }
                }
                rotated.setStyle(backgroundColorStyleAdd);
                rotated2.setStyle(backgroundColorStyleAdd);
                rotated3.setStyle(backgroundColorStyleAdd);
                this.tempChanged.add(rotated);
                this.tempChanged.add(rotated2);
                this.tempChanged.add(rotated3);
            }
            this.checkAround(this.recentHover);
        }
    }

    @FXML
    protected void squareHover(MouseEvent event) {
        this.canBePlaced = true;
        Button hovered = (Button) event.getSource();
        String backgroundColorStyle = "-fx-background-color: rgb(107,255,151)";

        if (this.getSelectedButton() != null) {
            this.recentHover = hovered;
            String buttonID = hovered.getId();
            int x = Character.getNumericValue(buttonID.charAt(buttonID.length() - 1));
            int y = Character.getNumericValue(buttonID.charAt(buttonID.length() - 2));

            hovered.setStyle(backgroundColorStyle);

            if ("doubleShip".equals(this.getSelectedButton().getId())) {
                if(this.getButton(x+1, y) == null || this.getPlacement(x + 1, y)){
                    if(this.getButton(x, y+1) == null || this.getPlacement(x, y+1)){
                        if(this.getButton(x-1, y) == null || this.getPlacement(x-1,y)){
                            this.getButton(x, y-1).setStyle(backgroundColorStyle);
                            this.tempChanged.add(this.getButton(x, y-1));
                        }else{
                            this.getButton(x-1, y).setStyle(backgroundColorStyle);
                            this.tempChanged.add(this.getButton(x-1, y));
                        }
                    }else{
                        this.getButton(x, y+1).setStyle(backgroundColorStyle);
                        this.tempChanged.add(this.getButton(x, y+1));
                    }
                }else{
                    this.getButton(x + 1, y).setStyle(backgroundColorStyle);
                    this.tempChanged.add(this.getButton(x + 1, y));
                }
            } else if ("tripleShip".equals(this.getSelectedButton().getId())) {
                if(this.getButton(x+1, y) == null || this.getButton(x+2, y) == null ||
                        this.getPlacement(x + 1, y) || this.getPlacement(x + 2, y)){
                    if(this.getButton(x, y+1) == null || this.getButton(x, y+2) == null ||
                            this.getPlacement(x, y+1) || this.getPlacement(x, y+2)){
                        if(this.getButton(x-1, y) == null || this.getButton(x-2, y) == null ||
                                this.getPlacement(x-1, y) || this.getPlacement(x-2, y)){
                            this.canBePlaced = false;
                            String basicStyle = "-fx-background-color: lightgray;";
                            hovered.setStyle(basicStyle);
                            return;
                        }else{
                            this.getButton(x-1, y).setStyle(backgroundColorStyle);
                            this.getButton(x-2, y).setStyle(backgroundColorStyle);
                            this.tempChanged.add(this.getButton(x-1, y));
                            this.tempChanged.add(this.getButton(x-2, y));
                        }
                    }else{
                        this.getButton(x, y+1).setStyle(backgroundColorStyle);
                        this.getButton(x, y+2).setStyle(backgroundColorStyle);
                        this.tempChanged.add(this.getButton(x, y+1));
                        this.tempChanged.add(this.getButton(x, y+2));
                    }
                }else{
                    this.getButton(x + 1, y).setStyle(backgroundColorStyle);
                    this.getButton(x + 2, y).setStyle(backgroundColorStyle);
                    this.tempChanged.add(this.getButton(x + 1, y));
                    this.tempChanged.add(this.getButton(x + 2, y));
                }
            } else if ("quadrupleShip".equals(this.getSelectedButton().getId())) {
                if(this.getButton(x+1, y) == null || this.getButton(x+2, y) == null ||
                this.getButton(x+3, y) == null || this.getPlacement(x + 1, y) ||
                        this.getPlacement(x + 2, y) || this.getPlacement(x + 3, y)){
                    if(this.getButton(x, y+1) == null || this.getButton(x, y+2) == null ||
                    this.getButton(x, y+3) == null || this.getPlacement(x, y+1) ||
                            this.getPlacement(x, y+2) || this.getPlacement(x, y+3)){
                        if(this.getButton(x-1, y) == null || this.getButton(x-2, y) == null ||
                                this.getButton(x-3, y) == null || this.getPlacement(x-1, y) ||
                                this.getPlacement(x-2, y) || this.getPlacement(x-3, y)){
                                this.canBePlaced = false;
                                String basicStyle = "-fx-background-color: lightgray;";
                                hovered.setStyle(basicStyle);
                                return;

                        }else{
                            this.getButton(x - 1, y).setStyle(backgroundColorStyle);
                            this.getButton(x - 2, y).setStyle(backgroundColorStyle);
                            this.getButton(x - 3, y).setStyle(backgroundColorStyle);
                            this.tempChanged.add(this.getButton(x - 1, y));
                            this.tempChanged.add(this.getButton(x - 2, y));
                            this.tempChanged.add(this.getButton(x - 3, y));
                        }
                    }else{
                        this.getButton(x, y+1).setStyle(backgroundColorStyle);
                        this.getButton(x, y+2).setStyle(backgroundColorStyle);
                        this.getButton(x, y+3).setStyle(backgroundColorStyle);
                        this.tempChanged.add(this.getButton(x, y+1));
                        this.tempChanged.add(this.getButton(x, y+2));
                        this.tempChanged.add(this.getButton(x, y+3));
                    }
                }else{
                    this.getButton(x + 1, y).setStyle(backgroundColorStyle);
                    this.getButton(x + 2, y).setStyle(backgroundColorStyle);
                    this.getButton(x + 3, y).setStyle(backgroundColorStyle);
                    this.tempChanged.add(this.getButton(x + 1, y));
                    this.tempChanged.add(this.getButton(x + 2, y));
                    this.tempChanged.add(this.getButton(x + 3, y));
                }
            }
        } else {
            String backgroundColorStyleGray = "-fx-background-color: lightgray";
            hovered.setStyle(backgroundColorStyleGray);
        }
        this.checkAround(hovered);
    }

    @FXML
    protected void squareUnHover(MouseEvent event) {
        this.canBePlaced = true;
        this.recentHover = null;
        Button hovered = (Button) (event.getSource());
        String backgroundColorStyle = "-fx-background-color: rgb(128,128,128);";
        hovered.setStyle(backgroundColorStyle);
        for (Button button : this.tempChanged) {
            button.setStyle(backgroundColorStyle);
        }
        this.tempChanged.clear();
    }
    @FXML
    protected void squareClicked(ActionEvent event){
        playPlaceSound();
        String buttonID = ((Button)(event.getSource())).getId();
        char xChar = buttonID.charAt(buttonID.length() - 1);
        int x = Character.getNumericValue(xChar);
        char yChar = buttonID.charAt(buttonID.length() - 2);
        int y = Character.getNumericValue(yChar);
        if(this.getSelectedButton() != null && this.canBePlaced) {
            this.setPlacement(x,y, true);
            Button clicked = (Button) (event.getSource());
            this.getSelectedButton().setUserData((int) this.getSelectedButton().getUserData() - 1);
            String backgroundColorStyle = "-fx-background-color: cyan";
            clicked.setStyle(backgroundColorStyle);
            clicked.setOnMouseExited(null);
            clicked.setOnMouseEntered(null);
            clicked.setOnAction(null);
            if(this.getSelectedButton().getId().equals("singleShip")){
                this.singleShipLabel.setText(Integer.toString((int)this.getSelectedButton().getUserData()));
            }
            else if(this.getSelectedButton().getId().equals("doubleShip")){
                this.doubleShipLabel.setText(Integer.toString((int)this.getSelectedButton().getUserData()));
            }
            else if(this.getSelectedButton().getId().equals("tripleShip")){
                this.tripleShipLabel.setText(Integer.toString((int)this.getSelectedButton().getUserData()));
            }
            else if(this.getSelectedButton().getId().equals("quadrupleShip")){
                this.quadrupleShipLabel.setText(Integer.toString((int)this.getSelectedButton().getUserData()));
            }
            if ((int) this.getSelectedButton().getUserData() <= 0) {
                String backgroundColorStyleInactive = "-fx-background-color: red";
                this.getSelectedButton().setStyle(backgroundColorStyleInactive);
                this.getSelectedButton().setDisable(true);
            }else{
                String backgroundColorStyleUnclicked = "-fx-background-color: rgb(128,128,128);";
                this.getSelectedButton().setStyle(backgroundColorStyleUnclicked);
            }
            for (Button button : this.tempChanged) {
                button.setStyle(backgroundColorStyle);
                button.setOnMouseExited(null);
                button.setOnMouseEntered(null);
                button.setOnAction(null);
                String recentbuttonID = button.getId();
                char recentxChar = recentbuttonID.charAt(recentbuttonID.length() - 1);
                int recentx = Character.getNumericValue(recentxChar);
                char recentyChar = recentbuttonID.charAt(recentbuttonID.length() - 2);
                int recenty = Character.getNumericValue(recentyChar);
                this.setPlacement(recentx,recenty, true);
            }
            this.tempChanged.clear();
            this.setSelectedButton(null);
        }
    }

    @FXML
    protected void chooseBattleship(ActionEvent event){
        playClickSound();
        Button clicked = (Button)(event.getSource());
        if(clicked.getId().equals("singleShip") || clicked.getId().equals("doubleShip") ||
                clicked.getId().equals("tripleShip") || clicked.getId().equals("quadrupleShip")){
            if(this.getSelectedButton() == null && (int) clicked.getUserData() > 0){
                this.setSelectedButton(clicked);
                String backgroundColorStyle = "-fx-background-color: cyan;";
                this.getSelectedButton().setStyle(backgroundColorStyle);
            }
            else{
                String backgroundColorStyle = "-fx-background-color: rgb(128,128,128);";
                this.getSelectedButton().setStyle(backgroundColorStyle);
                this.setSelectedButton(null);
            }
        }
    }
    @FXML
    protected void resetPlacement(){
        playClickSound();
        this.selectedButton = null;
        this.singleShip.setUserData(4);
        this.doubleShip.setUserData(3);
        this.tripleShip.setUserData(2);
        this.quadrupleShip.setUserData(1);
        this.singleShipLabel.setText("4");
        this.doubleShipLabel.setText("3");
        this.tripleShipLabel.setText("2");
        this.quadrupleShipLabel.setText("1");
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                this.battleField[i][j] = false;
            }
        }
        this.tempChanged.clear();
        this.recentHover = null;
        this.canBePlaced = true;
        for(Node button : this.gameHolder.getChildren()){
            if (button instanceof Button currentButton) {
                currentButton.setDisable(false);
                String backgroundColorStyle = "-fx-background-color: rgb(128,128,128);";
                currentButton.setStyle(backgroundColorStyle);
                this.singleShip.setDisable(false);
                this.singleShip.setStyle(backgroundColorStyle);
                this.doubleShip.setDisable(false);
                this.doubleShip.setStyle(backgroundColorStyle);
                this.tripleShip.setDisable(false);
                this.tripleShip.setStyle(backgroundColorStyle);
                this.quadrupleShip.setDisable(false);
                this.quadrupleShip.setStyle(backgroundColorStyle);
                currentButton.setOnMouseExited(this::squareUnHover);
                currentButton.setOnMouseEntered(this::squareHover);
                currentButton.setOnAction(this::squareClicked);
            }
        }
    }
    public Button getSelectedButton() {
        return selectedButton;
    }

    public void setSelectedButton(Button selectedButton) {
        this.selectedButton = selectedButton;
    }

    public boolean[][] getBattleField() {
        return battleField;
    }
    public void setBattleField(boolean[][] battleField){
        this.battleField = battleField;
    }
    public boolean getPlacement(int x, int y){
        if(this.getButton(x,y) == null){
            return false;
        }
        return this.getBattleField()[y][x];
    }
    public void setPlacement(int x, int y, boolean state){
        boolean[][] copiedBattleField = this.getBattleField();
        copiedBattleField[y][x] = state;
        this.setBattleField(copiedBattleField);
    }

    public Button getButton(int xTarget, int yTarget){
        for (Node node : this.gameHolder.getChildren()) {
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
    public void showPlacement(){
        for (boolean[] booleans : this.battleField) {
            System.out.println();
            for (int y = 0; y < this.battleField.length; y++) {
                System.out.print(booleans[y] + " ");
            }
        }
    }
    public void checkAround(Button mainButton){
        if(this.selectedButton == null){
            String basicStyle = "-fx-background-color: lightgray;";
            mainButton.setStyle(basicStyle);
            return;
        }
        String backgroundColorStyle = "-fx-background-color: rgb(232,95,95);";
        String mainID = mainButton.getId();
        int xMain = Character.getNumericValue(mainID.charAt(mainID.length() - 1));
        int yMain = Character.getNumericValue(mainID.charAt(mainID.length() - 2));
        if(this.getPlacement(xMain+1, yMain) || this.getPlacement(xMain+1, yMain+1) ||
                this.getPlacement(xMain, yMain+1) || this.getPlacement(xMain-1, yMain+1) ||
                this.getPlacement(xMain-1, yMain) || this.getPlacement(xMain-1, yMain-1) ||
                this.getPlacement(xMain, yMain-1) || this.getPlacement(xMain+1, yMain-1)){
            mainButton.setStyle(backgroundColorStyle);
            if(!this.tempChanged.isEmpty()){
                for(Button ship : this.tempChanged){
                    ship.setStyle(backgroundColorStyle);
                }
            }
            this.canBePlaced = false;
            return;
        }
        for(Button shipPart : this.tempChanged){
            String shipPartID = shipPart.getId();
            int x = Character.getNumericValue(shipPartID.charAt(shipPartID.length() - 1));
            int y = Character.getNumericValue(shipPartID.charAt(shipPartID.length() - 2));
            if(this.getPlacement(x+1, y) || this.getPlacement(x+1, y+1) ||
                    this.getPlacement(x, y+1) || this.getPlacement(x-1, y+1) ||
                    this.getPlacement(x-1, y) || this.getPlacement(x-1, y-1) ||
                    this.getPlacement(x, y-1) || this.getPlacement(x+1, y-1)){
                mainButton.setStyle(backgroundColorStyle);
                for(Button ship : this.tempChanged){
                    ship.setStyle(backgroundColorStyle);
                }
                this.canBePlaced = false;
                return;
            }
        }
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    private void showError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Put every battleship on the board!");
        alert.showAndWait();
    }
}

