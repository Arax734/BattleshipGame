package com.example.battleship.roomConnection;

import java.util.List;
import java.util.ArrayList;
public class Room {
    private Client clientTurn;
    private final String roomId;
    private final List<Client> clients;
    private boolean[][] player1;
    private boolean[][] player2;

    public Room(String roomId) {
        this.roomId = roomId;
        this.clients = new ArrayList<>();
        this.player1 = new boolean[10][10];
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                this.player1[i][j] = false;
            }
        }
        this.player2 = new boolean[10][10];
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                this.player2[i][j] = false;
            }
        }
    }

    public String getRoomId() {
        return roomId;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void addClient(Client client) {
        clients.add(client);
    }

    public boolean isFull() {
        return clients.size() >= 2;
    }

    public boolean[][] getPlayer1() {
        return player1;
    }

    public void setPlayer1(boolean[][] player1) {
        this.player1 = player1;
    }

    public boolean[][] getPlayer2() {
        return player2;
    }

    public void setPlayer2(boolean[][] player2) {
        this.player2 = player2;
    }

    public Client getClientTurn() {
        return clientTurn;
    }

    public void setClientTurn(Client clientTurn) {
        this.clientTurn = clientTurn;
    }
}