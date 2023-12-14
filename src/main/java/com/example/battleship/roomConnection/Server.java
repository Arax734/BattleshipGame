package com.example.battleship.roomConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private Set<PrintWriter> clients = new HashSet<>();
    private Map<String, Set<PrintWriter>> roomClients = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        new Server().start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(59090)) {
            System.out.println("Server is running...");

            while (true) {
                Socket socket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                clients.add(writer);

                new Thread(new Handler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Handler implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String username;
        private String currentRoom;

        public Handler(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    processInput(input);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                removeClient();
            }
        }

        private void processInput(String input) {
            if (input.startsWith("USERNAME:")) {
                handleUsername(input.substring(9));
            } else if (input.startsWith("JOIN_ROOM:")) {
                handleJoinRoom(input.substring(10));
            } else if (input.startsWith("MESSAGE:")) {
                handleMessage(input.substring(8));
            }
        }

        private void handleUsername(String username) {
            this.username = username;
            clients.add(out);
            broadcast("Server: " + username + " has joined.");
        }

        private void handleJoinRoom(String roomName) {
            if (currentRoom != null) {
                roomClients.get(currentRoom).remove(out);
            }

            currentRoom = roomName;
            roomClients.computeIfAbsent(roomName, k -> new HashSet<>()).add(out);

            broadcast("Server: " + username + " has joined room: " + roomName);
        }

        private void handleMessage(String message) {
            if (currentRoom != null) {
                Set<PrintWriter> roomMembers = roomClients.get(currentRoom);
                for (PrintWriter member : roomMembers) {
                    member.println(username + ": " + message);
                }
            } else {
                broadcast(username + ": " + message);
            }
        }

        private void removeClient() {
            clients.remove(out);
            if (currentRoom != null) {
                roomClients.get(currentRoom).remove(out);
            }
            broadcast("Server: " + username + " has left.");
        }
    }

    private void broadcast(String message) {
        for (PrintWriter client : clients) {
            client.println(message);
        }
    }
}