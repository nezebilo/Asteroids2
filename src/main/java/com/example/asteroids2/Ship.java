package com.example.asteroids2;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

public class Ship extends Role {

    public Ship(int x, int y) {
        super(new Polygon(-10, -10, 20, 0, -10, 10), x, y);
        //In a game, players have two chances to revive.
    }

    public void setRotate(){
        this.shape.setRotate(this.shape.getRotate() + 270);
    }
}
