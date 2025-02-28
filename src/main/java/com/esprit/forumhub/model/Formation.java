package com.esprit.forumhub.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.List;

public class Formation {
    private int id;
    private String titre;
    private String duree;
    private String niveau;
    private String contenu;
    private List<User> utilisateurs;
    private List<Cours> cours;

    // StringProperty for titre
    private final StringProperty titreProperty = new SimpleStringProperty();

    public Formation(int id, String titre, String duree, String niveau, String contenu, List<User> utilisateurs, List<Cours> cours) {
        this.id = id;
        this.titre = titre;
        this.duree = duree;
        this.niveau = niveau;
        this.contenu = contenu;
        this.utilisateurs = utilisateurs;
        this.cours = cours;

        // Bind the titreProperty to the titre field
        this.titreProperty.set(titre);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) {
        this.titre = titre;
        this.titreProperty.set(titre); // Update the property when titre changes
    }

    // Method to return the titreProperty
    public StringProperty titreProperty() {
        return titreProperty;
    }

    public String getDuree() { return duree; }
    public void setDuree(String duree) { this.duree = duree; }

    public String getNiveau() { return niveau; }
    public void setNiveau(String niveau) { this.niveau = niveau; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public List<User> getUtilisateurs() { return utilisateurs; }
    public void setUtilisateurs(List<User> utilisateurs) { this.utilisateurs = utilisateurs; }

    public List<Cours> getCours() { return cours; }
    public void setCours(List<Cours> cours) { this.cours = cours; }

    @Override
    public String toString() {
        return titre; // This will be displayed in the ComboBox
    }
}