package com.example.asteroids2;

import com.example.asteroids2.Flyingobject.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.example.asteroids2.ConstantVar.Size.*;
import static com.example.asteroids2.ConstantVar.SpeedRate.*;
import static com.example.asteroids2.ConstantVar.Level.*;



public class Main extends Application {
    protected Pane pane;

    protected Scene scene;

    protected double startTime;

    protected Text score;

    protected int points;
    protected Map<KeyCode, Boolean> pressedKeys = new HashMap<>();
    public static int WIDTH = 600;
    public static int HEIGHT = 600;

    protected Ship ship;

    protected List<Asteroid> asteroids = new ArrayList<>();

    protected List<Alien> aliens = new ArrayList<>();

    protected List<Projectile> projectiles = new ArrayList<>();


    protected List<Projectile> alienProjectiles = new ArrayList<>();


    //record the last firing time, to control firing frequency
    protected double lastFireTime;

    protected double lastDestroyedTime;

    protected double alienLastFireTime;
    protected double lastThrustTime;

    protected static final int SHIP_FIRE_INTERVAL = 500; // ms
    protected static final int ALIEN_FIRE_INTERVAL = 5000; // ms
    protected static final int SHIP_THRUST_INTERVAL = 5000; // ms
    protected static final int SHIP_INVINCIBLE_TIME = 3000; // ms
    protected static final int SHIP_PROJECTILE_EXIST_TIME = 5000;// ms
    protected static final int ALIEN_PROJECTILE_EXIST_TIME = 5000;// ms




    protected Random random = new Random();

