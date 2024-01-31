package com.example.battleship.gameFunctionality;

public class ShipElement {
    private String buttonID;
    private boolean isHit;
    public ShipElement(String buttonID){
        this.buttonID = buttonID;
        this.isHit = false;
    }

    public String getButtonID() {
        return buttonID;
    }

    public void setButtonID(String buttonID) {
        this.buttonID = buttonID;
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }
}
