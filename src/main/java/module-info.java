module org.example.spotifylab {
    requires javafx.controls;
    requires javafx.fxml;
    opens org.example.spotifylab.controller to javafx.fxml;
    exports org.example.spotifylab to javafx.graphics; //donne l'accès à MainFx
}