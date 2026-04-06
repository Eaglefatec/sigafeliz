module com.sigafeliz.sigafeliz {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.sigafeliz to javafx.fxml;
    exports com.sigafeliz;
    exports com.sigafeliz.controller;
    opens com.sigafeliz.controller to javafx.fxml;
    opens com.sigafeliz.model to javafx.base;
}