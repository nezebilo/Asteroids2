package com.example.asteroids2;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.net.URL;
import java.util.ResourceBundle;

public class UFO {

    private Double xpos;
    private Double ypos;
    private Polygon body;

    public UFO() {
        xpos = 300.0;
        ypos = 300.0;
        body = new Polygon();
        drawUFO();
    }

    public void drawUFO() {

        body.getPoints().addAll(
                // head base right
                xpos + 15, ypos - 20,
                // head top right
                xpos + 10, ypos - 40,
                // head top left
                xpos - 10, ypos - 40,
                // head base left
                xpos - 15, ypos - 20,
                // top left
                xpos - 25, ypos - 20,
                // top right
                xpos + 25, ypos - 20,
                // centre right
                xpos + 40, ypos,
                // bottom right
                xpos + 25, ypos + 20,
                // bottom left
                xpos - 25, ypos + 20,
                // centre left
                xpos - 40, ypos,
                // centre right
                xpos + 40, ypos,
                // centre left
                xpos - 40, ypos,
                // top left
                xpos - 25, ypos - 20
        );
        body.setStroke(Color.WHITE);
    }

    public Polygon getBody() {
        return body;
    }

    public void move() {
        xpos += 1;
        this.drawUFO();
    }
}
