package com.example.asteroids2;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameStart extends Application {
    public static int WIDTH = 600;
    public static int HEIGHT = 600;

    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = new Pane();
        pane.setPrefSize(WIDTH, HEIGHT);

        PlayerShip playerShip = new PlayerShip(WIDTH / 2, HEIGHT / 2);
        pane.getChildren().add(playerShip.getBody());

        new AnimationTimer() {
            @Override
            public void handle(long l) {
                playerShip.move();
            }
        }.start();

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }
}
