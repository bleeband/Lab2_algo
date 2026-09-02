module org.example.spotifylab {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.spotifylab to javafx.fxml;
    exports org.example.spotifylab;
}