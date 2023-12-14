package com.example.battleship.roomConnection;

import java.util.List;
import java.util.ArrayList;
public class Room {
    private final int roomId;
    private final List<BattleshipClientHandler> clients;

    public Room(int roomId) {
        this.roomId = roomId;
        this.clients = new ArrayList<>();
    }

    public int getRoomId() {
        return roomId;
    }

    public List<BattleshipClientHandler> getClients() {
        return clients;
    }

    public void addClient(BattleshipClientHandler client) {
        clients.add(client);
    }

    public boolean isFull() {
        return clients.size() >= 2;
    }
}