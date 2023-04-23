package com.example.asteroids2.Flyingobject;

import javafx.scene.shape.Polygon;


public class Ship extends FlyingObject {
    protected boolean invincibility;

    public Ship(int x, int y) {
        super(new Polygon(
                -10, -10,
                20, 0,
                -10, 10,
                -5, 0,
                -10, -10), x, y);
        //In a game, players have two chances to revive.
        this.setRotate();
        this.invincibility = false;
        this.setLives(3);
    }

    public void setRotate() {
        this.shape.setRotate(this.shape.getRotate() + 270);
    }

    public void thrust(int x, int y) {
        this.shape.setTranslateX(x);
        this.shape.setTranslateY(y);
    }

    public boolean isInvincibility() {
        return invincibility;
    }

    public void setInvincibility(boolean invincibility) {
        this.invincibility = invincibility;
    }

    public void decelerate() {
        double changeX = Math.cos(Math.toRadians(this.shape.getRotate()));
        double changeY = Math.sin(Math.toRadians(this.shape.getRotate()));

        changeX *= 0.005;
        changeY *= 0.005;

        this.movement = this.movement.subtract(changeX, changeY);
    }

}
