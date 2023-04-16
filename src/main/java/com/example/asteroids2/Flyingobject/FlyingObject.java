package com.example.asteroids2.Flyingobject;

import com.example.asteroids2.Main;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public abstract class FlyingObject {
    protected Polygon shape;
    protected Point2D movement;
    protected boolean isAlive;

    protected int numOfDeath;

    protected double createTime;

    public FlyingObject(Polygon polygon, int x, int y) {
        this.shape = polygon;
        this.shape.setTranslateX(x);
        this.shape.setTranslateY(y);
        //default isAlive is true.
        this.isAlive = true;
        //it means that once the object is destroyed, the numOfDeath of object will plus 1.
        this.numOfDeath = 0;
        this.movement = new Point2D(0, 0);
        this.createTime = System.currentTimeMillis();
        this.getShape().setFill(Color.WHITE);
    }

    public Polygon getShape() {
        return shape;
    }

    public void turnLeft() {
        this.shape.setRotate(this.shape.getRotate() - 2);
    }

    public void turnRight() {
        this.shape.setRotate(this.shape.getRotate() + 2);
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

    public void accelerate(double coefficient) {
        double changeX = Math.cos(Math.toRadians(this.shape.getRotate()));
        double changeY = Math.sin(Math.toRadians(this.shape.getRotate()));

        changeX *= 0.01 * coefficient;
        changeY *= 0.01 * coefficient;

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

    public boolean collide(FlyingObject other) {
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

}
