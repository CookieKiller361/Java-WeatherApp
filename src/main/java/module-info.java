module org.weatherapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.weatherapp to javafx.fxml;
    exports org.weatherapp;
}