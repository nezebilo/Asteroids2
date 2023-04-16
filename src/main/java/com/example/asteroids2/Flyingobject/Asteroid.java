package com.example.asteroids2.Flyingobject;

import javafx.scene.shape.Polygon;

import java.util.Random;

public class Asteroid extends FlyingObject {
    protected double rotationalMovement;
    private int accelerationAmount;

    //control the size of each asteroid
    protected int size;

    protected int speedTimes;
    public Asteroid(int x, int y,  int size, int speedTimes) {
        super(createPolygon(size), x, y);
        this.size=size;
        //Set it to rotate at a random angle
        Random rnd = new Random();
        super.getShape().setRotate(rnd.nextInt(360));

        //control the speed of a created asteroid
        //the size is bigger, the speed is lower
        if(size == 3){
            this.speedTimes = 1 + speedTimes;
            getAccelerationAmount(this.speedTimes);
        } else if (size == 2) {
            this.speedTimes = 2 + speedTimes;
            getAccelerationAmount(this.speedTimes);
        }else {
            this.speedTimes = 3 + speedTimes;
            getAccelerationAmount(this.speedTimes);
        }

        //Set it spin
        this.rotationalMovement = 0.5 - rnd.nextDouble();
    }

    public void accelerate() {
        double changeX = Math.cos(Math.toRadians(this.shape.getRotate()));
        double changeY = Math.sin(Math.toRadians(this.shape.getRotate()));

        changeX *= 0.1;
        changeY *= 0.1;

        this.movement = this.movement.add(changeX, changeY);
    }

    public static Polygon createPolygon(int times) {
        Random rnd = new Random();

        double size = 10*times + rnd.nextInt(10);

        Polygon polygon = new Polygon();
        double c1 = Math.cos(Math.PI * 2 / 5);
        double c2 = Math.cos(Math.PI / 5);
        double s1 = Math.sin(Math.PI * 2 / 5);
        double s2 = Math.sin(Math.PI * 4 / 5);

        polygon.getPoints().addAll(
                size, 0.0,
                size * c1, -1 * size * s1,
                -1 * size * c2, -1 * size * s2,
                -1 * size * c2, size * s2,
                size * c1, size * s1);

        for (int i = 0; i < polygon.getPoints().size(); i++) {
            int change = rnd.nextInt(5) - 2;
            polygon.getPoints().set(i, polygon.getPoints().get(i) + change);
        }

        return polygon;
    }

    @Override
    public void move() {
        super.move();
        super.getShape().setRotate(super.getShape().getRotate() + rotationalMovement);
    }

    public void getAccelerationAmount(int num) {
        this.accelerationAmount = num;
        for (int i = 0; i < accelerationAmount; i++) {
            accelerate();
        }
    }

    public double getRotationalMovement() {
        return rotationalMovement;
    }

    public void setRotationalMovement(double rotationalMovement) {
        this.rotationalMovement = rotationalMovement;
    }

    public int getAccelerationAmount() {
        return accelerationAmount;
    }

    public void setAccelerationAmount(int accelerationAmount) {
        this.accelerationAmount = accelerationAmount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSpeedTimes() {
        return speedTimes;
    }

    public void setSpeedTimes(int speedTimes) {
        this.speedTimes = speedTimes;
    }
}
