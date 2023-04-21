package com.example.asteroids2.MenuUi;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.function.Consumer;


public class LayoutElement {
    private static final Font customFont;

    static {
        try {
            customFont = Font.loadFont(new FileInputStream
                    ("src/main/resources/imageAndFont/Roboto-BoldItalic.ttf"), 18);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Button getQuitBtn(Stage primaryStage) {
        Button quitBtn = new Button("Quit Game");
        quitBtn.setFont(customFont);
        quitBtn.setOnAction(e -> primaryStage.close());
        return quitBtn;
    }

    public static Button getQuitGameBtn(Stage primaryStage, Pane pauseMenuPane) {
        Button quitGameBtn = new Button("Quit Game");
        quitGameBtn.setFont(customFont);

        quitGameBtn.setOnAction(e ->  primaryStage.close());
        quitGameBtn.setLayoutX((pauseMenuPane.getPrefWidth() - quitGameBtn.getWidth()) / 3);
        quitGameBtn.setLayoutY((pauseMenuPane.getPrefHeight() - quitGameBtn.getHeight()) / 1.8);
        return quitGameBtn;
    }

    public static Button getMainMenuBtn(Stage primaryStage, Pane gameOverPane, Pane pane, Scene mainMenuScene) {
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
        Image backgroundImg = new Image(Objects.requireNonNull(LayoutElement.class.getResourceAsStream(name)));
        ImageView backgroundView = new ImageView(backgroundImg);
        backgroundView.setFitWidth(v);
        backgroundView.setFitHeight(v1);
        return backgroundView;
    }

    public static Button getMenuBtn(StackPane root, StackPane highScoreContainer) {
        Button mainMenuBtn = new Button("Back to Main Menu");
        mainMenuBtn.setFont(customFont);
        mainMenuBtn.setOnAction(event -> root.getChildren().remove(highScoreContainer));
        mainMenuBtn.setLayoutX(110);
        mainMenuBtn.setLayoutY(250);
        return mainMenuBtn;
    }

    public static Text getGameTitleText() {
        Text gameTitleText = new Text("Asteroids");
        gameTitleText.setFont(Font.font("Arial", FontWeight.BOLD, 50));
        gameTitleText.setFill(Color.BLACK);
        return gameTitleText;
    }

    public static Text playInstructionSetting() {
        Text platingInstruction = new Text("""
                                           How To Control:
                         UP: Acceleration    DOWN: Deceleration
                               LEFT & RIGHT: Rotate   B: Brake
                     SPACE: Fire   J: Hyperspace Jump   ESC: Pause\
                """);
        platingInstruction.setFill(Color.WHITE);
        return platingInstruction;
    }

    public static Button getStartButton(Stage primaryStage, Consumer<Stage> function) {
        Button startBtn = new Button("Start Game");
        startBtn.setFont(customFont);
        startBtn.setOnAction(e -> function.accept(primaryStage));

        return startBtn;
    }

    public static Button getHignScoreButton(Consumer<Stage> function) {
        Button highScoreBtn = new Button("High Score List");
        highScoreBtn.setFont(customFont);
        // add event to highscore button
        highScoreBtn.setOnAction(e -> function.accept(null));
        return highScoreBtn;
    }

    public static Button getResumeBtn(AnimationTimer getAnimationTimer,
                                      Consumer<Stage> function,
                                      Pane pauseMenuPane) {
        Button resumeBtn = new Button("Resume Game");
        resumeBtn.setFont(customFont);
        resumeBtn.setOnAction(e -> {
            function.accept(null);
            getAnimationTimer.start();
        });
        resumeBtn.setLayoutX((pauseMenuPane.getPrefWidth() - resumeBtn.getWidth()) / 3.5);
        resumeBtn.setLayoutY((pauseMenuPane.getPrefHeight() - resumeBtn.getHeight()) / 4);
        return resumeBtn;
    }

    public static Button getRestartBtn(Stage primaryStage, Consumer<Stage> function) {
        Button restartBtn = new Button("Restart");
        restartBtn.setFont(customFont);
        restartBtn.setOnAction(e -> function.accept(primaryStage));
        restartBtn.setLayoutX(135);
        restartBtn.setLayoutY(125);
        return restartBtn;
    }
}
