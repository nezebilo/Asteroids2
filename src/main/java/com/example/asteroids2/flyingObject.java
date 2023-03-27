package com.example.asteroids2;

import javafx.scene.shape.Polygon;

public class flyingObject {
    private boolean isEnemy; // team of object
    private Polygon shape; // shape of object

    public flyingObject(Double[] position, Double[][] vertexOffsets, boolean isEnemy) {
        // check for illegal arguments:
        if (position.length != 2 || vertexOffsets[0].length != 2)
            throw new IllegalArgumentException();

        // assign team
        this.isEnemy = isEnemy;

        // each sub-class will provide, in their own constructor, vertexOffsets
        // which is a 2-D array: Double[vertices][2]
        // and position Double[2] will be provided at each object's creation
        int vertices = vertexOffsets.length;
        Double[] vertices_coordinates = new Double[2 * vertices]; // x & y for each vertex
        Double centerX = position[0], centerY = position[1];

        // populate the vertices_coordinates array from position and vertexOffsets
        // and create the shape from it
        int coordinate_index = 0;
        for (Double[] offsets : vertexOffsets) {
            vertices_coordinates[coordinate_index++] = centerX + offsets[0];
            vertices_coordinates[coordinate_index++] = centerY + offsets[1];
        }

        this.shape = new Polygon();
        this.shape.getPoints().addAll(vertices_coordinates);
    }

}


