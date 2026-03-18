module com.eaglefatec.sigafeliz {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires org.apache.poi.ooxml;
    requires java.desktop;

    opens com.eaglefatec.sigafeliz to javafx.fxml;
    opens com.eaglefatec.sigafeliz.controller to javafx.fxml;
    opens com.eaglefatec.sigafeliz.model to javafx.base;

    exports com.eaglefatec.sigafeliz;
}
