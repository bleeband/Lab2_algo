package org.example.spotifylab.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.util.Duration;
import org.example.spotifylab.model.Chanson;
import org.example.spotifylab.service.LecteurService;
import org.example.spotifylab.util.FormateurDuree;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

final class LecteurController {
    private final Label titre;
    private final Label artiste;
    private final Label tempsEcoule;
    private final Label dureeTotale;
    private final Slider progression;
    private final Supplier<List<Chanson>> fileLecture;
    private final Supplier<Chanson> chansonSelectionnee;
    private final Consumer<Chanson> affichageDetails;
    private final Runnable rafraichirChansons;
    private final LecteurService lecteurService = new LecteurService();
    private Timeline minuterieLecture;

    LecteurController(Label titre,
                      Label artiste,
                      Label tempsEcoule,
                      Label dureeTotale,
                      Slider progression,
                      Button precedent,
                      Button play,
                      Button pause,
                      Button suivant,
                      Button shuffle,
                      Supplier<List<Chanson>> fileLecture,
                      Supplier<Chanson> chansonSelectionnee,
                      Consumer<Chanson> affichageDetails,
                      Runnable rafraichirChansons,
                      Runnable selectionRequise) {
        this.titre = titre;
        this.artiste = artiste;
        this.tempsEcoule = tempsEcoule;
        this.dureeTotale = dureeTotale;
        this.progression = progression;
        this.fileLecture = fileLecture;
        this.chansonSelectionnee = chansonSelectionnee;
        this.affichageDetails = affichageDetails;
        this.rafraichirChansons = rafraichirChansons;

        configurer();
        play.setOnAction(event -> lireOuReprendreSelection(selectionRequise));
        pause.setOnAction(event -> pauseLecture());
        precedent.setOnAction(event -> chansonPrecedente());
        suivant.setOnAction(event -> chansonSuivante());
        shuffle.setOnAction(event -> melangerLecture());
    }

    private void configurer() {
        progression.setMin(0);
        progression.setValue(0);
        tempsEcoule.setText("0:00");
        dureeTotale.setText("0:00");
        minuterieLecture = new Timeline(new KeyFrame(Duration.seconds(1), event -> avancerLecture()));
        minuterieLecture.setCycleCount(Timeline.INDEFINITE);
    }

    private void lireOuReprendreSelection(Runnable selectionRequise) {
        Chanson selection = chansonSelectionnee.get();
        Chanson courante = lecteurService.chansonCourante();

        if (selection != null && selection != courante) {
            Chanson chanson = lecteurService.lire(fileLecture.get(), selection);
            afficherLecture(chanson, true);
            affichageDetails.accept(chanson);
            rafraichirChansons.run();
        } else if (courante != null) {
            afficherLecture(courante, false);
            lecteurService.demarrer();
        } else {
            selectionRequise.run();
            return;
        }

        minuterieLecture.play();
    }

    private void pauseLecture() {
        lecteurService.pause();
        minuterieLecture.pause();
    }

    private void chansonPrecedente() {
        afficherChangementLecture(lecteurService.precedente());
    }

    private void chansonSuivante() {
        afficherChangementLecture(lecteurService.suivante());
    }

    private void melangerLecture() {
        afficherChangementLecture(lecteurService.melanger());
    }

    private void afficherChangementLecture(Chanson chanson) {
        if (chanson != null) {
            afficherLecture(chanson, true);
            affichageDetails.accept(chanson);
            rafraichirChansons.run();
            if (lecteurService.estEnLecture()) {
                minuterieLecture.play();
            }
        }
    }

    private void avancerLecture() {
        Chanson chanson = lecteurService.chansonCourante();
        if (chanson == null || !lecteurService.estEnLecture()) {
            return;
        }

        double prochaineSeconde = progression.getValue() + 1;
        if (prochaineSeconde >= chanson.getDureeSec()) {
            chansonSuivante();
            return;
        }

        progression.setValue(prochaineSeconde);
        tempsEcoule.setText(FormateurDuree.formater((int) prochaineSeconde));
    }

    private void afficherLecture(Chanson chanson, boolean recommencerProgression) {
        titre.setText(chanson.getTitre());
        artiste.setText(chanson.getArtiste());
        progression.setMax(chanson.getDureeSec());
        dureeTotale.setText(FormateurDuree.formater(chanson.getDureeSec()));

        if (recommencerProgression) {
            progression.setValue(0);
            tempsEcoule.setText("0:00");
        }
    }
}
