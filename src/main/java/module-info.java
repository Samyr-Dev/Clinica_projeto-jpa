module org.clinica {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.clinica.model to javafx.base;
    opens org.clinica.dao to javafx.fxml;
    opens org.clinica to javafx.fxml;
    exports org.clinica;
    exports org.clinica.dao to javafx.fxml;
}