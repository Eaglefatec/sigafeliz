package com.eaglefatec.sigafeliz;

import com.eaglefatec.sigafeliz.controller.MainController;
import com.eaglefatec.sigafeliz.dao.DatabaseManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize database on startup
        DatabaseManager.getInstance();

        MainController mainController = new MainController(primaryStage);
        mainController.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
