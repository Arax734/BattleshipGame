package com.example.battleship.roomConnection;

import com.example.battleship.HelloApplication;
import com.example.battleship.gameFunctionality.Game;
import com.example.battleship.gameFunctionality.JoinRoom;
import com.example.battleship.gameFunctionality.PrepareField;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Thread {
    public Stage recentStage;
    private final String username;
    private Room room;
    private boolean placementDone;
    private int order;
    private Game playerGUI;
    public Client(String username) {
        this.username = username;
        this.room = null;
        this.placementDone = false;
        this.order = -1;
    }
    public String getUsername(){
        return this.username;
    }
    @Override
    public void run() {
        try {
            Socket socket = new Socket("localhost", 59090);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            Platform.runLater(() -> {
                try {
                    Server.getInstance().getAllClients().add(this);
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("join-room.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());

                    JoinRoom joinRoomController = fxmlLoader.getController();
                    joinRoomController.setUsernameLabel(this.username);

                    Stage prepareFieldStage = new Stage();
                    this.recentStage = prepareFieldStage;
                    prepareFieldStage.setTitle("Join Room - "+this.username);
                    prepareFieldStage.setScene(scene);
                    prepareFieldStage.setResizable(false);

                    prepareFieldStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            while (true) {
                String message = in.readLine();
                if (message != null) {
                    System.out.println(message);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void prepareField() {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("prepare-field.fxml"));
        try {
            Scene newScene = new Scene(fxmlLoader.load());

            PrepareField prepareFieldController = fxmlLoader.getController();
            prepareFieldController.setClient(this);

            this.recentStage.setTitle(this.getRoom().getRoomId()+": Prepare field - "+this.getUsername());
            this.recentStage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void startGame(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("game.fxml"));
        try {
            Scene newScene = new Scene(fxmlLoader.load());

            Game gameController = fxmlLoader.getController();
            gameController.setClient(this);
            gameController.loadData();
            this.setPlayerGUI(gameController);

            if(this.getRoom().getClients().get(0).equals(this)){
                this.recentStage.setTitle(this.getRoom().getRoomId()+": Game - "+this.getUsername()+" vs "+this.getRoom().getClients().get(1).getUsername());
            }else{
                this.recentStage.setTitle(this.getRoom().getRoomId()+": Game - "+this.getUsername()+" vs "+this.getRoom().getClients().get(0).getUsername());
            }

            this.recentStage.setScene(newScene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public boolean isPlacementDone() {
        return placementDone;
    }

    public void setPlacementDone(boolean placementDone) {
        this.placementDone = placementDone;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Game getPlayerGUI() {
        return playerGUI;
    }

    public void setPlayerGUI(Game playerGUI) {
        this.playerGUI = playerGUI;
    }
}
