package com.example.battleship.roomConnection;

<<<<<<< HEAD
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
public class Room {
    private final String roomId;
    private final List<Client> clients;

    public Room(String roomId) {
=======
import java.util.List;
import java.util.ArrayList;
public class Room {
    private final int roomId;
    private final List<BattleshipClientHandler> clients;

    public Room(int roomId) {
>>>>>>> 566737c10a6baa9b2553fdcb49ab4d80fc0378f0
        this.roomId = roomId;
        this.clients = new ArrayList<>();
    }

<<<<<<< HEAD
    public String getRoomId() {
        return roomId;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void addClient(Client client) {
=======
    public int getRoomId() {
        return roomId;
    }

    public List<BattleshipClientHandler> getClients() {
        return clients;
    }

    public void addClient(BattleshipClientHandler client) {
>>>>>>> 566737c10a6baa9b2553fdcb49ab4d80fc0378f0
        clients.add(client);
    }

    public boolean isFull() {
        return clients.size() >= 2;
    }
}