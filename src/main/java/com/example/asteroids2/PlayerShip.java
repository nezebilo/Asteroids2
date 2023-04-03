package com.example.asteroids2;


import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerShip extends FlyingObject {
    private final static int INVINCIBILITY_WINDOW_IN_SECONDS = 3;
    // todo: fire bullet; hyperspace jump
    private final int remainingLives;
    private final int respawnX, respawnY;
    private final boolean respawn;
    private final LocalDateTime createTime;

//    private static int thrust =


    public PlayerShip(int positionX, int positionY, int remainingLives, boolean respawn) {
        super(positionX, positionY,
                shipCorners(),
                270, // points straight up
                0, // when first created the player ship shouldn't move
                Team.PLAYER);
        this.remainingLives = remainingLives;
        respawnX = positionX;
        respawnY = positionY;
        this.respawn = respawn;
        this.createTime = LocalDateTime.now();

    }

    public PlayerShip(int remainingLives, boolean respawn) { // create by default at center of screen
        this(GameStart.WIDTH / 2,
                GameStart.HEIGHT / 2,
                remainingLives,
                respawn);
    }

    public PlayerShip(int remainingLives) {
        this(remainingLives, false);
    }

    public boolean isInvincible() {
        if (!respawn) return false;
        Duration timeAfterRespawn = Duration.between(createTime, LocalDateTime.now());
        return timeAfterRespawn.toSeconds() < INVINCIBILITY_WINDOW_IN_SECONDS;
    }

    private static int[][] shipCorners() {
        // this method defines the shape of the player ship
        // relative to the central point (TranslateX, TranslateY)
        // FACING POSITIVE X-AXIS
        // it will be flipped 270 degrees to face up toward positive y-axis

        int size = 10;
        return new int[][]{
                {size, 0}, // top corner
                {-size, -size}, // bottom left corner
                {-size / 2, 0},
                {-size, size} // bottom right corner
        };
    }

    public void turn(boolean turnsLeft) {
        double curRotate = this.getBody().getRotate();
        double update = turnsLeft ? -5 : 5;
        this.getBody().setRotate(curRotate + update);
    }

    public void applyThrust() {
        double incrementalSpeed = 0.01;
        // get the radians of the angle the ship is pointing at
        double radians = Math.toRadians(this.getBody().getRotate());
        // deconstruct the incremental speed into x & y axes
        double x_change = incrementalSpeed * Math.cos(radians);
        double y_change = incrementalSpeed * Math.sin(radians);
        // add the incremental speed to the current movement per frame
        Point2D newMovement = this.getMovementPerFrame().add(x_change, y_change);
        // apply change
        this.setMovementPerFrame(newMovement);
    }

    @Override
    public List<FlyingObject> collideAction() {
        // has remaining health: return list of a single player ship
        // todo: implement respawn invincibility
        assert !this.isAlive(); // this method should only be called if this ship is not alive
        if (remainingLives > 1) {
            return new ArrayList<>(
                    Collections.singletonList(
                            new PlayerShip(respawnX, respawnY, remainingLives - 1, true)
                    )
            );
        } else return null;
    }

    @Override
    public void move() {
        if (this.isInvincible())
            this.getBody().setFill(Color.RED);
        else this.getBody().setFill(Color.BLACK);
        super.move();
    }
}