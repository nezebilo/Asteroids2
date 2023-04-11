package com.example.asteroids2;

import javafx.scene.transform.Scale;

import java.util.List;

public class EnemyShip extends FlyingObject {
    public EnemyShip(int positionX, int positionY) {
        super(positionX, positionY,
                enemyShipCorners(),
                0,
                2,
                Team.ENEMY
        );
        Scale scale = new Scale();
        scale.setX(0.5);
        scale.setY(0.5);
        scale.setPivotX(this.getBody().getBoundsInParent().getCenterX());
        scale.setPivotY(this.getBody().getBoundsInParent().getCenterY());

        this.getBody().getTransforms().addAll(scale);
    }

    private static int[][] enemyShipCorners() {
        return new int[][]{
                {15, -20}, // head base right
                {10, -40}, // head top right
                {-10, -40}, // head top left
                {-15, -20}, // head base left
                {-25, -20}, // top left
                {25, -20}, // rop right
                {40, 0}, // center right
                {25, 20}, // bottom right
                {-25, 20}, // bottom left
                {-40, 0}, // center left
                {40, 0}, // center right
                {-40, 0}, // center left
                {-25, -20} // top left
        };
    }

    @Override
    public void move() {
        super.move();
    }

    @Override
    public List<FlyingObject> collideAction() {
        // no flying object created after collision
        return null;
    }
}
