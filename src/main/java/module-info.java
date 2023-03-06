module com.example.asteroids2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens com.example.asteroids2 to javafx.fxml;
    exports com.example.asteroids2;
}