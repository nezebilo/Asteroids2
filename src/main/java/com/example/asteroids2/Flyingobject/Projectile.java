package com.example.asteroids2.Flyingobject;

import javafx.scene.shape.Polygon;

public class Projectile extends FlyingObject {
    public Projectile(int x, int y) {
        super(new Polygon(2, -2, 2, 2, -2, 2, -2, -2), x, y);
    }

    public void accelerate() {
        double changeX = Math.cos(Math.toRadians(this.shape.getRotate()));
        double changeY = Math.sin(Math.toRadians(this.shape.getRotate()));

        changeX *= 10;
        changeY *= 10;

        this.movement = this.movement.add(changeX, changeY);
    }
}
