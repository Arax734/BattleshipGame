package com.example.battleship.roomConnection;

import com.example.battleship.HelloApplication;
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
    private final String username;

    public Client(String username) {
        this.username = username;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket("localhost", 59090);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            Platform.runLater(() -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("join-room.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());

                    if(Server.getInstance().getRoom("room1") != null){
                        if(Server.getInstance().getRoom("room1").getClients().size() >= 2){
                            System.out.println("Room is full");
                        }
                        else{
                            Server.getInstance().getRoom("room1").getClients().add(this);
                            System.out.println(this.username+" joined room1");
                            System.out.println("Players: "+Server.getInstance().getRoom("room1").getClients().size());
                        }
                    }
                    else{
                        System.out.println("Creating new room");
                        Server.getInstance().getRooms().put("room1", new Room("room1"));
                        Server.getInstance().getRoom("room1").getClients().add(this);
                        System.out.println(this.username+" joined room1");
                        System.out.println("Players: "+Server.getInstance().getRoom("room1").getClients().size());
                    }

                    Stage prepareFieldStage = new Stage();
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
}
