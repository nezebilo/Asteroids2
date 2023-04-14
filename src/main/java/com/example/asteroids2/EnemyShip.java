package com.example.asteroids2;

import javafx.geometry.Point2D;
import javafx.scene.transform.Scale;

import java.util.List;
public class EnemyShip extends FlyingObject {

    // timer is used to create zig-zag movement
    private int timer;
    // interval is how long between changing vertical direction
    private static int interval = 30;
    // up tells whether to zag up or down
    private boolean up = true;
    public EnemyShip(int positionX, int positionY) {
        super(positionX, positionY,
                enemyShipCorners(),
                0,
                2,
                Team.ENEMY
        );

        // scale the body to correct size
        Scale scale = new Scale();
        scale.setX(0.5);
        scale.setY(0.5);
        scale.setPivotX(this.getBody().getBoundsInParent().getCenterX());
        scale.setPivotY(this.getBody().getBoundsInParent().getCenterY());

        this.getBody().getTransforms().addAll(scale);

        // set timer to 0
        this.timer = 0;

        Point2D movement = this.getMovementPerFrame();
        this.setMovementPerFrame(new Point2D(movement.getX(), 1));
    }

    private static int[][] enemyShipCorners() {
        return new int[][]{
                {15, -20}, // head base right
                {10, -40}, // head top right
                {-10, -40}, // head top left
                {-15, -20}, // head base left
                {-25, -20}, // top left
                {25, -20}, // rop right
                {40, 0}, // center right
                {25, 20}, // bottom right
                {-25, 20}, // bottom left
                {-40, 0}, // center left
                {40, 0}, // center right
                {-40, 0}, // center left
                {-25, -20} // top left
        };
    }

    @Override
    public void move() {
        super.move();

        if (this.timer >= this.interval) {
            if (this.up) {
                Point2D movement = this.getMovementPerFrame();
                this.setMovementPerFrame(new Point2D(movement.getX(), -1));
                this.up = false;
            }
            else {
                Point2D movement = this.getMovementPerFrame();
                this.setMovementPerFrame(new Point2D(movement.getX(), 1));
                this.up = true;
            }
            this.timer = 0;
        }

        this.timer++;
    }

    @Override
    public List<FlyingObject> collideAction() {
        // no flying object created after collision
        return null;
    }
}
