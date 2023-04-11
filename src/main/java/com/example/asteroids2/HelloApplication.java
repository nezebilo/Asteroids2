package com.example.asteroids2;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        Group root = new Group();
        Scene scene = new Scene(root, 600, 600, Color.BLACK);

        EnemyShip ufo = new EnemyShip(50, 50);
        root.getChildren().add(ufo.getBody());

        new AnimationTimer() {

            @Override
            public void handle(long now) {
                ufo.move();
            }
        }.start();

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}