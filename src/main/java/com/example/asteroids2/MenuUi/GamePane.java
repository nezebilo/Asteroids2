package com.example.asteroids2.MenuUi;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GamePane {
    private final Pane pane;
    private final StackPane container;
    private final Pane tempPane;

    private final ImageView backgroundView;

    public GamePane(int x, int y,String link){
        backgroundView = LayoutElement.getBackgroundView(link, x, y);
        pane = new Pane();
        container = new StackPane();
        tempPane = new StackPane();
        pane.setPrefSize(x, y);
    }
    public GamePane(int x, int y , String link, Pane parentPane){
        backgroundView = LayoutElement.getBackgroundView(link, x, y);
        pane = new Pane();
        container = new StackPane();
        tempPane = new StackPane();

        pane.setPrefSize(x, y);
        pane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-border-color: white; -fx-border-width: 2;");

        pane.setLayoutX((parentPane.getPrefWidth() - pane.getPrefWidth()) / 2);
        pane.setLayoutY((parentPane.getPrefHeight() - pane.getPrefHeight()) / 2);

        container.getChildren().addAll(backgroundView, pane);
        container.setLayoutX(pane.getLayoutX());
        container.setLayoutY(pane.getLayoutY());
        parentPane.getChildren().add(container);
    }

    public void setPosition(int x, int y){
        pane.setLayoutX(x);
        pane.setLayoutY(y);
    }

    public void setBg(Pane parentPane){
        container.getChildren().addAll(backgroundView, pane);
        container.setLayoutX(parentPane.getLayoutX());
        container.setLayoutY(parentPane.getLayoutY());
        parentPane.getChildren().add(container);
    }

    public void setBg(){
        tempPane.getChildren().addAll(backgroundView, pane);
    }

    public void setBg(VBox vBox){
        container.getChildren().addAll(backgroundView, vBox);
    }

    public Pane getPane() {
        return pane;
    }

    public Pane getTempPane() {
        return tempPane;
    }

    public StackPane getContainer() {
        return container;
    }

    public void addElement(Node node){
        pane.getChildren().add(node);
    }

}
