package com.example.tp2;

import javafx.scene.image.Image;

import static com.example.tp2.FlappyEnemy.windowHeight;
import static com.example.tp2.FlappyEnemy.windowWidth;

//Classe représentant les héros dans le jeu
public class Hero extends enemyJeu {
    //
    public enum Type {
        MELEE, STEALTH, TANK
    }

    //Attributs privés pour le type, la postion, la vitesse, l'amplitude et la derniere fois que l'heros c'est teleporter
    private final Type type;        // Type de l'héro
    private double initialX;        // Position X de l'héro
    private double initialY;        // Position Y de l'héro
    private double amplitude;       // Amplitude de l'héro
    private double verticalSpeed;   // La vitesse verticalment de l'héro
    private long lastTeleportTime;  // La derniere fois que l'héro s'est teleporter
    private final double speed;     // Vitesse de l'héro

    //Constructeur de la classe Hero
    public Hero(Image image, double x, double y, double size, Type type) {
        super(image, x, y, size, size);    // Image de l'héro
        this.type = type;
        this.initialX = x;
        this.initialY = y;
        this.speed = -2;                   // Mouvement à gauche de l'hero
        typeHeros();                       // Appel de la méthode pour savoir cestv quoi le type d'hérs qui va apparaitre
        this.lastTeleportTime = System.currentTimeMillis();
    }

    // Méthode pour le type d'héros
    private void typeHeros() {
        switch (type) {
            case STEALTH:
                this.amplitude = 50;        // L'amplitude de base pour le type furtif
                this.verticalSpeed = 0.5;   // la vitesse verticale du type furtif
                break;
            case TANK, MELEE:
                this.amplitude = 0;        // L'amplitude de base pour le type tank et corps à corps
                this.verticalSpeed = 0;    // la vitesse verticale du type tank et corps à corps
                break;
        }
    }

    @Override
    // Méthode qui fait en sorte que tous les héros se deplacent a la vitesse du défilement
    public void update(double elapsedTime) {
        this.x += this.speed * elapsedTime;

        // Comportements des héros dans le jeu
        switch (type) {
            case STEALTH:
                double currentTime = System.currentTimeMillis() * 0.001;
                setY(initialY + amplitude * Math.sin(currentTime * verticalSpeed));
                break;
            case TANK:
                if ((System.currentTimeMillis() - lastTeleportTime) > 500) {
                    randomPosition();
                    lastTeleportTime = System.currentTimeMillis();
                }
                break;
            case MELEE:
                break;
        }

    }

    //Méthode qui modifie la position initiale a la posstion actuelle avant de teleporter
    private void randomPosition() {
        initialX = getX();
        initialY = getY();

        // Choisis aléatoirement la position entre -30 et 30 pixels
        double deltaX = Math.random() * 60 - 30;  // Cela fait pour X
        double deltaY = Math.random() * 60 - 30;  // Cela fait pour Y

        // Cela sert à pour teleporter
        setX(Math.max(0, Math.min(windowWidth - getWidth(), initialX + deltaX)));
        setY(Math.max(0, Math.min(windowHeight - 40 - getHeight(), initialY + deltaY)));

        lastTeleportTime = System.currentTimeMillis();
    }

    //Methode pour avoir le type de l'héro
    public Type getType() {
        return this.type;
    }



}
