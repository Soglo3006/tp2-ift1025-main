package com.example.tp2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static com.example.tp2.FlappyEnemy.windowWidth;

//Classe représentant le balle tirer de l'ennemi
public class BalleEnemy {

    //Attributs privés pour x, y, la longueur et la largeur de la balle
    private double x;                               // Position de la balle x
    private final double y;                         // Position de la balle y
    private static final double bulletWidth = 10;   // Largeur de la balle
    private static final double bulletHeight = 4;   // Longueur de la balle

    // Constructeur de la classe BalleEnemy
    public BalleEnemy(double x, double y) {
        this.x = x;
        this.y = y;
    }


    public void update(double time) {
        // speed of bullet per second
        double speed = 400;
        x += speed * time; // move bullet
    }


    public boolean isOffScreen() {
        return x > windowWidth;
    }

    // Méthode pour savoir si la balle touche les héros
    public boolean toucheHeros(Hero hero) {
        // Vérifie l'intersection des hros et la balle sur forme d'une délimitatiojn
        return x < hero.getX() + hero.getWidth() && x + bulletWidth > hero.getX() &&
                y < hero.getY() + hero.getHeight() && y + bulletHeight > hero.getY();
    }


    // Méthode pour contruire le balle en espece
    public void build(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillRect(x, y - 2, bulletWidth, bulletHeight); // Drawing a small rectangle as a bullet
    }
}

 
