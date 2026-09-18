module org.example.spotifylab {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.postgresql.jdbc;
    opens org.example.spotifylab.controller to javafx.fxml;
    opens org.example.spotifylab.model to javafx.base;
    exports org.example.spotifylab to javafx.graphics; //donne l'accès à MainFx
}
