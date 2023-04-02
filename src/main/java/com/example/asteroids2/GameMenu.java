package com.example.asteroids2;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameMenu extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // Create the "Start Game" button
        Button startBtn = new Button("Start Game");
        startBtn.setOnAction(e -> {
            // Code to start the game goes here
        });

        // Create the "Quit Game" button
        Button quitBtn = new Button("Quit Game");
        quitBtn.setOnAction(e -> primaryStage.close());

        // Create the main menu layout
        VBox mainMenuLayout = new VBox(20, startBtn, quitBtn);
        mainMenuLayout.setAlignment(Pos.CENTER);

        // Create the main menu scene
        Scene mainMenuScene = new Scene(mainMenuLayout, 400, 300);

        // Set the stage's title and scene
        primaryStage.setTitle("Game Title");
        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }
}