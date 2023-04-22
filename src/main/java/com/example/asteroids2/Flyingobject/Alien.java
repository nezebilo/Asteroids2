package com.example.asteroids2.Flyingobject;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

import java.util.Random;

public class Alien extends FlyingObject {

    // timer is used to create zig-zag movement
    private int timer;
    // interval is how long between changing vertical direction
    private static final int INTERVAL = 100;

    // zig movement
    private Point2D zig;

    // zag movement
    private Point2D zag;
    // zigOrZag tells whether to zig or zag
    private boolean zigOrZag = true;

    public Alien(int x, int y, int speedTimes) {
        super(new Polygon(-30.0, 0.0,
                30,0,
                8,8,
                -8,8,
                -30,0,
                -8,-8,
                -5,-12,
                5,-12,
                8,-8,
                -8,-8,
                8,-8,
                30,0), x, y);

        //control the speed of a created alien
        getAccelerationAmount(speedTimes);
        // set timer to 0
        this.timer = 0;
    }

    public void getAccelerationAmount(int num) {
        Random random =new Random();
        double changeX = random.nextDouble(-1,1);
        double changeY = random.nextDouble(-1,1);

        for (int i = 0; i < num; i++) {
            changeX *= 0.6;
            changeY *= 0.6;
            this.movement = this.movement.add(changeX, changeY);
        }

        // set zig and zag movement
        this.zig = new Point2D(- movement.getX() * 0.75, movement.getY());
        this.zag = new Point2D(movement.getX() * 1.5, 0);
    }

    @Override
    public void move() {
        super.move();

        // if interval is exceeded we change the movement
        if (this.timer >= INTERVAL) {
            if (this.zigOrZag) {
                this.setMovement(this.zig);
                this.zigOrZag = false;
            }
            else {
                this.setMovement(this.zag);
                this.zigOrZag = true;
            }
            // reset the timer
            this.timer = 0;
        }

        // increment the timer
        this.timer++;
    }



}
