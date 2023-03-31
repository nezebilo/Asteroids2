package com.example.asteroids2;


import javafx.geometry.Point2D;

public class PlayerShip extends FlyingObject {
    // todo: thrust; invincible period after respawning;
    private int HP;

    public PlayerShip(int positionX, int positionY) {
        super(positionX, positionY,
                shipCorners(),
                270, // points straight up
                0, // when first created the player ship shouldn't move
                Team.PLAYER);
        this.HP = 3;
    }

    private static int[][] shipCorners() {
        // this method defines the shape of the player ship
        // relative to the central point (TranslateX, TranslateY)
        // FACING POSITIVE X-AXIS
        // it will be flipped 270 degrees to face up toward positive y-axis

        return new int[][]{
                {10, 0}, // top corner
                {-10, -10}, // bottom left corner
                {-5, 0},
                {-10, 10} // bottom right corner
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
}