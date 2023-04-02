package com.example.asteroids2;


import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerShip extends FlyingObject {
    // todo: thrust; invincible period after respawning;
    private int HP;
    private int respawnX, respawnY;

    public PlayerShip(int positionX, int positionY, int HP) {
        super(positionX, positionY,
                shipCorners(),
                270, // points straight up
                0, // when first created the player ship shouldn't move
                Team.PLAYER);
        this.HP = HP;
        respawnX = positionX;
        respawnY = positionY;
    }

    public PlayerShip(int HP) { // create by default at center of screen
        this(GameStart.WIDTH / 2,
                GameStart.HEIGHT / 2,
                HP);
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
        double incrementalSpeed = 0.05;
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
        // has remaining health: return list of a single playership
        // todo: implement respawn invincibility
        assert !this.isAlive(); // this method should only be called if this ship is not alive
        if (HP > 0) {
            return new ArrayList<>(
                    Collections.singletonList(
                            new PlayerShip(respawnX, respawnY, HP - 1)
                    )
            );
        } else return null;
    }
}