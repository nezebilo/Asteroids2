package com.example.asteroids2;


import com.example.asteroids2.ConstantVar.Level;
import com.example.asteroids2.Flyingobject.*;
import com.example.asteroids2.MenuUi.LayoutElement;
import com.example.asteroids2.MenuUi.GamePane;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;

import static com.example.asteroids2.ConstantVar.Level.*;
import static com.example.asteroids2.ConstantVar.Size.*;
import static com.example.asteroids2.ConstantVar.SpeedRate.*;
import static com.example.asteroids2.MenuUi.LayoutElement.*;


public class Main extends Application {
    protected Pane mainGamePane;
    protected Scene mainGameScene;
    protected Scene mainMenuScene;
    private Pane pauseMenuPane;
    private StackPane mainMenuContainer;
    private StackPane pauseMenuContainer;
    protected double startTime;
    protected int currentPoints;
    protected int lastPoints;
    private String[][] scores;


    protected Ship ship;
    protected Map<KeyCode, Boolean> pressedKeys = new HashMap<>();
    protected List<Asteroid> asteroids = new ArrayList<>();
    protected List<Alien> aliens = new ArrayList<>();
    protected List<Projectile> projectiles = new ArrayList<>();
    protected List<Projectile> alienProjectiles = new ArrayList<>();


    //record the last firing time, to control firing frequency
    protected double lastFireTime;
    protected double lastDestroyedTime;
    protected double alienLastFireTime;
    protected double lastThrustTime;

    public static int WIDTH = 600;
    public static int HEIGHT = 600;
    protected static final int SHIP_FIRE_INTERVAL = 500; // ms
    protected static final int ALIEN_FIRE_INTERVAL = 5000; // ms
    protected static final int SHIP_THRUST_INTERVAL = 3000; // ms
    protected static final int SHIP_INVINCIBLE_TIME = 3000; // ms
    protected static final int SHIP_PROJECTILE_EXIST_TIME = 3000;// ms
    protected static final int ALIEN_PROJECTILE_EXIST_TIME = 1000;// ms
    protected static final int ALIEN_EXISTING_MAXIMUM = 3;
    protected static final int SHIP_PROJECTILE_EXIST_MAXIMUM = 5;
    protected static final double CREATED_PROBABILITY = 0.001;

    protected Random random = new Random();
    private boolean isPaused = false;
    private Label infoLabel;
    // Load the custom font
    static Font customFont;

