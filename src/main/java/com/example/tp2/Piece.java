package com.example.tp2;

import javafx.scene.image.Image;

// Classe représentant les pieces dans le jeu
public class Piece {
    // Attributs privés pour l'image, x, y, la longueru et la largeur donnée pour les pieces
    private final Image image;     // Image actuelle de la piece
    private double x;              // Postion de l'image par rapport a x
    private final double y;        // Postion de l'image par rapport a y
    private final double width;    // Largeur de la piece
    private final double height;   // Largeur de la piece

    // Constructeur de la classe Piece
    public Piece(Image image, double x, double y, double width, double height) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Getters et Setters
    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Image getImage() {
        return image;
    }
}


