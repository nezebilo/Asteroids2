package com.example.asteroids2;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main extends Application {
    protected Pane pane;

    protected Scene scene;

    protected Text score;
    protected Map<KeyCode, Boolean> pressedKeys = new HashMap<>();
    public static int WIDTH = 600;
    public static int HEIGHT = 600;

    protected Ship ship;

    protected List<Asteroid> asteroids  = new ArrayList<>();

    //sample asteroid, if it is not needed will be deleted.
    protected Asteroid asteroid;

    protected List<Projectile> projectiles = new ArrayList<>();
    //record the last firing time, to control firing frequency
    protected double lastFireTime;

    /**
     * To do:
     * menu
     * game level changes with time
     * ship life
     * collision between player ship and asteroids
     * add aliens
     * add image to different roles
     */


    @Override
    public void start(Stage stage) throws Exception {
        //stage > scene > pane
        //game pane
        pane = new Pane();
        pane.setPrefSize(WIDTH, HEIGHT);
        //display score
        score = new Text(10,20,"Score: 0");
        pane.getChildren().add(score);
        //add roles to pane
        addRoles(pane);

        //game scene
        scene = new Scene(pane);
        stage.setTitle("Asteroids Game");
        stage.setScene(scene);


        AnimationTimer getAnimationTimer = getAnimationTimer();
        getAnimationTimer.start();

        stage.show();
    }

    private AnimationTimer getAnimationTimer() {
        return new AnimationTimer() {

            //handle function will be automatically called.
            @Override
            public void handle(long now) {
                scene.setOnKeyPressed(event -> {
                    pressedKeys.put(event.getCode(), Boolean.TRUE);
                });

                scene.setOnKeyReleased(event -> {
                    pressedKeys.put(event.getCode(), Boolean.FALSE);
                });
                //Continuous generation of asteroids
                createAsteroids(pane);
                //control ship, turning direction, going forward
                controlShip();

                fire();
                collision();
                moveAsteroid();


                //Once a collision occurs, game stops
                asteroids.forEach(asteroid -> {
                    if(ship.collide(asteroid)){
                        stop();
                    }
                });
            }
        };
    }

    private AtomicInteger getAtomicInteger(){
        return new AtomicInteger();
    };


    private void addRoles(Pane pane) {
        //create ship and add it to pane
        ship = new Ship(WIDTH / 2, HEIGHT / 2);
        ship.setRotate();
        pane.getChildren().add(ship.getShape());
        //create some asteroid when a new game starts
        Random rnd = new Random();
        for (int i = 0; i < 5; i++) {
            Asteroid asteroid = new Asteroid(rnd.nextInt(WIDTH), rnd.nextInt(HEIGHT));
            //This shows that the newly generated asteroid will not be too close to the ship
            if(!asteroid.collide(ship)) {
                asteroids.add(asteroid);
                pane.getChildren().add(asteroid.getShape());
            }
        }
    }

    private void createAsteroids(Pane pane) {
        //if a random number is less 0.005, a new asteroids will be added to the window.
        if(Math.random() < 0.005) {
            Asteroid asteroid = new Asteroid(WIDTH, HEIGHT);
            if(!asteroid.collide(ship)) {
                asteroids.add(asteroid);
                pane.getChildren().add(asteroid.getShape());
            }
        }
    }

    private void controlShip() {
        if (pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
            ship.turnLeft();
        }

        if (pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
            ship.turnRight();
        }

        if (pressedKeys.getOrDefault(KeyCode.UP, false)) {
            ship.accelerate();
        }

        ship.move();
    }

    private void fire(){
        //only 10 projectiles exist at the same time
        if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && projectiles.size() < 10) {
            //fire interval is 500 ms
            if (System.currentTimeMillis() - lastFireTime > 500) {
                lastFireTime = System.currentTimeMillis();
                Projectile projectile = new Projectile((int) ship.getShape().getTranslateX(), (int) ship.getShape().getTranslateY());
                projectile.getShape().setRotate(ship.getShape().getRotate());
                projectiles.add(projectile);

                projectile.accelerate();
                projectile.setMovement(projectile.getMovement().normalize().multiply(3));
                pane.getChildren().add(projectile.getShape());
            }
        }
        //make all projectiles move
        projectiles.forEach(projectile -> projectile.move());
    }

    public void collision(){

        //if a projectile hits an asteroid, both of them disappear
        projectiles.forEach(projectile -> {
            asteroids.forEach(asteroid -> {
                if(projectile.collide(asteroid)) {
                    //record the number of death of each object
                    projectile.setNumOfDeath(1);
                    asteroid.setNumOfDeath(1);
                    //set the status of objects which occurs collision is false
                    //it mean that the status objects marked as false should be deleted
                    projectile.setIsALive(false);
                    asteroid.setIsALive(false);
                }
                //For each asteroid destroyed, the player gains ten points
                //bonus score can be set up in 'addAndGet' by change the parameter.
                if(!asteroid.isAlive){
                    score.setText("Score: " + getAtomicInteger().addAndGet(10));
                }
            });
        });

        projectiles.stream()
                .filter(projectile -> !projectile.getIsALive())
                .forEach(projectile -> pane.getChildren().remove(projectile.getShape()));
        asteroids.stream()
                .filter(asteroid -> !asteroid.getIsALive())
                .forEach(asteroid -> pane.getChildren().remove(asteroid.getShape()));
        //Remove all projectiles and asteroids that collide
        projectiles.removeAll(projectiles.stream()
                .filter(projectile -> !projectile.getIsALive())
                .collect(Collectors.toList()));
        asteroids.removeAll(asteroids.stream()
                .filter(asteroid -> !asteroid.getIsALive())
                .collect(Collectors.toList()));
    }
    //make all asteroids can move
    private void moveAsteroid() {
        asteroids.forEach(asteroid -> asteroid.move());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
