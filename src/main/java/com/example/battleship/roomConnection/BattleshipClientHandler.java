package com.example.battleship.roomConnection;

import com.example.battleship.roomConnection.Room;
import com.example.battleship.roomConnection.RoomManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BattleshipClientHandler implements Runnable {
    private final Socket clientSocket;
    private final BufferedReader in;
    private final PrintWriter out;
    private Room room;

    public BattleshipClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.room = null;
    }

    @Override
    public void run() {
        try {
            System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

            // Klient wybiera pokój (możesz dostosować to do swoich potrzeb)
            chooseRoom();

            while (true) {
                String message = in.readLine();
                if (message == null) {
                    break;
                }

                // Obsługa otrzymanej wiadomości
                System.out.println("Received from client " + clientSocket.getInetAddress().getHostAddress() + ": " + message);

                // Przesłanie wiadomości do innych klientów w pokoju
                room.getClients().forEach(client -> {
                    if (!client.equals(this)) {
                        client.sendMessage(message);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void chooseRoom() {
        // Klient wybiera pokój (możesz dostosować to do swoich potrzeb)
        sendMessage("Choose a room (e.g., JOIN_ROOM 1): ");
        try {
            String joinRoomMessage = in.readLine();
            String[] parts = joinRoomMessage.split(" ");
            if (parts.length == 2 && parts[0].equals("JOIN_ROOM")) {
                int roomId = Integer.parseInt(parts[1]);
                room = RoomManager.getInstance().getRoom(roomId);
                if (room == null) {
                    room = RoomManager.getInstance().createRoom(roomId);
                }

                if (!room.isFull()) {
                    room.addClient(this);
                    sendMessage("Joined room " + roomId);
                } else {
                    sendMessage("Room is full. Disconnecting...");
                    closeConnection();
                }
            } else {
                sendMessage("Invalid room selection. Disconnecting...");
                closeConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    private void closeConnection() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}