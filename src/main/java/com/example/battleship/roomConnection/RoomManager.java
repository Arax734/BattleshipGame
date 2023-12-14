package com.example.battleship.roomConnection;

import java.util.HashMap;
import java.util.Map;

public class RoomManager {
    private static RoomManager instance;
    private final Map<Integer, Room> rooms;

    private RoomManager() {
        this.rooms = new HashMap<>();
    }

    public static RoomManager getInstance() {
        if (instance == null) {
            instance = new RoomManager();
        }
        return instance;
    }

    public Room getRoom(int roomId) {
        return rooms.get(roomId);
    }

    public Room createRoom(int roomId) {
        Room room = new Room(roomId);
        rooms.put(roomId, room);
        return room;
    }
}