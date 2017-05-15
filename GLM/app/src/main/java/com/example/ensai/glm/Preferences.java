package com.example.ensai.glm;

/**
 * Created by ensai on 15/05/17.
 */

public class Preferences {

    private String nom, description;


    public Preferences(String nom, String description) {
        super();
        this.nom = nom;
        this.description = description;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