    /*
     *暂停游戏
     * 储存和获取最高分
     * 优化menu/UI
     */
    @Override
    public void start(Stage stage) throws Exception {
        //stage > scene > pane
        //game pane
        pane = new Pane();
        pane.setPrefSize(WIDTH, HEIGHT);
        //record the start time of this game
        startTime = System.currentTimeMillis();
        //display score
        score = new Text(10, 20, "Score: 0");
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

                //Continuous generation of asteroids and aliens
                createAsteroids(pane);
                createAliens(pane);
                //control ship, turning direction, going forward
                controlShip();
                checkInvincibility();

                fire();
                alienFire();
                collision();
                moveObjects();
                removeProjectiles();


                //Once a collision occurs, game stops
                asteroids.forEach(asteroid -> {
                    //When a collision occurs，
                    //if the number of deaths of the ship is already greater than or equal to 3, the game will stop.
                    //On the contrary, recreate ship in the center of window
                    try {
                        checkCollisionOfShip(asteroid);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

                aliens.forEach(alien -> {
                    try {
                        checkCollisionOfShip(alien);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        };
    }

    private void checkCollisionOfShip(FlyingObject obj) throws Exception {
        if (ship.collide(obj) && ship.getNumOfDeath() > 2 && !ship.isInvincibility()) {
            System.exit(0);
        } else if (ship.collide(obj) && !ship.isInvincibility()) {
            lastDestroyedTime = System.currentTimeMillis();
            pane.getChildren().remove(obj.getShape());
            obj.setIsALive(false);
            pane.getChildren().remove(ship.getShape());
            //increase the number of death
            int temp = ship.getNumOfDeath() + 1;
            //change the location ship in the center of window
            ship = new Ship(WIDTH / 2, HEIGHT / 2);
            ship.setInvincibility(true);
            ship.setNumOfDeath(temp);
            //Change the colour of the reborn ship
            ship.getShape().setFill(Color.web("#FF0000"));
            pane.getChildren().add(ship.getShape());
        }
    }

    private void checkInvincibility(){
        if ((System.currentTimeMillis() - lastDestroyedTime > SHIP_INVINCIBLE_TIME)){
            ship.setInvincibility(false);
            ship.getShape().setFill(Color.BLACK);
        }
    }

    private AtomicInteger getAtomicInteger() {
        return new AtomicInteger();
    }

    private void addRoles(Pane pane) {
        //create ship and add it to pane
        ship = new Ship(WIDTH / 2, HEIGHT / 2);
        pane.getChildren().add(ship.getShape());
        //create some lowest moving asteroids when a new game starts
        Random rnd = new Random();
        for (int i = 0; i < 2; i++) {
            Asteroid asteroid = new Asteroid(rnd.nextInt(WIDTH), rnd.nextInt(HEIGHT),LARGE.setSize(), LOWER.setSpeedRate());
            //This shows that the newly generated asteroid will not be too close to the ship
            if (!asteroid.collide(ship)) {
                asteroids.add(asteroid);
                pane.getChildren().add(asteroid.getShape());
            }
        }
    }

    private void createAsteroids(Pane pane) {
        //if a random number is less 0.005, a new asteroids will be added to the window.
        if (Math.random() < 0.001) {
            if ((System.currentTimeMillis() - startTime) <= EASY.setLevel()) {
                Asteroid asteroid = new Asteroid(WIDTH, HEIGHT, LARGE.setSize(), LOWER.setSpeedRate());
                if (!asteroid.collide(ship)) {
                    asteroids.add(asteroid);
                    pane.getChildren().add(asteroid.getShape());
                }
            } else if ((System.currentTimeMillis() - startTime) > EASY.setLevel()
                    && (System.currentTimeMillis() - startTime <= MODERATE.setLevel())) {
                Asteroid asteroid = new Asteroid(WIDTH, HEIGHT, random.nextInt(MEDIUM.setSize(), LARGE.setSize() + 1), NORMAL.setSpeedRate());
                if (!asteroid.collide(ship)) {
                    asteroids.add(asteroid);
                    pane.getChildren().add(asteroid.getShape());
                }
            } else if ((System.currentTimeMillis() - startTime) > HARD.setLevel()) {
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
        if (Math.random() < 0.001) {
            if (((System.currentTimeMillis() - startTime) > MODERATE.setLevel()) &
                    ((System.currentTimeMillis() - startTime) <= HARD.setLevel())) {
                Alien alien = new Alien(WIDTH, HEIGHT,1);
                if (!alien.collide(ship)) {
                    aliens.add(alien);
                    alien.getShape().setFill(Color.TRANSPARENT);
                    alien.getShape().setStroke(Color.RED);
                    pane.getChildren().add(alien.getShape());
                }
            } else{
                Alien alien = new Alien(WIDTH, HEIGHT,2);
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
        scene.setOnKeyPressed(event -> {
            pressedKeys.put(event.getCode(), Boolean.TRUE);
        });

        scene.setOnKeyReleased(event -> {
            pressedKeys.put(event.getCode(), Boolean.FALSE);
        });

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
                Math.pow(ship.getMovement().getY(), 2))) * 60 ;
        if (pressedKeys.getOrDefault(KeyCode.UP, false)) {
            //limit the speed of the ship, prevents unlimited increase in speed of ship
            if (speed < 200){
                ship.accelerate(1);
            }
        }


        //This function is used to slow down the ship, but our project does not need this function.
        if (pressedKeys.getOrDefault(KeyCode.DOWN, false)) {
            if (speed >= 0 ){
                ship.setMovement(new Point2D(0,0));
            }
        }



        if (pressedKeys.getOrDefault(KeyCode.J, false)){
            //per 3 seconds, ship can use one time
            if((System.currentTimeMillis() - lastThrustTime) > SHIP_THRUST_INTERVAL){
                //remove the shape of ship from pane
                pane.getChildren().remove(ship.getShape());
                //using while loop until find a proper position
                ship.thrust(random.nextInt(WIDTH), random.nextInt(HEIGHT));
                while (thrustCheckAsteroid() && thrustCheckAlien()){
                    ship.thrust(random.nextInt(WIDTH), random.nextInt(HEIGHT));
                }
                //only a thrust has successfully occurred, we record the time
                lastThrustTime = System.currentTimeMillis();
                pane.getChildren().add(ship.getShape());
            }
        }

        //slowdown function

        ship.move();
    }

    //The following two functions are designed to detect if a collision has occurred after a thrust
    protected boolean thrustCheckAsteroid(){
        for(Asteroid asteroid : asteroids){
            if (asteroid.collide(ship)){
                return true;
            }
        }
        return false;
    }

    protected boolean thrustCheckAlien(){
        for(Alien alien : aliens){
            if (alien.collide(ship)){
                return true;
            }
        }
        return false;
    }

    private void fire() {
        //only 10 projectiles exist at the same time
        if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && projectiles.size() < 10) {
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
                pane.getChildren().add(projectile.getShape());
            }
        }
        //make all projectiles move
        projectiles.forEach(projectile -> projectile.move());
    }

    private void alienFire(){
        //fire interval is 500 ms
        if (System.currentTimeMillis() - alienLastFireTime > ALIEN_FIRE_INTERVAL) {
            alienLastFireTime = System.currentTimeMillis();
            aliens.forEach(alien -> {
                Projectile alienprojectile = new Projectile(
                        (int) alien.getShape().getTranslateX(),
                        (int) alien.getShape().getTranslateY());
                alienprojectile.getShape().setRotate(
                        Math.toDegrees(
                                Math.atan(
                                        (ship.getShape().getTranslateY() - alien.getShape().getTranslateY())/
                                                (ship.getShape().getTranslateX() - alien.getShape().getTranslateX())
                                )));
                alienProjectiles.add(alienprojectile);


                alienprojectile.accelerate();
                alienprojectile.setMovement(alienprojectile.getMovement().normalize().multiply(3));
                alienprojectile.getShape().setFill(Color.RED);
                pane.getChildren().add(alienprojectile.getShape());
            });
        }

        //make all projectiles move
        alienProjectiles.forEach(alienProjectile -> alienProjectile.move());
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

    public void removeObjects(){
        //if a projectile and asteroid, both of them should be deleted from the projectiles and asteroids list, respectively.
        //remove all dead projectiles and asteroids from the window
        projectiles.stream()
                .filter(projectile -> !projectile.getIsALive())  //the statement returns a stream which includes the dead projectiles
                .forEach(projectile -> pane.getChildren().remove(projectile.getShape()));//remove the shape of  every dead projectile
        alienProjectiles.stream()
                .filter(alienProjectile -> !alienProjectile.getIsALive())  //the statement returns a stream which includes the dead projectiles
                .forEach(alienProjectile -> pane.getChildren().remove(alienProjectile.getShape()));
        asteroids.stream()
                .filter(asteroid -> !asteroid.getIsALive())
                .forEach(asteroid -> {
                    pane.getChildren().remove(asteroid.getShape());
                    //For each asteroid destroyed, the player gains ten points
                    //bonus score can be set up in 'addAndGet' by change the parameter.
                    points += 10;
                    score.setText("Score: " + getAtomicInteger().addAndGet(points));
                });
        aliens.stream()
                .filter(alien -> !alien.getIsALive())
                .forEach(alien -> {
                    pane.getChildren().remove(alien.getShape());
                    points += 20;
                    score.setText("Score: " + getAtomicInteger().addAndGet(points));
                });
        //Remove all projectiles and asteroids that collide
        projectiles.removeAll(projectiles.stream()
                .filter(projectile -> !projectile.getIsALive())
                .collect(Collectors.toList()));
        alienProjectiles.removeAll(alienProjectiles.stream()
                .filter(alienProjectile -> !alienProjectile.getIsALive())
                .collect(Collectors.toList()));
        asteroids.removeAll(asteroids.stream()
                .filter(asteroid -> !asteroid.getIsALive())
                .collect(Collectors.toList()));
        aliens.removeAll(aliens.stream()
                .filter(alien -> !alien.getIsALive())
                .collect(Collectors.toList()));
    }

    public void collision() {
        //Label asteroids and projectiles as dead if they collide
        projectiles.forEach(projectile -> {
            //check projectiles and asteroids
            asteroids.forEach(asteroid -> {
                if (projectile.collide(asteroid)) {
                    //record the number of death of each object
                    projectile.setNumOfDeath(1);
                    asteroid.setNumOfDeath(1);
                    //set the status of objects which occurs collision is false
                    //it mean that the status objects marked as false should be deleted
                    projectile.setIsALive(false);
                    asteroid.setIsALive(false);
                }
            });
            //check projectiles and aliens
            aliens.forEach(alien -> {
                if (projectile.collide(alien)){
                    projectile.setNumOfDeath(1);
                    alien.setNumOfDeath(1);
                    //set the status of objects which occurs collision is false
                    //it mean that the status objects marked as false should be deleted
                    projectile.setIsALive(false);
                    alien.setIsALive(false);
                }
            });
        });

        alienProjectiles.forEach(alienProjectile -> {
            try {
                checkCollisionOfShip(alienProjectile);
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
                    if(asteroid.getSize() != 1){
                        int temp = 0;
                        while (temp<2){
                            Asteroid newAsteroid = new Asteroid(
                                    (int) asteroid.getShape().getTranslateX() + random.nextInt(-10,10),
                                    (int) asteroid.getShape().getTranslateY() + random.nextInt(-10,10),
                                    asteroid.getSize() - 1,asteroid.getSpeedTimes() - 1);
                            if(!newAsteroid.collide(ship)){
                                smallerAsteroids.add(newAsteroid);
                                pane.getChildren().add(newAsteroid.getShape());
                                temp+=1;
                            }
                        }
                    }
                });
        smallerAsteroids.forEach(smallerAsteroid ->
        {
            smallerAsteroid.move();
        });
        return smallerAsteroids;
    }

    //make all asteroids can move
    private void moveObjects() {
        asteroids.forEach(asteroid -> asteroid.move());
        aliens.forEach(alien -> alien.move());
    }

    public static void main(String[] args) {
        launch(args);
    }
}