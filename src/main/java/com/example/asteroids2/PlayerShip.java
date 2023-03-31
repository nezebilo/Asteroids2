package com.example.asteroids2;

public class PlayerShip extends FlyingObject {
    // todo: thrust; invincible period after respawning;

    public PlayerShip(int positionX, int positionY) {
        super(positionX, positionY,
                getShipCornerOffsets(),
                270, // points straight up
                1, // todo: change to 0; when first created the player ship shouldn't move
                Team.PLAYER);
    }

    private static int[][] getShipCornerOffsets() {
        // this method defines the shape of the player ship
        // relative to the central point (positionX, positionY)

        return new int[][]{
                {0, -10}, // top corner
                {-10, 10}, // bottom left corner
                {0, 5},
                {10, 10} // bottom right corner
        };
    }
}