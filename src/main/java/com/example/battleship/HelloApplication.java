package com.example.battleship;

<<<<<<< HEAD
import com.example.battleship.roomConnection.Client;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
=======
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
>>>>>>> 566737c10a6baa9b2553fdcb49ab4d80fc0378f0
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

<<<<<<< HEAD
=======
import java.io.BufferedReader;
>>>>>>> 566737c10a6baa9b2553fdcb49ab4d80fc0378f0
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

<<<<<<< HEAD
public class HelloApplication extends Application{
    private Button connectButton;
    private TextField usernameField;
    private Stage primaryStage;


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Battleship Client");

        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        connectButton = new Button("Connect to Server");
        connectButton.setOnAction(e -> {
            try {
                String username = usernameField.getText();
                connectToServer(username);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(usernameField, connectButton);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
=======
public class HelloApplication extends Application {

    private BufferedReader in;
    private PrintWriter out;
    private Stage primaryStage;
    private TextArea chatArea;
    private TextField messageField;
    private TextField roomField;
    private Button connectButton;
    private Button joinRoomButton;
    private TextField usernameField;

    private String currentRoom;
    private String clientUsername;
    private Map<String, Integer> roomOccupancy = new HashMap<>();
>>>>>>> 566737c10a6baa9b2553fdcb49ab4d80fc0378f0

    private void connectToServer(String username) throws IOException {
        new Client(username).start();
    }
<<<<<<< HEAD
=======

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        primaryStage.setTitle("Battleship Client");

        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        connectButton = new Button("Connect to Server");
        connectButton.setOnAction(e -> {
            try {
                String username = usernameField.getText();
                connectToServer(username);
                primaryStage.setScene(createChatScene());
                primaryStage.setTitle("Battleship Client - Global Chat");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(usernameField, connectButton);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Scene createChatScene() {
        chatArea = new TextArea();
        chatArea.setEditable(false);

        messageField = new TextField();
        messageField.setPromptText("Type your message here");

        roomField = new TextField();
        roomField.setPromptText("Enter room name");

        joinRoomButton = new Button("Join Room");
        joinRoomButton.setOnAction(e -> joinRoom());

        Button sendMessageButton = new Button("Send Message");
        sendMessageButton.setOnAction(e -> sendMessage());

        // Dodaj nowy przycisk
        Button prepareFieldButton = new Button("Prepare Field");
        prepareFieldButton.setOnAction(e -> prepareField());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(chatArea, messageField, roomField, joinRoomButton, sendMessageButton, prepareFieldButton);

        return new Scene(layout, 300, 400);
    }

    private void connectToServer(String username) throws IOException {
        this.clientUsername = username;

        Socket socket = new Socket("localhost", 59090);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        out.println("USERNAME:" + username);

        new Thread(() -> {
            try {
                while (true) {
                    String line = in.readLine();
                    if (line != null) {
                        Platform.runLater(() -> updateChatArea(line));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void joinRoom() {
        String roomName = roomField.getText();
        out.println("JOIN_ROOM:" + roomName);
        currentRoom = roomName;

        // Increase room occupancy count
        roomOccupancy.putIfAbsent(roomName, 0);
        int occupants = roomOccupancy.get(roomName) + 1;
        roomOccupancy.put(roomName, occupants);

        Platform.runLater(() -> {
            updateChatArea("Joined room: " + roomName);
            updateChatArea("Occupants in room: " + occupants);
            primaryStage.setTitle("Battleship Client - Room: " + roomName);
            joinRoomButton.setDisable(true);
            roomField.setDisable(true);
        });
    }

    private void sendMessage() {
        String message = messageField.getText();
        out.println("MESSAGE:" + currentRoom + ":" + message);
        messageField.clear();
    }

    private void prepareField() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("prepare-field.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            primaryStage.setTitle(this.currentRoom);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateChatArea(String message) {
        if (message.startsWith("Server:")) {
            return;
        }

        if (message.startsWith("Joined room:") || message.startsWith("Occupants in room:")) {
            chatArea.appendText(message + "\n");
        } else if (message.startsWith(clientUsername + ":")) {
            String messageWithoutUsername = message.substring(clientUsername.length() + 1);
            chatArea.appendText("You:" + messageWithoutUsername + "\n");
        } else {
            chatArea.appendText(message + "\n");
        }
    }
>>>>>>> 566737c10a6baa9b2553fdcb49ab4d80fc0378f0
}
