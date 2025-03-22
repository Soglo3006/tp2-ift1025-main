package com.example.tp2;
//Importation de javaFX
import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import java.util.Random;

// Classe représentant
public class FlappyEnemy extends Application {
    // Les donneés privées pour la fenetre du jeu et la vitesse et gravité de l'ennemi
    static final int windowWidth = 640;
    static final int windowHeight = 440;

    private double playerSpeed = 1.2/60;
    private double gravity = 500;

    // Les sources d'image en privées
    private Image backgroundImage;
    private Image enemyImage;
    private Image enemyImagelowHP;
    private Image coinImage;
    private Image meleeImage;
    private Image stealthImage;
    private Image tankImage;


    private enemyJeu enemy;
    private List<Piece> pieces;
    private List<Hero> heroes;

    private boolean isPaused = false;
    private Button pauseButton; // Button Pause
    private Label pieceLabel;   //Donnee des pieces obtenu par l'ennemi
    private Label lifeLabel;    //Donnee de la vie de l'ennemi

    private long lastCoinTime = 0;// Temps depuis la dernière pièce générée
    private int nombrePieces = 0;
    private int life = 100;
    private static final long coinInterval = 2000000000; // 2 secondes en nanosecondes
    private double backgroundX = 0;

    private long lastShootTime = 0;
    private final List<BalleEnemy> bullets = new ArrayList<>();
    private static final long nanosperSecond = 1_000_000_000;   // 1 secondes en nanosecondes

    private long lastHeroTime = 0;
    private static final long heroInterval = 3000000000L;       // 3 secondes en nanosecondes
    private final Random random = new Random();

    private AnimationTimer timer;

