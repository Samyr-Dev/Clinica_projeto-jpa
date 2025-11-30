module org.clinica {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.clinica.controller to javafx.fxml;
    opens org.clinica to javafx.fxml;
    exports org.clinica;
    exports org.clinica.controller to javafx.fxml;
}