    // set the font
    static {
        try {
            customFont = Font.loadFont(new FileInputStream
                    ("src/main/resources/imageAndFont/Roboto-BoldItalic.ttf"), 18);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // Load background music
    String musicFile = "src/main/resources/sfx/Enigma-Long-Version-Complete-Version.mp3";
    Media sound = new Media(new File(musicFile).toURI().toString());
    MediaPlayer music = new MediaPlayer(sound);

    // Load fire sound effect
    String fireSFXFile = "src/main/resources/sfx/mixkit-laser-gun-shot-3110.wav";
    Media fireSFXSound = new Media(new File(fireSFXFile).toURI().toString());
    MediaPlayer fireSFX = new MediaPlayer(fireSFXSound);

    // Load explosion sound effect
    String explodeSFXFile = "src/main/resources/sfx/mixkit-arcade-chiptune-explosion-1691.wav";
    Media explodeSFXSound = new Media(new File(explodeSFXFile).toURI().toString());
    MediaPlayer explodeSFX = new MediaPlayer(explodeSFXSound);
    // Load jump sound effect
    String jumpSFXFile = "src/main/resources/sfx/mixkit-space-deploy-whizz-3003.wav";
    Media jumpSFXSound = new Media(new File(jumpSFXFile).toURI().toString());
    MediaPlayer jumpSFX = new MediaPlayer(jumpSFXSound);


    public Main() {
    }

    @Override
    public void start(Stage primaryStage) {
        mainMenuScene(primaryStage);
    }

    //  main menu mainGameScene / game mainGameScene/ high score mainGamePane/ pause mainGamePane/game over mainGamePane
    private void mainMenuScene(Stage primaryStage) {
        // Create the "Start Game" button with the custom image
        Button startBtn = getStartButton(primaryStage, gameStartFunction);

        // Create the "Quit Game" button with the custom image
        Button quitBtn = LayoutElement.getQuitBtn(primaryStage);

        // Create the "High Score List" button
        Button highScoreBtn = getHignScoreButton(highScoreFunction);

        // GAME TITLE
        Text gameTitleText = getGameTitleText();

        // Playing instruction
        Text platingInstruction = playInstructionSetting();

        // Create the main menu layout
        VBox mainMenuLayout = new VBox(20, gameTitleText, startBtn, quitBtn, highScoreBtn, platingInstruction);
        mainMenuLayout.setAlignment(Pos.CENTER);

        // Create a StackPane to hold the background and menu layout
        GamePane tempPane = new GamePane(400, 340, "/imageAndFont/box.png");
        tempPane.setBg(mainMenuLayout);
        mainMenuContainer = tempPane.getContainer();

        // Create the main menu mainGameScene
        mainMenuScene = new Scene(mainMenuContainer, 400, 340);

        //stage > mainGameScene > mainGamePane
        primaryStage.setTitle("Asteroids");
        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }
    private void mainGameScene(Stage primaryStage){
        GamePane tempPane = new GamePane(WIDTH, HEIGHT, "/imageAndFont/mainGameBackground.jpg");
        tempPane.setPosition(WIDTH, HEIGHT);

        mainGamePane = tempPane.getPane();

        //record the start time of this game
        startTime = System.currentTimeMillis();

        //add roles to mainGamePane
        addRoles(mainGamePane);

        // Create a StackPane to hold the background and game mainGamePane
        tempPane.setBg();

        // set text info into main game mainGamePane
        mainGameTextInfo();

        // Set music to loop
        music.setOnEndOfMedia(() -> music.seek(Duration.ZERO));
        // Start game music
        music.play();

        mainGameScene = new Scene(tempPane.getTempPane());
        primaryStage.setScene(mainGameScene);
    }
    private void highScoreMenu() throws IOException {

        GamePane highScorePane = new GamePane(400, 340, "/imageAndFont/mainGameBackground.jpg");
        // Create the game over mainGamePane
        highScorePane.setBg(mainMenuContainer);

        Button mainMenuBtn = LayoutElement.getMenuBtn(mainMenuContainer, highScorePane.getContainer());

        highScorePane.addElement(mainMenuBtn);

        // Create a ListView to display the high scores
        ListView<String> highScoreListView = new ListView<>();

        // Read the file
        readFile();
        ObservableList<String> highScores;

        //create the showing content
        highScores = highScoreListContent();

        highScoreListView.setItems(highScores);

        // Set the size and position of the list view
        highScoreListView.setLayoutX(120);
        highScoreListView.setLayoutY(60);
        highScoreListView.setPrefSize(150, 130);

        highScorePane.addElement(highScoreListView);
    }
    private void gameOver(Stage primaryStage, AnimationTimer animationTimer) {
        GamePane tempPane = new GamePane(350, 250, "/imageAndFont/box.png", mainGamePane);

        // Stop the music
        music.stop();

        // Stop the animation timer
        animationTimer.stop();

        // Create the "Restart" button
        Button restartBtn = getRestartBtn(primaryStage, restartFunction);

        // Create the "Back to Main Menu" button
        Button mainMenuBtn = LayoutElement.getMainMenuBtn(primaryStage, tempPane.getPane(), mainGamePane, mainMenuScene);

        // Add buttons to the game over mainGamePane
        tempPane.getPane().getChildren().addAll(restartBtn, mainMenuBtn);

        // gameOverText
        addTextToPane(tempPane.getPane(), "Game  Over", 75, 60, 30);

        // scoreText
        addTextToPane(tempPane.getPane(), "Your Score: " + currentPoints,
                110, 95, 16);

        // code to ask the player to enter a name
        askForPlayerName();

        // Add the game over mainGamePane to the game mainGamePane
        mainGamePane.getChildren().add(tempPane.getPane());
    }
    private void showPauseMenu(Stage primaryStage, AnimationTimer getAnimationTimer) {

        GamePane pausePane = new GamePane(300, 200, "/imageAndFont/box.png", mainGamePane);

        pauseMenuPane = pausePane.getPane();
        pauseMenuContainer = pausePane.getContainer();

        Button resumeBtn = getResumeBtn(getAnimationTimer, resumeFunction, pauseMenuPane);
        Button quitGameBtn = LayoutElement.getQuitGameBtn(primaryStage,pausePane.getPane());

        pausePane.addElement(resumeBtn);
        pausePane.addElement(quitGameBtn);

        mainGamePane.getChildren().add(pauseMenuPane);
    }


    // menu need functions
    private ObservableList<String> highScoreListContent() {
        ObservableList<String> highScores;
        if (scores == null) {
            highScores = FXCollections.observableArrayList("no scores");
        } else {
            // sort the scores
            Arrays.sort(scores, (o1, o2) -> Integer.parseInt(o2[1]) - Integer.parseInt(o1[1]));

            List<String> scoresList = new ArrayList<>();
            if (scores.length < 5) {
                for (int i = 0; i < scores.length; i++) {
                    scoresList.add("No." + (i + 1) + "-" + scores[i][0] + "-" + scores[i][1]);
                }

            } else {
                for (int i = 0; i < 5; i++) {
                    scoresList.add("No." + (i + 1) + "-" + scores[i][0] + "-" + scores[i][1]);
                }
            }
            // Add some test high scores to the list
            highScores = FXCollections.observableArrayList(scoresList);
        }
        return highScores;
    }

    private void readFile() throws IOException {
        FileReader highScoreFile = new FileReader("src/main/java/com/example/asteroids2/highScores.txt");
        BufferedReader bufferedReader = new BufferedReader(highScoreFile);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] tokens = line.split(" ");
            scores = new String[tokens.length / 2][2];
            for (int i = 0, j = 0; i < tokens.length; i += 2, j++) {
                scores[j][0] = tokens[i];
                scores[j][1] = tokens[i + 1];
            }
        }
    }
    private void mainGameTextInfo() {
        infoLabel = new Label("  Score: "+ currentPoints +"  \n  Life: " +
                (ship.getLives()+1) +  "\n  Level: " + changeLevel());
        // Set the font color to white
        infoLabel.setTextFill(Color.WHITE);
        infoLabel.setFont(customFont);

        Label playInstructMainMenu = new Label("""
                   UP: Acceleration
                   DOWN: Deceleration
                   B: Brake
                   LEFT & RIGHT: Rotate
                   SPACE: Fire
                   J: Hyperspace Jump
                   ESC: Pause Game\
                """);
        playInstructMainMenu.setTextFill(Color.WHITE);
        playInstructMainMenu.setLayoutY(485);
        playInstructMainMenu.setLayoutX(0);

        mainGamePane.getChildren().add(infoLabel);
        mainGamePane.getChildren().add(playInstructMainMenu);
    }
    private void askForPlayerName() {
        Platform.runLater(() -> {
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Game Over");
            dialog.setHeaderText("Enter your name:");
            dialog.setContentText("Name:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String name = result.get();
                // record the score with the name
                if (!name.equals("")) {
                    recordNameScore(name.replace(" ", "_"));
                } else {
                    name = "Unknown";
                    recordNameScore(name.replace(" ", "_"));
                }
            }
        });
    }
    private void addTextToPane(Pane pane, String text, double x, double y, int fontSize) {
        Text newText = new Text(text);
        newText.setFont(Font.font("Verdana", FontWeight.BOLD, fontSize));
        newText.setLayoutX(x);
        newText.setLayoutY(y);
        newText.setFill(Color.WHITE);
        pane.getChildren().add(newText);
    }
    private void startNewGame(Stage primaryStage) {
        // Reset player's life, score, and other game states
        asteroids = new ArrayList<>();
        aliens = new ArrayList<>();
        projectiles = new ArrayList<>();
        alienProjectiles = new ArrayList<>();
        pressedKeys = new HashMap<>();

        currentPoints = 0;
        ship = new Ship(WIDTH / 2, HEIGHT / 2);

        startTime = System.currentTimeMillis();

        // Start the game
        mainGameScene(primaryStage);
        AnimationTimer getAnimationTimer = getAnimationTimer(primaryStage);
        getAnimationTimer.start();
        escToPause(primaryStage, getAnimationTimer);
    }
    private void escToPause(Stage primaryStage, AnimationTimer getAnimationTimer) {
        mainGameScene.addEventHandler(KeyEvent.KEY_PRESSED, e3 -> {
            if (e3.getCode() == KeyCode.ESCAPE) {
                if (!isPaused) {
                    getAnimationTimer.stop();
                    isPaused = true;
                    // Show pause menu
                    showPauseMenu(primaryStage, getAnimationTimer);
                } else {
                    mainGamePane.getChildren().remove(pauseMenuPane);
                    mainGamePane.getChildren().remove(pauseMenuContainer);
                    isPaused = false;
                    getAnimationTimer.start();
                }
            }
        });
    }
    private void recordNameScore(String name) {
        // Implement the logic to record the player's score
        try {
            File file = new File("src/main/java/com/example/asteroids2/highScores.txt");
            FileWriter writer = new FileWriter(file, true);
            FileReader reader = new FileReader(file);
            int charCount = reader.read();
            if (charCount == -1) {
                //save username
                writer.write(name + " " + currentPoints);
            } else {
                //save username
                writer.write(" " + name + " " + currentPoints);
            }
            reader.close();
            writer.close();
        } catch (IOException e) {
            System.out.println("File is not found");
        }
    }
    private void showGameInfo() {
        infoLabel.setText("  Score: " + currentPoints + "  \n  Life: " +
                (ship.getLives() + 1) + "  \n  Level: " + changeLevel());
    }

