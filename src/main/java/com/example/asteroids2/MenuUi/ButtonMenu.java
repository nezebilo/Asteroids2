package com.example.asteroids2.MenuUi;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class ButtonMenu {

    public static Button getQuitBtn(Stage primaryStage, Font customFont) {
        Button quitBtn = new Button("Quit Game");
        quitBtn.setFont(customFont);
        quitBtn.setOnAction(e -> primaryStage.close());
        return quitBtn;
    }


    public static Button getQuitGameBtn(Stage primaryStage, Font customFont, Pane pauseMenuPane) {
        Button quitGameBtn = new Button("Quit Game");
        quitGameBtn.setFont(customFont);

        quitGameBtn.setOnAction(e -> {
            primaryStage.close();
        });
        quitGameBtn.setLayoutX((pauseMenuPane.getPrefWidth() - quitGameBtn.getWidth()) / 3);
        quitGameBtn.setLayoutY((pauseMenuPane.getPrefHeight() - quitGameBtn.getHeight()) / 1.8);
        return quitGameBtn;
    }


    public static Button getMainMenuBtn(Stage primaryStage, Pane gameOverPane, Font customFont, Pane pane, Scene mainMenuScene) {
        Button mainMenuBtn = new Button("Back to Main Menu");
        mainMenuBtn.setFont(customFont);
        mainMenuBtn.setOnAction(e -> {
            pane.getChildren().remove(gameOverPane);
            primaryStage.setScene(mainMenuScene);
            primaryStage.show();
        });
        mainMenuBtn.setLayoutX(90);
        mainMenuBtn.setLayoutY(180);
        return mainMenuBtn;
    }


    public static ImageView getBackgroundView(String name, int v, int v1) {
        Image backgroundImg = new Image(Objects.requireNonNull(ButtonMenu.class.getResourceAsStream(name)));
        ImageView backgroundView = new ImageView(backgroundImg);
        backgroundView.setFitWidth(v);
        backgroundView.setFitHeight(v1);
        return backgroundView;
    }

    public static Button getMenuBtn(Font customFont, StackPane root, StackPane highScoreContainer) {
        Button mainMenuBtn = new Button("Back to Main Menu");
        mainMenuBtn.setFont(customFont);
        mainMenuBtn.setOnAction(event -> {
            root.getChildren().remove(highScoreContainer);
        });
        mainMenuBtn.setLayoutX(110);
        mainMenuBtn.setLayoutY(250);
        return mainMenuBtn;
    }

}