    //Méthode qui verifie les touches qui font quelque chose dans le jeu
    private void keyHandlers(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                enemy.setVelocityY(-300);
            } else if (event.getCode() == KeyCode.E) {
                long nanotimeNow = System.nanoTime();
                if (nanotimeNow - lastShootTime > nanosperSecond) { // c'est pour le coolown de une secondes
                    bullets.add(new BalleEnemy(enemy.getX() + enemy.getWidth(), enemy.getY() + enemy.getHeight() / 2));
                    lastShootTime = nanotimeNow;
                }
            }
        });
    }


    // Méthode qui cree le jeu
    private void setup(Pane root) {
        HBox menu = new HBox(10);
        menu.setPrefSize(windowWidth, 40);
        menu.setLayoutY(windowHeight-40);

        pieceLabel = new Label("Piece: " + nombrePieces);
        lifeLabel = new Label("Life: " + life + "%");

        // Initialisation du pauseButton
        pauseButton = new Button("Pause");
        pauseButton.setFocusTraversable(false);
        pauseButton.setOnMouseClicked(event -> pauseButton());

        menu.getChildren().addAll(pieceLabel, lifeLabel, pauseButton);
        root.getChildren().add(menu);
    }

    // Méthode qui permet de metrre en pause le jeu
    private void pauseButton() {
        isPaused = !isPaused;
        // Mise à jour du texte du bouton basé sur l'état de pause
        if (isPaused) {
            pauseButton.setText("Resume");
        } else {
            pauseButton.setText("Pause");
        }
    }

    // Méthode qui permet de faire dérouler le jeu
    @Override
    public void start(Stage stage) {
        Pane root = new Pane(); // Création du conteneur principal pour tous les composants de l'UI

        Canvas canvas = new Canvas(windowWidth, windowHeight-40); // Créer un Canvas pour le jeu
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas); // Ajouter le Canvas au conteneur principal

        setup(root); // Initialiser l'interface utilisateur et l'ajouter au root

        imageGame();
        initializeGameEntities(); // Initialiser les entités du jeu

        Scene scene = new Scene(root, windowWidth, windowHeight); // Créer la scène avec le root comme parent
        keyHandlers(scene); // Change les gestionnaires de touches pour la scène

        timer = new AnimationTimer() {
            public void handle(long now) {
                if (!isPaused) {
                    updateGameState();
                    buildGame(gc);
                }
            }
        };
        timer.start();

        stage.setScene(scene);
        stage.setTitle("Flappy Enemy Game");
        stage.show();
    }

    // Méthode qui permet de modifer le nombre de pieces colletes par l'ennemi
    private void updateCoins() {
        nombrePieces += 1;
        pieceLabel.setText("Piece: " + nombrePieces);
        playerSpeed += 0.002;
        gravity += 15;

    }
    //Méthode qui modifie le nombre de pieces que l'ennemia obtenu dans la barre
    private void updatePieceLabel() {
        pieceLabel.setText("Piece: " + nombrePieces);
    }

    // Méthode qui modifie la vie de l'ennemi dans la barre
    private void updateLife() {
        lifeLabel.setText("Life: " + life + "%");
        if (life <= 0) {
            endGame();
        } else if (life <= 50) {
            enemy.setImage(enemyImagelowHP);  // Change l'image si l'ennemi a 50 HP
        } else {
            enemy.setImage(enemyImage);
        }
    }


    // Methode pour finir le jeu
    private void endGame() {

        timer.stop();
        gameoverScreen();

    }

    // Méthode pour avoir acces au image
    private void imageGame() {

        backgroundImage = new Image("file:src/main/java/com/example/tp2/assets/bg.png");
        enemyImage = new Image("file:src/main/java/com/example/tp2/assets/sukuna.png");
        enemyImagelowHP = new Image("file:src/main/java/com/example/tp2/assets/sukuna 50.jpeg");
        coinImage = new Image("file:src/main/java/com/example/tp2/assets/coin.png");
        meleeImage = new Image("file:src/main/java/com/example/tp2/assets/Mario.jpg");
        stealthImage = new Image("file:src/main/java/com/example/tp2/assets/POO.png");
        tankImage = new Image("file:src/main/java/com/example/tp2/assets/Naruto.png");
    }


    // Méthode avec l'initialisation des heros, ennemi et les pieces
    private void initializeGameEntities() {
        enemy = new enemyJeu(enemyImage, (double) windowWidth / 8, (double) windowHeight / 2 - 60, 60, 60);
        pieces = new ArrayList<>();
        heroes = new ArrayList<>();

    }

    // Methode pour avoir un héros par hasard
    private void randomHero() {
        double radius = 10 + random.nextInt(36);
        double x = windowWidth + radius;
        double y = random.nextDouble() * (windowHeight - 40 - 2 * radius) + radius;

        Hero.Type type = Hero.Type.values()[random.nextInt(Hero.Type.values().length)];
        Image heroImage = imageType(type);
        Hero newHero = new Hero(heroImage, x, y, radius * 2, type);
        heroes.add(newHero);
    }


    // Méthode pour donner l'iamge des différents types de héros
    private Image imageType(Hero.Type type) {
        return switch (type) {
            case MELEE -> meleeImage;
            case STEALTH -> stealthImage;
            case TANK -> tankImage;
        };
    }


    // Méthode pour le déroulement du jeu avec les autres méthodes
    private void updateGameState() {
        // Déplacement du background
        backgroundX -= 2;  // Vitesse de défilement du background
        if (backgroundX <= -backgroundImage.getWidth()) {
            backgroundX = 0;
        }

        enemy.setVelocityY(enemy.getVelocityY() + gravity * (0.6 / 60));


        enemy.update(playerSpeed);

        // Tout cela permet d'éviter que l'ennemi sort de l'ecran
        if (enemy.getY() <= 0) {
            enemy.setY(0);
            enemy.setVelocityY(Math.abs(enemy.getVelocityY()) * 0.99);
        }

        // Cela sert a faire que l'ennemi ne sort pas de la barre en bas
        if (enemy.getY() + enemy.getHeight() >= windowHeight - 40) {
            enemy.setY(windowHeight - 40 - enemy.getHeight());
            enemy.setVelocityY(-Math.abs(enemy.getVelocityY()) * 0.99);
        }
        if (System.nanoTime() - lastCoinTime > coinInterval) {
            double coinYPosition = new Random().nextInt(windowHeight - 40 - 30);
            Piece newCoin = new Piece(coinImage, windowWidth, coinYPosition, 30, 30);
            pieces.add(newCoin);
            lastCoinTime = System.nanoTime();
        }

        // Modifie la position de la piece aléatoiremtn dans le jeu
        Iterator<Piece> iterator = pieces.iterator();
        while (iterator.hasNext()) {
            Piece coin = iterator.next();
            coin.setX(coin.getX() - 10);
            if (enemy.touchePiece(coin)) {
                iterator.remove();
                updateCoins();
            } else if (coin.getX() + coin.getWidth() < 0) {
                iterator.remove();
            }
        }
        Iterator<BalleEnemy> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            BalleEnemy bullet = bulletIterator.next();
            bullet.update(0.05);
            if (bullet.isOffScreen()) {
                bulletIterator.remove();
            }
            Iterator<Hero> heroIterator = heroes.iterator();
            while (heroIterator.hasNext()) {
                Hero hero = heroIterator.next();
                if (bullet.toucheHeros(hero)) {
                    addCoins(hero.getType());
                    heroIterator.remove();
                    bulletIterator.remove();
                }
            }

            if (bullet.isOffScreen()) {
                bulletIterator.remove();
            }


    }
        if (System.nanoTime() - lastHeroTime > heroInterval) {
            randomHero();
            lastHeroTime = System.nanoTime();
        }

        Iterator<Hero> heroIterator = heroes.iterator();
        while (heroIterator.hasNext()) {
            Hero hero = heroIterator.next();
            hero.update(2);// Mise à jour avec un temps hypothétique entre les images
            if (enemy.toucheHero(hero)) { // Assuming intersects is a method that checks bounding box collision
                heroIterator.remove();
                switch (hero.getType()) {
                    case MELEE:
                        // For 'corps à corps' type, the game ends directly
                        endGame();
                        break;
                    case STEALTH:
                        if (nombrePieces <= 10){
                            nombrePieces = 0;
                            updatePieceLabel();
                        } else {
                            nombrePieces = nombrePieces - 10;
                            updatePieceLabel();
                        }
                        break;

                    case TANK:
                        // For 'tank' type, enemy loses half of its life
                        life -= 50;
                        updateLife(); // Update life display and check if game should end
                        if (life <= 0) {
                            endGame();
                        }
                        break;

                }
            }

            // Condition générale pour vérifier si un héros est sorti de l'écran
            if (hero.getX() + hero.getWidth() < 0) {
                heroIterator.remove();  // Supprime le héros de la liste si complètement sorti à gauche
            } else if (hero.getType() == Hero.Type.TANK && hero.getX() < -hero.getWidth()) {
                // Condition spéciale pour les tanks si on veut les faire sortir différemment
                // Par exemple, permettre qu'ils sortent complètement même s'ils sont grands
                heroIterator.remove();
            }
        }

    }
    private void addCoins(Hero.Type type) {
        switch (type) {
            case MELEE:
                nombrePieces += 5; // Add 5 coins for Melee type
                break;
            case STEALTH:
                nombrePieces += 8; // Add 8 coins for Stealth type
                break;
            case TANK:
                nombrePieces += 7; // Add 7 coins for Tank type
                break;
        }
        updatePieceLabel(); // Update the UI label
    }


    private void buildGame(GraphicsContext gc) {
        gc.drawImage(backgroundImage, backgroundX, 0);
        gc.drawImage(backgroundImage, backgroundX + backgroundImage.getWidth(), 0);
        enemy.build(gc);
        for (Piece coin : pieces) {
            gc.drawImage(coin.getImage(), coin.getX(), coin.getY(), coin.getWidth(), coin.getHeight());
        }
        for (BalleEnemy bullet : bullets) {
            bullet.build(gc);
        }
        for (Hero hero : heroes) {
            hero.build(gc);
        }

    }

    private void gameoverScreen() {
        Pane gameOverScreen = new Pane();
        Label gameOverLabel = new Label("GAME OVER vous avez fini avec: " + nombrePieces);
        gameOverLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-background-color: rgba(0, 0, 0, 0.8);");
        gameOverLabel.setLayoutX(100);
        gameOverLabel.setLayoutY(240);
        gameOverScreen.getChildren().add(gameOverLabel);
        Stage stage = new Stage();
        stage.setScene(new Scene(gameOverScreen, windowWidth, windowHeight));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
