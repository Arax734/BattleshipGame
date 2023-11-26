package com.example.battleship;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class PrepareField {
    private String isSelected = null;
    public Button singleShip;
    public Button doubleShip;
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

    @FXML
    protected void chooseBattleship(ActionEvent event){
        Button clicked = (Button)(event.getSource());
        if(clicked.getId().equals("singleShip") || clicked.getId().equals("doubleShip") ||
                clicked.getId().equals("tripleShip") || clicked.getId().equals("quadrupleShip")){
            if(this.getIsSelected() == null){
                this.setIsSelected(clicked.getId());
            }
            else{
                this.setIsSelected(null);
            }
        }
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }
}
