package com.example.asteroids2;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class Bullet {
    protected Polygon shape;
    protected Point2D movement;

    protected boolean isAlive;

    protected int numOfDeath;

    protected double createTime;

    public Bullet(int x, int y) {
        this.shape = new Polygon(2, -2, 2, 2, -2, 2, -2, -2);
        this.shape.setTranslateX(x);
        this.shape.setTranslateY(y);
        //default isAlive is true.
        this.isAlive = true;
        //it means that once the object is destroyed, the numOfDeath of object will plus 1.
        this.numOfDeath = 0;
        this.movement = new Point2D(0, 0);
        this.createTime = System.currentTimeMillis();
    }

    public Polygon getShape() {
        return shape;
    }

    public void turnLeft() {
        this.shape.setRotate(this.shape.getRotate() - 5);
    }

    public void turnRight() {
        this.shape.setRotate(this.shape.getRotate() + 5);
    }

    public void move() {
        this.shape.setTranslateX(this.shape.getTranslateX() + this.movement.getX());
        this.shape.setTranslateY(this.shape.getTranslateY() + this.movement.getY());

        if (this.shape.getTranslateX() < 0) {
            this.shape.setTranslateX(this.shape.getTranslateX() + Main.WIDTH);
        }

        if (this.shape.getTranslateX() > Main.WIDTH) {
            this.shape.setTranslateX(this.shape.getTranslateX() % Main.WIDTH);
        }

        if (this.shape.getTranslateY() < 0) {
            this.shape.setTranslateY(this.shape.getTranslateY() + Main.HEIGHT);
        }

        if (this.shape.getTranslateY() > Main.HEIGHT) {
            this.shape.setTranslateY(this.shape.getTranslateY() % Main.HEIGHT);
        }
    }

    public void accelerate() {
        double changeX = Math.cos(Math.toRadians(this.shape.getRotate()));
        double changeY = Math.sin(Math.toRadians(this.shape.getRotate()));

        changeX *= 0.01;
        changeY *= 0.01;

        this.movement = this.movement.add(changeX, changeY);
    }

    public Point2D getMovement() {
        return movement;
    }

    public void setMovement(Point2D movement) {
        this.movement = movement;
    }

    public boolean getIsALive() {
        return isAlive;
    }

    public void setIsALive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public boolean collide(Bullet other) {
        Shape collisionArea = Shape.intersect(this.shape, other.getShape());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }

    public int getNumOfDeath() {
        return numOfDeath;
    }

    public void setNumOfDeath(int numOfDeath) {
        this.numOfDeath = numOfDeath;
    }

    public double getCreateTime() {
        return createTime;
    }

    public void setCreateTime(double createTime) {
        this.createTime = createTime;
    }
}
