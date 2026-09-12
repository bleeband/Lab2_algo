module org.example.spotifylab {
    requires javafx.controls;
    requires javafx.fxml;
    opens org.example.spotifylab.controller to javafx.fxml;
}