package com.example.asteroids2;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.*;

public class GameStart extends Application {
    public static int WIDTH = 600;
    public static int HEIGHT = 600;

    private static int INITIAL_HP = 3;
    private static int INITIAL_ASTEROIDS = 5;

    private HashMap<Team, List<FlyingObject>> teamsOfFlyingObjects;
    private PlayerShip playerShip;
    private Pane pane;
    private HashMap<KeyCode, Boolean> keysPressed;

    @Override
    public void start(Stage stage) throws Exception {
        this.pane = new Pane();
        pane.setPrefSize(WIDTH, HEIGHT);
        Scene scene = new Scene(pane);

        addObjectsAtBeginning();
        trackKeyboard(scene);

        AnimationTimer animationTimer = animationManager();
        animationTimer.start();

        stage.setScene(scene);
        stage.show();
    }

    private AnimationTimer animationManager() {
        return new AnimationTimer() {
            @Override
            public void handle(long l) {
                playerShipControls();
                moveAllObjects();
                checkCollisionBetweenTeams();
                resolveCollisions();
                if (!playerShip.isAlive()) stop();
            }
        };
    }

    private void resolveCollisions() {
        //todo: remove flying objects that collided, and add resultant new objects to pane
        for (List<FlyingObject> team : teamsOfFlyingObjects.values()) {
            // remove or add while iterating - concurrent modification: use iterator
            ListIterator<FlyingObject> iterator = team.listIterator();
            while (iterator.hasNext()) {
                FlyingObject current = iterator.next();
                // if this object collided with another
                if (!current.isAlive()) {
                    // get new flying objects after collision: playership - new playership; asteroid: smaller asteroids
                    List<FlyingObject> newObjs = current.collideAction();
                    // remove this object from game
                    iterator.remove();
                    pane.getChildren().remove(current.getBody());
                    // add new flying objects, if any, back into game
                    if (newObjs != null) {
                        for (FlyingObject newObj : newObjs) {
                            iterator.add(newObj);
                            pane.getChildren().add(newObj.getBody());
                            if (newObj instanceof PlayerShip)
                                playerShip = (PlayerShip) newObj;
                        }
                    }
                }
            }
        }
    }

    private void moveAllObjects() {
        for (List<FlyingObject> team : teamsOfFlyingObjects.values())
            for (FlyingObject object : team)
                object.move();
    }

    private void playerShipControls() {
        if (keysPressed.getOrDefault(KeyCode.LEFT, false))
            playerShip.turn(true);
        if (keysPressed.getOrDefault(KeyCode.RIGHT, false))
            playerShip.turn(false);
        if (keysPressed.getOrDefault(KeyCode.UP, false))
            playerShip.applyThrust();
    }

    private void checkCollisionBetweenTeams() {
        //todo: test collision between each team - iterate over keys with new List<KeySet()>
        List<Team> teamKeys = new ArrayList<>(teamsOfFlyingObjects.keySet());
        for (int i = 0; i < teamKeys.size(); i++) {
            // team1 = player team in 1st iteration, = enemy in last iteration
            List<FlyingObject> team1 = teamsOfFlyingObjects.get(teamKeys.get(i));
            for (int j = i + 1; j < teamKeys.size(); j++) {
                List<FlyingObject> team2 = teamsOfFlyingObjects.get(teamKeys.get(j));
                // check collision between each object of the 2 teams
                for (FlyingObject obj1 : team1)
                    for (FlyingObject obj2 : team2)
                        FlyingObject.checkCollision(obj1, obj2);
            }
        }
    }

    private void trackKeyboard(Scene scene) {
        keysPressed = new HashMap<>();
        scene.setOnKeyPressed(keyEvent -> keysPressed.put(keyEvent.getCode(), true));
        scene.setOnKeyReleased(keyEvent -> keysPressed.put(keyEvent.getCode(), false));
    }

    private void addObjectsAtBeginning() {
        // create flying objects and add to flyingObjects list
        teamsOfFlyingObjects = new HashMap<>();

        // create player ship at center of screen
        this.playerShip = new PlayerShip(INITIAL_HP);
        teamsOfFlyingObjects.put(playerShip.getTeam(),
                new ArrayList<>(Collections.singletonList(playerShip)));

        // create asteroids
        List<FlyingObject> asteroids = new ArrayList<>();
        for (int i = 0; i < INITIAL_ASTEROIDS; i++)
            asteroids.add(new SampleAsteroid());
        teamsOfFlyingObjects.put(Team.ASTEROID, asteroids);

        for (List<FlyingObject> team : teamsOfFlyingObjects.values())
            for (FlyingObject object : team)
                pane.getChildren().add(object.getBody());
    }
}
