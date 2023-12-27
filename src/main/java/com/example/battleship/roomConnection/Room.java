package com.example.battleship.roomConnection;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;

import java.util.List;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
public class Room {
    private Client clientTurn;
    private final String roomId;
    private final List<Client> clients;
    private boolean[][] player1;
    private boolean[][] player2;

    public int elapsedTimeSeconds = 0;
    public int elapsedTimeMinutes = 0;
    public Timeline timeline;

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
        Label timerLabel = new Label("00:00");

        EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateTime();
            }
        };

        this.timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), eventHandler)
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateTime() {
        elapsedTimeSeconds++;
        if (elapsedTimeSeconds == 60) {
            elapsedTimeSeconds = 0;
            elapsedTimeMinutes++;
        }
        String time = String.format("%02d:%02d", elapsedTimeMinutes, elapsedTimeSeconds);
        for(Client client : this.getClients()){
            if(client.getPlayerGUI() != null){
                client.getPlayerGUI().getTimerLabel().setText(time);
            }
        }
    }

    public void startTimer() {
        if (this.timeline != null) {
            this.timeline.play();
        }
    }

    public void pauseTimer() {
        if (this.timeline != null) {
            this.timeline.pause();
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