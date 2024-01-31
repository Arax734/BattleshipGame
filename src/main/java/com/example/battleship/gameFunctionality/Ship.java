package com.example.battleship.gameFunctionality;

import java.util.ArrayList;

public class Ship {
    private boolean sunk;
    private ArrayList<ShipElement> shipElements;
    public Ship(){
        this.shipElements = new ArrayList<>();
        this.sunk = false;
    }

    public ArrayList<ShipElement> getShipElements() {
        return shipElements;
    }

    public void setShipElements(ArrayList<ShipElement> shipElements) {
        this.shipElements = shipElements;
    }

    public boolean isSunk() {
        return sunk;
    }

    public void setSunk(boolean sunk) {
        this.sunk = sunk;
    }
}
