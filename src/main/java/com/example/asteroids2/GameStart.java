package com.example.asteroids2;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;

public class GameStart extends Application {
    public final static int WIDTH = 600;
    public final static int HEIGHT = 600;

    private final static int PLAYER_LIVES = 3;
    private final static int INITIAL_ASTEROIDS = 5;

    private HashMap<Team, List<FlyingObject>> teamsOfFlyingObjects;
    private PlayerShip playerShip;
    private Pane pane;
    private HashMap<KeyCode, Boolean> keysPressed;
    //store all bullets
    private List<Bullet> bullets = new ArrayList<>();

    private long lastFireTime;

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
                fire();
                removeBullets();
                moveAllObjects();
                checkCollisionBetweenTeams();
                resolveCollisions();
                // this will be true only when the player ship is removed from the pane
                // and no new player ship was returned from playerShip.collideAction();
                // meaning: when playerShip's remaining lives = 0
                if (!playerShip.isAlive()) stop();
            }
        };
    }

    private void fire() {
            //only 10 bullets exist at the same time
            if (keysPressed.getOrDefault(KeyCode.SPACE, false) && bullets.size() < 10) {
                //fire interval is 500 ms
                if (System.currentTimeMillis() - lastFireTime > 500) {
                    lastFireTime = System.currentTimeMillis();
                    Bullet bullet = new Bullet((int) playerShip.getBody().getTranslateX(), (int) playerShip.getBody().getTranslateY());
                    bullet.getShape().setRotate(playerShip.getBody().getRotate());
                    bullets.add(bullet);

                    bullet.accelerate();
                    bullet.setMovement(bullet.getMovement().normalize().multiply(3));
                    pane.getChildren().add(bullet.getShape());
                }
            }
            //make all bullets move
            bullets.forEach(bullet -> bullet.move());
        }

    public void removeBullets() {
        //Label bullets that have timed out
        bullets.forEach(projectile -> {
            //the projectile will be removed after 10 seconds
            if ((System.currentTimeMillis() - projectile.getCreateTime()) >= 10000) {
                projectile.setIsALive(false);
            }
        });

        //remove bullets that have been marked as dead
        bullets.stream()
                .filter(projectile -> !projectile.getIsALive())
                .forEach(projectile -> {
                    pane.getChildren().remove(projectile.getShape());
                });

        bullets.removeAll(bullets.stream()
                .filter(projectile -> !projectile.getIsALive())
                .collect(Collectors.toList()));

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
                    // get new flying objects after collision: player ship - new player ship; asteroid: smaller asteroids
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
        for (List<FlyingObject> team : teamsOfFlyingObjects.values()) {
            for (FlyingObject object : team) {
                object.move();
            }
        }

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
        this.playerShip = new PlayerShip(PLAYER_LIVES);
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
