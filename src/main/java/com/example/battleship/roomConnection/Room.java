package com.example.battleship.roomConnection;

import java.util.List;
import java.util.ArrayList;
public class Room {
    private final String roomId;
    private final List<Client> clients;

    public Room(String roomId) {
        this.roomId = roomId;
        this.clients = new ArrayList<>();
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
}