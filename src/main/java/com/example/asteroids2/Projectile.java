package com.example.asteroids2;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

import java.util.List;

public class Projectile extends FlyingObject{
    private Polygon projectile;
    private Point2D movement;

    public Projectile(int x, int y) {
        this.projectile = new Polygon(2, -2, 2, 2, -2, 2, -2, -2);
        this.projectile.setTranslateX(x);
        this.projectile.setTranslateY(y);

        this.movement = new Point2D(0, 0);
        this.movementPerFrame = new Point2D(0, 0);
        this.body = createShape(positionX, positionY, new int[][]{
                {1, 0}, // top corner
                {-1, -1}, // bottom left corner
                {-1 / 2, 0},
                {-1, 1} // bottom right corner
        }, 0);
    }


    public Polygon getProjectile() {
        return projectile;
    }

    public void move() {
        this.projectile.setTranslateX(this.projectile.getTranslateX() + this.movement.getX());
        this.projectile.setTranslateY(this.projectile.getTranslateY() + this.movement.getY());
    }

    @Override
    public List<FlyingObject> collideAction() {
        return null;
    }

    public void accelerate() {
        double changeX = Math.cos(Math.toRadians(this.projectile.getRotate()));
        double changeY = Math.sin(Math.toRadians(this.projectile.getRotate()));

        changeX *= 10;
        changeY *= 10;

        this.movement = this.movement.add(changeX, changeY);
    }

    public void setProjectile(Polygon projectile) {
        this.projectile = projectile;
    }

    public Point2D getMovement() {
        return movement;
    }

    public void setMovement(Point2D movement) {
        this.movement = movement;
    }
}
