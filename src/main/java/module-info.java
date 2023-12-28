module com.example.battleship {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires mysql.connector.j;

    opens com.example.battleship to javafx.fxml;
    exports com.example.battleship;
    exports com.example.battleship.gameFunctionality;
    opens com.example.battleship.gameFunctionality to javafx.fxml;
    exports com.example.battleship.roomConnection;
    opens com.example.battleship.roomConnection to javafx.fxml;
}