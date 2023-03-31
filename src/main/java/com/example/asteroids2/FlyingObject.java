package com.example.asteroids2;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class FlyingObject {
    private final Polygon body; // body of object
    private Point2D movementPerFrame; // momentum vector: speed and direction
    private final Team team; // 3 teams; see enum class "Team"

    public FlyingObject(Polygon body, int directionAngleWithXAxis, int speed, Team team) {
        // check for illegal arguments:
        if (directionAngleWithXAxis < 0 || directionAngleWithXAxis >= 360)
            throw new IllegalArgumentException();
        this.body = body;
        // set movement per frame
        this.movementPerFrame = setMovement(directionAngleWithXAxis, speed);
        // assign team; only takes damage when different teams clash
        this.team = team;
    }

    public FlyingObject(int positionX, int positionY,
                        int[][] cornerOffsets,
                        int directionAngleWithXAxis, int speed, Team team) {
        this(
                createShape(positionX, positionY, cornerOffsets),
                directionAngleWithXAxis, speed, team);
    }

    private static Polygon createShape(int positionX, int positionY, int[][] cornersCoordinates) {
        // cornersCoordinates can be provided by each subclass - see PlayerShip class
        // which is a 2-D array: Double[corners][2]
        // i.e. cornersCoordinates.length = number of corners this polygon has

        if (cornersCoordinates[0].length != 2)
            throw new IllegalArgumentException();

        Polygon body = new Polygon();
        // set the position of this object's body
        body.setTranslateX(positionX);
        body.setTranslateY(positionY);

        int corners = cornersCoordinates.length;
        Double[] corner_coordinates = new Double[2 * corners]; // x & y for each corner

        // populate the corner_coordinates array from cornersCoordinates (flatten 2D into 1D)
        // and create the body from it
        int coordinate_index = 0;
        for (int[] XandYCoordinates : cornersCoordinates) {
            corner_coordinates[coordinate_index++] = (double) XandYCoordinates[0];
            corner_coordinates[coordinate_index++] = (double) XandYCoordinates[1];
        }
        body.getPoints().addAll(corner_coordinates);
        return body;
    }

    private Point2D setMovement(double angle, double speed) {
        // directionAngle [0, 360) is the clock-wise angle between
        // this object's movement direction
        // and the positive x-axis
        // so if the movement direction is straight up: direction angle would be = 270

        // deconstruct the direction into length along X and Y axes
        double x = speed * Math.cos(Math.toRadians(angle));
        double y = speed * Math.sin(Math.toRadians(angle));
        return new Point2D(x, y);
    }

    public Polygon getBody() {
        return this.body;
    }

    public void move() {
        // this method should be called in every frame
        // so this method just moves the object in the direction specified in this.movementPerFrame
        // most objects move consistently at one speed in one direction (except the ships)
        double newX = this.body.getTranslateX() + movementPerFrame.getX();
        double newY = this.body.getTranslateY() + movementPerFrame.getY();
        int paneWidth = GameStart.WIDTH, paneHeight = GameStart.HEIGHT;
        // prevent the flying object from falling off the pane
        if (newX < 0 || newX > paneWidth)
            newX = (newX + paneWidth) % paneWidth;
        if (newY < 0 || newY > paneHeight)
            newY = (newY + paneHeight) % paneHeight;

        // apply change in position
        this.body.setTranslateX(newX);
        this.body.setTranslateY(newY);
    }

    public boolean collide(FlyingObject other) {
        // only collide with object of different team
        if (other.team == this.team) return false;
        // detect intersection with object of different team
        Shape collisionArea = Shape.intersect(this.body, other.body);
        return collisionArea.getBoundsInLocal().getWidth() > 0;
    }
}