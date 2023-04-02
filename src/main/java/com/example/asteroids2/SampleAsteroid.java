package com.example.asteroids2;

import java.util.List;
import java.util.Random;

public class SampleAsteroid extends FlyingObject {
    public SampleAsteroid() {
        super(randomPosition(true),
                randomPosition(false),
                asteroidCorners(),
                new Random().nextInt(0, 360),
                0.5,
                Team.ASTEROID
        );
    }

    private static int randomPosition(boolean isX) {
        // place the asteroid in a random position within the top quarter of the pane
        int min, max;
        if (isX) {
            // anywhere on the x-axis
            min = 0;
            max = GameStart.WIDTH;
        } else { // is y
            // place in the top quarter:
            min = GameStart.HEIGHT * 3 / 4;
            max = GameStart.HEIGHT;
        }
        Random random = new Random();
        return random.nextInt(min, max);
    }

    private static int[][] asteroidCorners() {
        int size = 40;
        int corners = 10;
        int[][] cornerCoordinates = new int[corners][2];
        Random random = new Random();
        for (int i = 0; i < corners; i++) {
            cornerCoordinates[i][0] = random.nextInt(-size, size);
            cornerCoordinates[i][1] = random.nextInt(-size, size);
        }
        return cornerCoordinates;
    }

    @Override
    public List<FlyingObject> collideAction() {
        return null;
    }
}
