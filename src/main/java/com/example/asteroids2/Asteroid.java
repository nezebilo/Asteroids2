package com.example.asteroids2;

import javafx.scene.shape.Polygon;

import java.util.Random;

public class Asteroid extends Role{
    private double rotationalMovement;
    public Asteroid(int x, int y) {
        super(createPolygon(), x, y);

        Random rnd = new Random();

        super.getShape().setRotate(rnd.nextInt(360));

        int accelerationAmount = 1 + rnd.nextInt(10);
        for (int i = 0; i < accelerationAmount; i++) {
            accelerate();
        }

        this.rotationalMovement = 0.5 - rnd.nextDouble();
    }

    public void accelerate() {
        double changeX = Math.cos(Math.toRadians(this.shape.getRotate()));
        double changeY = Math.sin(Math.toRadians(this.shape.getRotate()));

        changeX *= 0.1;
        changeY *= 0.1;

        this.movement = this.movement.add(changeX, changeY);
    }

    public static Polygon createPolygon() {
        Random rnd = new Random();

        double size = 10 + rnd.nextInt(10);

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
}
