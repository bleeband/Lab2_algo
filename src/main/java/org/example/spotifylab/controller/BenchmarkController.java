package org.example.spotifylab.controller;

import org.example.spotifylab.algorithmes.AlgorithmeTri;
import org.example.spotifylab.model.Chanson;
import org.example.spotifylab.service.BenchmarkService;
import org.example.spotifylab.service.CritereTri;
import org.example.spotifylab.service.ResultatBenchmark;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.util.List;

/** Contrôleur de la fenêtre de comparaison des tris. */
public final class BenchmarkController {
    @FXML private ComboBox<CritereTri> critereCombo;
    @FXML private TableView<ResultatBenchmark> resultatsTable;
    @FXML private TableColumn<ResultatBenchmark, String> algorithmeCol;
    @FXML private TableColumn<ResultatBenchmark, String> complexiteCol;
    @FXML private TableColumn<ResultatBenchmark, Double> tempsCol;
    private final BenchmarkService service = new BenchmarkService();
    private List<Chanson> chansons = List.of();
    private List<AlgorithmeTri<Chanson>> algorithmes = List.of();

    @FXML private void initialize() {
        algorithmeCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().algorithme()));
        complexiteCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().complexite()));
        tempsCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(
                cell.getValue().millisecondes()).asObject());
        critereCombo.setItems(FXCollections.observableArrayList(CritereTri.values()));
        critereCombo.setValue(CritereTri.TITRE);
    }
    public void initialiser(List<Chanson> chansons, List<AlgorithmeTri<Chanson>> algorithmes) {
        this.chansons = chansons;
        this.algorithmes = algorithmes;
        lancer();
    }
    @FXML private void lancer() {
        if (!chansons.isEmpty()) resultatsTable.setItems(FXCollections.observableArrayList(
                service.mesurer(chansons, algorithmes, critereCombo.getValue())));
    }
}
