package org.example.spotifylab.service;


import org.example.spotifylab.model.Chanson;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** État et commandes du lecteur musical simulé. */
public final class LecteurService {
    private List<Chanson> file = new ArrayList<>();
    private int index = -1;
    private boolean lecture;

    public void chargerFile(List<Chanson> chansons, Chanson depart) {
        file = new ArrayList<>(chansons);
        index = depart == null ? (file.isEmpty() ? -1 : 0) : file.indexOf(depart);
    }

    public Chanson lire(List<Chanson> chansons, Chanson depart) {
        chargerFile(chansons, depart);
        Chanson chanson = chansonCourante();
        if (chanson != null) {
            chanson.incrementerEcoutes();
            demarrer();
        }
        return chanson;
    }

    public Chanson chansonCourante() { return index >= 0 && index < file.size() ? file.get(index) : null; }
    public void demarrer() { lecture = true; }
    public void pause() { lecture = false; }
    public boolean estEnLecture() { return lecture; }
    public Chanson suivante() {
        if (file.isEmpty()) return null;
        index = (index + 1) % file.size();
        chansonCourante().incrementerEcoutes();
        return chansonCourante();
    }
    public Chanson precedente() {
        if (file.isEmpty()) return null;
        index = (index - 1 + file.size()) % file.size();
        return chansonCourante();
    }
    public Chanson melanger() {
        Chanson courante = chansonCourante();
        Collections.shuffle(file);
        index = courante == null ? (file.isEmpty() ? -1 : 0) : file.indexOf(courante);
        return chansonCourante();
    }
}
