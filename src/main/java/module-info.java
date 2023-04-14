module com.example.asteroids2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens com.example.asteroids2 to javafx.fxml;
    exports com.example.asteroids2;
    exports com.example.asteroids2.Flyingobject;
    opens com.example.asteroids2.Flyingobject to javafx.fxml;
    exports com.example.asteroids2.ConstantVar;
    opens com.example.asteroids2.ConstantVar to javafx.fxml;
}