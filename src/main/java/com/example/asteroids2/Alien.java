package com.example.asteroids2;

import javafx.css.Size;
import javafx.scene.shape.Polygon;

import java.util.Random;

public class Alien extends Group{
    public Alien(int x, int y, int num) {
        super(new Polygon(-30.0, 0.0,
                -8.0,-8.0,
                -5.0,-12.0,
                5.0,-12.0,
                8.0,-8.0,
                30.0,0.0,
                8.0,8.0,
                -8.0,8.0,
                -30.0,0.0), x, y);

        //control the speed of a created asteroid
        getAccelerationAmount(num);
    }

    public void getAccelerationAmount(int num) {
        Random random =new Random();
        double changeX = random.nextDouble(-1,1);
        double changeY = random.nextDouble(-1,1);

        for (int i = 0; i < num; i++) {
            changeX *= 0.1;
            changeY *= 0.1;
            this.movement = this.movement.add(changeX, changeY);
        }
    }


}