    // button click events
    Consumer<Stage> gameStartFunction = primaryStage -> {
        //reset
        asteroids = new ArrayList<>();
        aliens = new ArrayList<>();
        projectiles = new ArrayList<>();
        alienProjectiles = new ArrayList<>();
        pressedKeys = new HashMap<>();
        mainGameScene(primaryStage);
        AnimationTimer getAnimationTimer = getAnimationTimer(primaryStage);
        getAnimationTimer.start();
        primaryStage.show();

        // Detect ESC key press event
        escToPause(primaryStage, getAnimationTimer);
    };
    Consumer<Stage> highScoreFunction = primaryStage -> {
        try {
            highScoreMenu();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    };
    Consumer<Stage> resumeFunction = primaryStage -> {
        mainGamePane.getChildren().remove(pauseMenuPane);
        mainGamePane.getChildren().remove(pauseMenuContainer);
        isPaused = false;
    };
    Consumer<Stage> restartFunction = this::startNewGame;


    private AnimationTimer getAnimationTimer(Stage primaryStage) {
        return new AnimationTimer() {

            //handle function will be automatically called.
            @Override
            public void handle(long now) {

                //Continuous generation of asteroids and aliens
                createAsteroids(mainGamePane);
                createAliens(mainGamePane);
                //control ship, turning direction, going forward
                controlShip();
                checkInvincibility();

                fire();
                alienFire();
                collision(primaryStage, this);
                moveObjects();
                removeProjectiles();


                //Once a collision occurs, game stops
                asteroids.forEach(asteroid -> {
                    //When a collision occurs，
                    //if the number of deaths of the ship is already greater than or equal to 3, the game will stop.
                    //On the contrary, recreate ship in the center of window
                    try {
                        checkCollisionOfShip(asteroid, primaryStage, this);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

                aliens.forEach(alien -> {
                    try {
                        checkCollisionOfShip(alien, primaryStage, this);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        };
    }

    private void checkCollisionOfShip(FlyingObject obj, Stage primaryStage, AnimationTimer getAnimationTimer) {
        if (ship.collide(obj) && ship.getLives() == 0 && !ship.isInvincibility()) {
            // play explosion sound
            explodeSFX.play();
            // rewind explosion sound to beginning
            explodeSFX.seek(Duration.ZERO);
            gameOver(primaryStage, getAnimationTimer);
        } else if (ship.collide(obj) && !ship.isInvincibility()) {
            lastDestroyedTime = System.currentTimeMillis();
            mainGamePane.getChildren().remove(obj.getShape());
            obj.setIsALive(false);
            mainGamePane.getChildren().remove(ship.getShape());
            //increase the number of death
            int temp = ship.getLives() - 1;
            //change the location ship in the center of window
            ship = new Ship(WIDTH / 2, HEIGHT / 2);
            ship.setInvincibility(true);
            ship.setLives(temp);
            //Change the colour of the reborn ship
            ship.getShape().setFill(Color.RED);
            mainGamePane.getChildren().add(ship.getShape());
            // play explosion sound
            explodeSFX.play();
            // rewind explosion sound to beginning
            explodeSFX.seek(Duration.ZERO);
        }
    }

    private void checkInvincibility() {
        if ((System.currentTimeMillis() - lastDestroyedTime > SHIP_INVINCIBLE_TIME)) {
            ship.setInvincibility(false);
            ship.getShape().setFill(Color.YELLOW);
        }
    }

    private void addRoles(Pane pane) {
        //create ship and add it to mainGamePane
        ship = new Ship(WIDTH / 2, HEIGHT / 2);
        //set the ship invincible
        ship.setInvincibility(true);
        lastDestroyedTime = System.currentTimeMillis();
        ship.getShape().setFill(Color.RED);
        pane.getChildren().add(ship.getShape());
        //create some lowest moving asteroids when a new game starts
        Random rnd = new Random();
        for (int i = 0; i < 2; i++) {
            Asteroid asteroid = new Asteroid(rnd.nextInt(WIDTH), rnd.nextInt(HEIGHT), LARGE.setSize(), LOWER.setSpeedRate());
            //This shows that the newly generated asteroid will not be too close to the ship
            if (!asteroid.collide(ship)) {
                asteroids.add(asteroid);
                pane.getChildren().add(asteroid.getShape());
            }
        }
    }

    private void createAsteroids(Pane pane) {
        //if a random number is less 0.005, a new asteroids will be added to the window.
        if (Math.random() < CREATED_PROBABILITY) {
            if ((System.currentTimeMillis() - startTime) <= Level.EASY_TIME.setLevel()) {
                Asteroid asteroid = new Asteroid(WIDTH, HEIGHT, LARGE.setSize(), LOWER.setSpeedRate());
                if (!asteroid.collide(ship)) {
                    asteroids.add(asteroid);
                    pane.getChildren().add(asteroid.getShape());
                }
            } else if ((System.currentTimeMillis() - startTime) > Level.EASY_TIME.setLevel()
                    && (System.currentTimeMillis() - startTime <= MODERATE_TIME.setLevel())) {
                Asteroid asteroid = new Asteroid(WIDTH, HEIGHT, random.nextInt(MEDIUM.setSize(), LARGE.setSize() + 1), NORMAL.setSpeedRate());
                if (!asteroid.collide(ship)) {
                    asteroids.add(asteroid);
                    pane.getChildren().add(asteroid.getShape());
                }
            } else if ((System.currentTimeMillis() - startTime) > HARD_TIME.setLevel()) {
                Asteroid asteroid = new Asteroid(WIDTH, HEIGHT, random.nextInt(SMALL.setSize(), LARGE.setSize() + 1), FASTER.setSpeedRate());
                if (!asteroid.collide(ship)) {
                    asteroids.add(asteroid);
                    pane.getChildren().add(asteroid.getShape());
                }
            }
        }
    }

    private void createAliens(Pane pane) {
        //if a random number is less 0.005, a new aliens will be added to the window.
        if (Math.random() < CREATED_PROBABILITY && (aliens.size() <= ALIEN_EXISTING_MAXIMUM)) {
            if (((System.currentTimeMillis() - startTime) > MODERATE_TIME.setLevel()) &
                    ((System.currentTimeMillis() - startTime) <= HARD_TIME.setLevel())) {
                Alien alien = new Alien(WIDTH, HEIGHT, 1);
                if (!alien.collide(ship)) {
                    aliens.add(alien);
                    alien.getShape().setFill(Color.TRANSPARENT);
                    alien.getShape().setStroke(Color.RED);
                    pane.getChildren().add(alien.getShape());
                }
            } else {
                Alien alien = new Alien(WIDTH, HEIGHT, 2);
                if (!alien.collide(ship)) {
                    aliens.add(alien);
                    alien.getShape().setFill(Color.TRANSPARENT);
                    alien.getShape().setStroke(Color.RED);
                    pane.getChildren().add(alien.getShape());
                }
            }

        }
    }

    private void controlShip() {
        mainGameScene.setOnKeyPressed(event ->  pressedKeys.put(event.getCode(), Boolean.TRUE));

        mainGameScene.setOnKeyReleased(event ->  pressedKeys.put(event.getCode(), Boolean.FALSE));

        if (pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
            ship.turnLeft();
        }

        if (pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
            ship.turnRight();
        }


        //speed = length / time
        //length = square (x-axis ** 2 + y-axis ** 2)
        //time: The number of times the AnimationTimer refreshes the screen per second
        double speed = Math.sqrt((Math.pow(ship.getMovement().getX(), 2) +
                Math.pow(ship.getMovement().getY(), 2))) * 60;
        if (pressedKeys.getOrDefault(KeyCode.UP, false)) {
            //limit the speed of the ship, prevents unlimited increase in speed of ship
            if (speed < 200) {
                ship.accelerate();
            }
        }

        //This function is used to slow down the ship, but our project does not need this function.
        if (pressedKeys.getOrDefault(KeyCode.DOWN, false)) {
                ship.decelerate();
        }
        // Brake
        if (pressedKeys.getOrDefault(KeyCode.B, false)) {
                ship.setMovement(new Point2D(0,0));
        }

        if (pressedKeys.getOrDefault(KeyCode.J, false)) {
            //per 3 seconds, ship can use one time
            if ((System.currentTimeMillis() - lastThrustTime) > SHIP_THRUST_INTERVAL) {
                //remove the shape of ship from mainGamePane
                mainGamePane.getChildren().remove(ship.getShape());
                //using while loop until find a proper position
                ship.thrust(random.nextInt(WIDTH), random.nextInt(HEIGHT));
                while (thrustCheckAsteroid() && thrustCheckAlien()) {
                    ship.thrust(random.nextInt(WIDTH), random.nextInt(HEIGHT));
                }
                //only a thrust has successfully occurred, we record the time
                lastThrustTime = System.currentTimeMillis();
                mainGamePane.getChildren().add(ship.getShape());
                // play jump sound
                jumpSFX.play();
                // rewind jump sound to beginning
                jumpSFX.seek(Duration.ZERO);
            }
        }

        ship.move();
    }

    //The following two functions are designed to detect if a collision has occurred after a thrust
    protected boolean thrustCheckAsteroid() {
        for (Asteroid asteroid : asteroids) {
            if (asteroid.collide(ship)) {
                return true;
            }
        }
        return false;
    }

    protected boolean thrustCheckAlien() {
        for (Alien alien : aliens) {
            if (alien.collide(ship)) {
                return true;
            }
        }
        return false;
    }

    private void fire() {
        //only 10 projectiles exist at the same time
        if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && projectiles.size() < SHIP_PROJECTILE_EXIST_MAXIMUM) {
            //fire interval is 500 ms
            if (System.currentTimeMillis() - lastFireTime > SHIP_FIRE_INTERVAL) {
                lastFireTime = System.currentTimeMillis();
                Projectile projectile = new Projectile(
                        (int) ship.getShape().getTranslateX(),
                        (int) ship.getShape().getTranslateY());
                projectile.getShape().setRotate(ship.getShape().getRotate());
                projectiles.add(projectile);

                projectile.accelerate();
                projectile.setMovement(projectile.getMovement().normalize().multiply(3));
                projectile.getShape().setFill(Color.YELLOW);
                mainGamePane.getChildren().add(projectile.getShape());

                // play sound
                fireSFX.play();
                // return sound to beginning
                fireSFX.seek(Duration.ZERO);
            }
        }
        //make all projectiles move
        projectiles.forEach(FlyingObject::move);
    }

    private void alienFire() {
        //fire interval is 5000 ms
        if (System.currentTimeMillis() - alienLastFireTime > ALIEN_FIRE_INTERVAL) {
            alienLastFireTime = System.currentTimeMillis();
            aliens.forEach(alien -> {
                Projectile alienprojectile = new Projectile(
                        (int) alien.getShape().getTranslateX(),
                        (int) alien.getShape().getTranslateY());
                //Adjusting the angle of the projectile so that it is directed towards the airship
                alienprojectile.getShape().setRotate(
                        Math.toDegrees(Math.atan2(ship.getShape().getTranslateY() - alien.getShape().getTranslateY(),
                                ship.getShape().getTranslateX() - alien.getShape().getTranslateX()
                        )));

                alienProjectiles.add(alienprojectile);


                alienprojectile.accelerate();
                alienprojectile.setMovement(alienprojectile.getMovement().normalize().multiply(3));
                alienprojectile.getShape().setFill(Color.RED);
                mainGamePane.getChildren().add(alienprojectile.getShape());
            });
        }

        //make all projectiles move
        alienProjectiles.forEach(FlyingObject::move);
    }

    //remove projectiles that have timed out
    public void removeProjectiles() {
        //Label projectiles that have timed out
        projectiles.forEach(projectile -> {
            //the projectile will be removed after 5 seconds
            if ((System.currentTimeMillis() - projectile.getCreateTime()) >= SHIP_PROJECTILE_EXIST_TIME) {
                projectile.setIsALive(false);
            }
        });

        alienProjectiles.forEach(alienProjectile -> {
            //the projectile will be removed after 2 seconds
            if ((System.currentTimeMillis() - alienProjectile.getCreateTime()) >= ALIEN_PROJECTILE_EXIST_TIME) {
                alienProjectile.setIsALive(false);
            }
        });
    }

    public void removeObjects() {
        //if a projectile and asteroid, both of them should be deleted from the projectiles and asteroids list, respectively.
        //remove all dead projectiles and asteroids from the window
        projectiles.stream()
                .filter(projectile -> !projectile.getIsALive())  //the statement returns a stream which includes the dead projectiles
                .forEach(projectile -> mainGamePane.getChildren().remove(projectile.getShape()));//remove the shape of  every dead projectile
        alienProjectiles.stream()
                .filter(alienProjectile -> !alienProjectile.getIsALive())  //the statement returns a stream which includes the dead projectiles
                .forEach(alienProjectile -> mainGamePane.getChildren().remove(alienProjectile.getShape()));
        asteroids.stream()
                .filter(asteroid -> !asteroid.getIsALive())
                .forEach(asteroid -> {
                    mainGamePane.getChildren().remove(asteroid.getShape());
                    //For each asteroid destroyed, the player gains ten points
                    //bonus score can be set up in 'addAndGet' by change the parameter.
                    if (asteroid.getSize() == SMALL.setSize()) {
                        currentPoints += 250;
                        lastPoints += 250;
                        showGameInfo();
                        bonusLife();

                    } else if (asteroid.getSize() == MEDIUM.setSize()) {
                        currentPoints += 500;
                        lastPoints += 500;
                        showGameInfo();
                        bonusLife();
                    } else if (asteroid.getSize() == LARGE.setSize()) {
                        currentPoints += 1000;
                        lastPoints += 1000;
                        showGameInfo();
                        bonusLife();
                    }
                });
        aliens.stream()
                .filter(alien -> !alien.getIsALive())
                .forEach(alien -> {
                    mainGamePane.getChildren().remove(alien.getShape());
                    currentPoints += 2000;
                    lastPoints += 2000;
                    showGameInfo();
                    bonusLife();
                });

        //Remove all projectiles and asteroids that collide
        projectiles.removeAll(projectiles.stream()
                .filter(projectile -> !projectile.getIsALive())
                .toList());
        alienProjectiles.removeAll(alienProjectiles.stream()
                .filter(alienProjectile -> !alienProjectile.getIsALive())
                .toList());
        asteroids.removeAll(asteroids.stream()
                .filter(asteroid -> !asteroid.getIsALive())
                .toList());
        aliens.removeAll(aliens.stream()
                .filter(alien -> !alien.getIsALive())
                .toList());
    }

    public void collision(Stage primaryStage, AnimationTimer getAnimationTimer) {
        //Label asteroids and projectiles as dead if they collide
        projectiles.forEach(projectile -> {
            //check projectiles and asteroids
            asteroids.forEach(asteroid -> {
                if (projectile.collide(asteroid)) {
                    //record the number of death of each object
                    projectile.setLives(0);
                    asteroid.setLives(0);
                    //set the status of objects which occurs collision is false
                    //it mean that the status objects marked as false should be deleted
                    projectile.setIsALive(false);
                    asteroid.setIsALive(false);
                    // play explosion sound
                    explodeSFX.play();
                    // rewind explosion sound to beginning
                    explodeSFX.seek(Duration.ZERO);
                }
            });
            //check projectiles and aliens
            aliens.forEach(alien -> {
                if (projectile.collide(alien)) {
                    projectile.setLives(0);
                    alien.setLives(0);
                    //set the status of objects which occurs collision is false
                    //it mean that the status objects marked as false should be deleted
                    projectile.setIsALive(false);
                    alien.setIsALive(false);
                    // play explosion sound
                    explodeSFX.play();
                    // rewind explosion sound to beginning
                    explodeSFX.seek(Duration.ZERO);
                }
            });
        });

        alienProjectiles.forEach(alienProjectile -> {
            try {
                checkCollisionOfShip(alienProjectile, primaryStage, getAnimationTimer);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        //If a larger asteroid is destroyed, then three smaller asteroids are created in the same place
        asteroids.addAll(createSmallerAsteroid());
        //delete the hit objects and the projectile
        removeObjects();
    }

    private ArrayList<Asteroid> createSmallerAsteroid() {
        ArrayList<Asteroid> smallerAsteroids = new ArrayList<>();
        asteroids.stream()
                .filter(asteroid -> !asteroid.getIsALive())
                .forEach(asteroid -> {
                    if (asteroid.getSize() != SMALL.setSize()) {
                        int temp = 0;
                        while (temp < 2) {
                            Asteroid newAsteroid = new Asteroid(
                                    (int) asteroid.getShape().getTranslateX() + random.nextInt(-10, 10),
                                    (int) asteroid.getShape().getTranslateY() + random.nextInt(-10, 10),
                                    asteroid.getSize() - 1, asteroid.getSpeedTimes() - 1);
                            if (!newAsteroid.collide(ship)) {
                                smallerAsteroids.add(newAsteroid);
                                mainGamePane.getChildren().add(newAsteroid.getShape());
                                temp += 1;
                            }
                        }
                    }
                });
        smallerAsteroids.forEach(Asteroid::move);
        return smallerAsteroids;
    }

    //according to the time to change the level of current game
    private String changeLevel() {
        double duration = System.currentTimeMillis() - startTime;
        if (duration < Level.EASY_TIME.setLevel()) {
            return String.valueOf(EASY_LEVEL.setLevel());
        } else if ((duration > EASY_TIME.setLevel())
                && (duration <= MODERATE_TIME.setLevel())) {
            return String.valueOf(MODERATE_LEVEL.setLevel());
        } else {
            return String.valueOf(HARD_LEVEL.setLevel());
        }
    }

    private void bonusLife() {
        System.out.println("hit");
        if (lastPoints >= 5000) {
            lastPoints -= 5000;
            if (ship.getLives() <= 5) {
                ship.setLives(ship.getLives() + 1);
                infoLabel.setText("  Score: " + currentPoints + "  \n  Life: " +
                        (ship.getLives() + 1) + "  LIFE + 1" + "  \n  Level: " + changeLevel());
            }
        }
    }

    //make all asteroids can move
    private void moveObjects() {
        asteroids.forEach(Asteroid::move);
        aliens.forEach(FlyingObject::move);
    }

    public static void main(String[] args) {
        launch(args);
    }
}