package com.example.asteroids2.Flyingobject;

import javafx.scene.input.KeyCode;
import javafx.scene.shape.Polygon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ship extends FlyingObject {
    protected boolean invincibility;

    public Ship(int x, int y) {
        super(new Polygon(-10, -10, 20, 0, -10, 10), x, y);
        //In a game, players have two chances to revive.
        this.setRotate();
        this.invincibility = false;
    }

    public void setRotate(){
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

}
