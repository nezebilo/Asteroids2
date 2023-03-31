package com.example.asteroids2;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameStart extends Application {
    public static int WIDTH = 600;
    public static int HEIGHT = 600;

    private List<FlyingObject> flyingObjects;
    private PlayerShip playerShip;
    private HashMap<KeyCode, Boolean> keysPressed;

    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = new Pane();
        pane.setPrefSize(WIDTH, HEIGHT);
        Scene scene = new Scene(pane);

        createObjects(pane);
        trackKeyboard(scene);

        AnimationTimer animationTimer = animationManager(pane);
        animationTimer.start();

        stage.setScene(scene);
        stage.show();
    }

    private AnimationTimer animationManager(Pane pane) {
        return new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (keysPressed.getOrDefault(KeyCode.LEFT, false))
                    playerShip.turn(true);
                if (keysPressed.getOrDefault(KeyCode.RIGHT, false))
                    playerShip.turn(false);
                if (keysPressed.getOrDefault(KeyCode.UP, false))
                    playerShip.applyThrust();
                // let all objects move
                for (FlyingObject object : flyingObjects)
                    object.move();

                //todo: remove: testing playership's collision method
                if (flyingObjects.get(0).collide(flyingObjects.get(1))) {
                    stop();
                }
            }
        };
    }

    private void trackKeyboard(Scene scene) {
        keysPressed = new HashMap<>();
        scene.setOnKeyPressed(keyEvent -> keysPressed.put(keyEvent.getCode(), true));
        scene.setOnKeyReleased(keyEvent -> keysPressed.put(keyEvent.getCode(), false));
    }

    private void createObjects(Pane pane) {
        // create flying objects and add to flyingObjects list
        //todo: 2-D List - list[0]: player; list[1]: asteroid; list[3]: enemyShip: easier collision detection
        flyingObjects = new ArrayList<>();

        // create player ship at center of screen
        this.playerShip = new PlayerShip(WIDTH / 2, HEIGHT / 2);
        flyingObjects.add(this.playerShip);

        // create asteroids
        SampleAsteroid asteroid = new SampleAsteroid();
        flyingObjects.add(asteroid);

        for (FlyingObject object : flyingObjects)
            pane.getChildren().add(object.getBody());
    }
}
