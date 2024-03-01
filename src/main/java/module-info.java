module org.weatherapp  {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.weatherapp.UI to javafx.fxml, javafx.controls;
    exports org.weatherapp.UI;

}