module org.weatherapp  {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens org.weatherapp.UI to javafx.fxml, javafx.controls;
    exports org.weatherapp.UI;

}