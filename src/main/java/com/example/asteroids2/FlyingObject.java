package com.example.asteroids2;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class FlyingObject {
    private final Polygon body; // shape of object: facing the positive X-Axis
    private Point2D movementPerFrame; // momentum vector: speed and direction
    private final Team team; // 3 teams; see enum class "Team"

    public FlyingObject(Polygon body, double speed, Team team) {
        this.body = body;
        this.movementPerFrame = createMovement(speed);
        this.team = team;
    }

    public FlyingObject(int positionX, int positionY,
                        int[][] cornerCoordinates,
                        int angleWithXAxis, int speed, Team team) {
        this(
                createShape(positionX, positionY, cornerCoordinates, angleWithXAxis),
                speed, team
        );
    }

    private static Polygon createShape(int positionX, int positionY, int[][] cornersCoordinates, int angleWithXAxis) {
        // note that the shape created here should face positive x-axis;
        // the rotation will later be done with body.setRotate() below

        // angleWithXAxis [0, 360) is the clock-wise angle between
        // this object's movement direction
        // and the positive x-axis
        // so if the movement direction is straight up: direction angle would be = 270

        // cornersCoordinates can be provided by each subclass - see PlayerShip class
        // which is a 2-D array: Double[corners][2]
        // i.e. cornersCoordinates.length = number of corners this polygon has

        if (cornersCoordinates[0].length != 2 ||
                angleWithXAxis < 0 || angleWithXAxis >= 360)
            throw new IllegalArgumentException();

        Polygon body = new Polygon();
        // set the position and rotate of this object's body
        body.setTranslateX(positionX);
        body.setTranslateY(positionY);
        // e.g. flips playerShip from facing right to facing up;
        // necessary for correct implementation of move(), turn() and applyThrust()
        body.setRotate(angleWithXAxis);

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

    private Point2D createMovement(double speed) {
        // get the direction that this object is pointing at (in radians)
        double bodyRadians = this.getBody().getRotate();

        // deconstruct the direction into length along X and Y axes
        double x = speed * Math.cos(bodyRadians);
        double y = speed * Math.sin(bodyRadians);
        return new Point2D(x, y);
    }

    protected Point2D getMovementPerFrame() { // defined for ships which would change direction
        return this.movementPerFrame;
    }

    protected void setMovementPerFrame(Point2D movementPerFrame) { // defined for ships which would change direction
        this.movementPerFrame = movementPerFrame;
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