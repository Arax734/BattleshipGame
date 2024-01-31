package com.example.battleship.roomConnection;

import java.io.Serializable;

public class DataTemplate implements Serializable {
    private Client client;
    private Room room;
    private String description;
    private String value;
    private Server server;
    public DataTemplate(Client client, Room room, String description, String value, Server server){
        this.client = client;
        this.room = room;
        this.description = description;
        this.value = value;
        this.server = server;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
}
