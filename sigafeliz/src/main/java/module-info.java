module com.sigafeliz.sigafeliz {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // Módulos do Apache POI
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens com.sigafeliz to javafx.fxml;
    exports com.sigafeliz;
    exports com.sigafeliz.controller;
    opens com.sigafeliz.controller to javafx.fxml;
    opens com.sigafeliz.model to javafx.base;
}