
package com.example.tp2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

// Classe représentant l'ennemi du jeu
public class enemyJeu {
    // Attributs privés pour l'image, la postion, la largeur, la longuer et la velocité de l'ennemi
    private Image image;            // Image de l'ennemi
    double x;                       // position x de l'ennemi
    private double y;               // position y de l'ennemi
    private final double width;     // Largeur de l'ennemi
    private final double height;    // Longueur de l'ennemi
    private final double velocityX; // La velocité dans la direction X
    private double velocityY;       // La velocité dans la direction Y

    //Constructeur de la classe enemyJeu
    public enemyJeu(Image image, double x, double y, double width, double height) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocityX = 0;
        this.velocityY = 0;

    }

    //Méthode pour savoir si il y a un collision de la piece avec l'ennemi
    public boolean touchePiece(Piece piece) {
        return this.x < piece.getX() + piece.getWidth() &&
                this.x + this.width > piece.getX() &&
                this.y < piece.getY() + piece.getHeight() &&
                this.y + this.height > piece.getY();
    }


    // Méthode pour savoir si l'ennemi touche un des héros
    public boolean toucheHero(Hero hero) {
        return x < hero.getX() + hero.getWidth() &&
               x + width > hero.getX() &&
               y < hero.getY() + hero.getHeight() &&
               y + height > hero.getY();
    }

    // Méthode pour modifier
    public void update(double time) {
        x += velocityX * time;
        y += velocityY * time;
    }
    //Méthode pour afficher l'image de l'ennemi
    public void build(GraphicsContext gc) {
        gc.drawImage(image, x, y, width, height);
    }

    // Getters et setters
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }



    public double getVelocityY() {
        return velocityY;
    }


    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public void setImage(Image newImage) {
            this.image = newImage;
        }
}
