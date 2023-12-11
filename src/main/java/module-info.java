module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires  javafx.graphics;


    opens sample to javafx.fxml;
    exports sample;
}