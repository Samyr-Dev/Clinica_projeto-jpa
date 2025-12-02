module org.clinica {
    requires javafx.controls;
    requires javafx.fxml;
    //requires org.hibernate.orm.core;
    //requires jakarta.persistence;
    requires java.sql;


    opens org.clinica.controller to javafx.fxml;
    opens org.clinica.model to javafx.base;
    opens org.clinica.dao to javafx.fxml;
    opens org.clinica to javafx.fxml;
    exports org.clinica;
    exports org.clinica.dao to javafx.fxml;
    exports org.clinica.controller to javafx.fxml;
}