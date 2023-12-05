package com.example.battleship;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PrepareField implements Initializable{
    private boolean[][] battleField;
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

    @FXML
    private Pane gameHolder;


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
    }
    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.R) {
            onRKeyPressed();
        }
        else if(event.getCode() == KeyCode.S){
            this.showPlacement();
        }
    }

    private void onRKeyPressed() {
        if (this.recentHover != null && !this.tempChanged.isEmpty()) {
            Button pattern = this.tempChanged.get(0);
            String buttonID = pattern.getId();
            int x = Character.getNumericValue(buttonID.charAt(buttonID.length() - 1));
            int y = Character.getNumericValue(buttonID.charAt(buttonID.length() - 2));

            Button recent = this.recentHover;
            String recentID = recent.getId();
            int xRecent = Character.getNumericValue(recentID.charAt(recentID.length() - 1));
            int yRecent = Character.getNumericValue(recentID.charAt(recentID.length() - 2));

            String backgroundColorStyleAdd = "-fx-background-color: yellow";

            String backgroundColorStyle = "-fx-background-color: rgb(128,128,128);";
            for (Button button : this.tempChanged) {
                button.setStyle(backgroundColorStyle);
            }
            this.tempChanged.clear();
            if ("doubleShip".equals(this.getSelectedButton().getId())) {
                if (x == xRecent + 1) {
                    this.getButton(xRecent, yRecent + 1).setStyle(backgroundColorStyleAdd);
                    this.tempChanged.add(this.getButton(xRecent, yRecent + 1));
                } else if (y == yRecent + 1) {
                    this.getButton(xRecent - 1, yRecent).setStyle(backgroundColorStyleAdd);
                    this.tempChanged.add(this.getButton(xRecent - 1, yRecent));
                } else if (x == xRecent - 1) {
                    this.getButton(xRecent, yRecent - 1).setStyle(backgroundColorStyleAdd);
                    this.tempChanged.add(this.getButton(xRecent, yRecent - 1));
                } else {
                    this.getButton(xRecent + 1, yRecent).setStyle(backgroundColorStyleAdd);
                    this.tempChanged.add(this.getButton(xRecent + 1, yRecent));
                }
            }
            else if("tripleShip".equals(this.getSelectedButton().getId())){
                if (x == xRecent + 1) {
                    this.getButton(xRecent, yRecent + 1).setStyle(backgroundColorStyleAdd);
                    this.getButton(xRecent, yRecent + 2).setStyle(backgroundColorStyleAdd);
                    this.tempChanged.add(this.getButton(xRecent, yRecent + 1));
                    this.tempChanged.add(this.getButton(xRecent, yRecent + 2));
                } else if (y == yRecent + 1) {
                    this.getButton(xRecent - 1, yRecent).setStyle(backgroundColorStyleAdd);
                    this.getButton(xRecent - 2, yRecent).setStyle(backgroundColorStyleAdd);
                    this.tempChanged.add(this.getButton(xRecent - 1, yRecent));
                    this.tempChanged.add(this.getButton(xRecent - 2, yRecent));
                } else if (x == xRecent - 1) {
                    this.getButton(xRecent, yRecent - 1).setStyle(backgroundColorStyleAdd);
                    this.getButton(xRecent, yRecent - 2).setStyle(backgroundColorStyleAdd);
                    this.tempChanged.add(this.getButton(xRecent, yRecent - 1));
                    this.tempChanged.add(this.getButton(xRecent, yRecent - 2));
                } else {
                    this.getButton(xRecent + 1, yRecent).setStyle(backgroundColorStyleAdd);
                    this.getButton(xRecent + 2, yRecent).setStyle(backgroundColorStyleAdd);
                    this.tempChanged.add(this.getButton(xRecent + 1, yRecent));
                    this.tempChanged.add(this.getButton(xRecent + 2, yRecent));
                }
            }
            else if("quadrupleShip".equals(this.getSelectedButton().getId())){
                if (x == xRecent + 1) {
                    this.getButton(xRecent, yRecent + 1).setStyle(backgroundColorStyleAdd);
                    this.getButton(xRecent, yRecent + 2).setStyle(backgroundColorStyleAdd);
                    this.getButton(xRecent, yRecent + 3).setStyle(backgroundColorStyleAdd);
                    this.tempChanged.add(this.getButton(xRecent, yRecent + 1));
                    this.tempChanged.add(this.getButton(xRecent, yRecent + 2));
                    this.tempChanged.add(this.getButton(xRecent, yRecent + 3));
                } else if (y == yRecent + 1) {
                    this.getButton(xRecent - 1, yRecent).setStyle(backgroundColorStyleAdd);
                    this.getButton(xRecent - 2, yRecent).setStyle(backgroundColorStyleAdd);
                    this.getButton(xRecent - 3, yRecent).setStyle(backgroundColorStyleAdd);
                    this.tempChanged.add(this.getButton(xRecent - 1, yRecent));
                    this.tempChanged.add(this.getButton(xRecent - 2, yRecent));
                    this.tempChanged.add(this.getButton(xRecent - 3, yRecent));
                } else if (x == xRecent - 1) {
                    this.getButton(xRecent, yRecent - 1).setStyle(backgroundColorStyleAdd);
                    this.getButton(xRecent, yRecent - 2).setStyle(backgroundColorStyleAdd);
                    this.getButton(xRecent, yRecent - 3).setStyle(backgroundColorStyleAdd);
                    this.tempChanged.add(this.getButton(xRecent, yRecent - 1));
                    this.tempChanged.add(this.getButton(xRecent, yRecent - 2));
                    this.tempChanged.add(this.getButton(xRecent, yRecent - 3));
                } else {
                    this.getButton(xRecent + 1, yRecent).setStyle(backgroundColorStyleAdd);
                    this.getButton(xRecent + 2, yRecent).setStyle(backgroundColorStyleAdd);
                    this.getButton(xRecent + 3, yRecent).setStyle(backgroundColorStyleAdd);
                    this.tempChanged.add(this.getButton(xRecent + 1, yRecent));
                    this.tempChanged.add(this.getButton(xRecent + 2, yRecent));
                    this.tempChanged.add(this.getButton(xRecent + 3, yRecent));
                }
            }
        }
    }

    @FXML
    protected void squareHover(MouseEvent event) {
        Button hovered = (Button) event.getSource();
        String backgroundColorStyle = "-fx-background-color: lime";
        String backgroundColorStyleAdd = "-fx-background-color: yellow";

        if (this.getSelectedButton() != null) {
            this.recentHover = hovered;
            String buttonID = hovered.getId();
            int x = Character.getNumericValue(buttonID.charAt(buttonID.length() - 1));
            int y = Character.getNumericValue(buttonID.charAt(buttonID.length() - 2));

            hovered.setStyle(backgroundColorStyle);

            if ("doubleShip".equals(this.getSelectedButton().getId()) && this.getButton(x + 1, y) != null) {
                this.getButton(x + 1, y).setStyle(backgroundColorStyleAdd);
                this.tempChanged.add(this.getButton(x + 1, y));
            } else if ("tripleShip".equals(this.getSelectedButton().getId()) && this.getButton(x + 1, y) != null && this.getButton(x + 2, y) != null) {
                this.getButton(x + 1, y).setStyle(backgroundColorStyleAdd);
                this.getButton(x + 2, y).setStyle(backgroundColorStyleAdd);
                this.tempChanged.add(this.getButton(x + 1, y));
                this.tempChanged.add(this.getButton(x + 2, y));
            } else if ("quadrupleShip".equals(this.getSelectedButton().getId()) && this.getButton(x + 1, y) != null && this.getButton(x + 2, y) != null && this.getButton(x + 3, y) != null) {
                this.getButton(x + 1, y).setStyle(backgroundColorStyleAdd);
                this.getButton(x + 2, y).setStyle(backgroundColorStyleAdd);
                this.getButton(x + 3, y).setStyle(backgroundColorStyleAdd);
                this.tempChanged.add(this.getButton(x + 1, y));
                this.tempChanged.add(this.getButton(x + 2, y));
                this.tempChanged.add(this.getButton(x + 3, y));
            }
        } else {
            String backgroundColorStyleGray = "-fx-background-color: lightgray";
            hovered.setStyle(backgroundColorStyleGray);
        }
    }

    @FXML
    protected void squareUnHover(MouseEvent event) {
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
        String buttonID = ((Button)(event.getSource())).getId();
        char xChar = buttonID.charAt(buttonID.length() - 1);
        int x = Character.getNumericValue(xChar);
        char yChar = buttonID.charAt(buttonID.length() - 2);
        int y = Character.getNumericValue(yChar);
        if(this.getSelectedButton() != null) {
            this.setPlacement(x,y, true);
            Button clicked = (Button) (event.getSource());
            this.getSelectedButton().setUserData((int) this.getSelectedButton().getUserData() - 1);
            String backgroundColorStyleAdd = "-fx-background-color: yellow";
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
        for(int x=0; x<this.battleField.length; x++){
            System.out.println();
            for(int y=0; y<this.battleField.length; y++){
                System.out.print(this.battleField[x][y]+" ");
            }
        }
    }
}

