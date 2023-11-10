package com.example.battleship;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Game {
    @FXML
    private Button square00;
    @FXML
    private Button square01;
    @FXML
    private Button square02;
    @FXML
    private Button square10;
    @FXML
    private Button square11;
    @FXML
    private Button square12;
    @FXML
    private Button square20;
    @FXML
    private Button square21;
    @FXML
    private Button square22;

    @FXML
    protected void squareClicked(ActionEvent event){
        Button clicked = (Button)(event.getSource());
        String backgroundColorStyle = "-fx-background-color: green;";
        clicked.setStyle(backgroundColorStyle);
    }
}
