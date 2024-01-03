package com.example.battleship.roomConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.*;

public class Server {

    private static final int PORT = 2137;
    private static Server instance;
    private final Set<PrintWriter> clients = new HashSet<>();
    private final Map<String, Room> rooms = new HashMap<>();
    private final ArrayList<Client> allClients = new ArrayList<Client>();

    public static void main(String[] args) {
        Server.getInstance().start();
    }

    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(59090)) {
            System.out.println("Server is running...");
            createDatabase();
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

    public Room getRoom(String roomId) {
        return rooms.get(roomId);
    }

    public Map<String, Room> getRooms() {
        return this.rooms;
    }

    public ArrayList<Client> getAllClients() {
        return allClients;
    }

    public Client getClient(String targetUsername) {
        for (Client client : this.getAllClients()) {
            if (client.getUsername().equals(targetUsername)) {
                return client;
            }
        }
        return null;
    }

    private static class Handler implements Runnable {
        private BufferedReader in;

        public Handler(Socket socket) {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void createDatabase() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            String url = "jdbc:mysql://localhost";
            String user = "root";
            String password = "";

            Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, user, password);

            if (!databaseExists(connection)) {
                try {
                    createDatabase(connection);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            connection.close();
            connection = DriverManager.getConnection(url+"/battleships", user, password);

            if (!tableExists(connection)){
                createLeaderboard(connection);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        }
    }
    private static boolean databaseExists(Connection connection) throws SQLException {
        String query = "SHOW DATABASES LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "battleships");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
    private static void createDatabase(Connection connection) throws SQLException {
        String createDatabaseQuery = "CREATE DATABASE " + "battleships";

        try (PreparedStatement preparedStatement = connection.prepareStatement(createDatabaseQuery)) {
            preparedStatement.executeUpdate();
        }
    }
    private static boolean tableExists(Connection connection) throws SQLException {
        String query = "SHOW TABLES LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "leaderboard");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return ((ResultSet) resultSet).next();
            }
        }
    }
    private static void createLeaderboard(Connection connection) throws SQLException {
        String createTableQuery = "CREATE TABLE "+ "leaderboard" +" ("
                + "id INT PRIMARY KEY AUTO_INCREMENT,"
                + "username VARCHAR(255),"
                + "moves int(3),"
                + "time varchar(5)"
                + ")";

        try (PreparedStatement preparedStatement = connection.prepareStatement(createTableQuery)) {
            preparedStatement.executeUpdate();
        }
    }
}