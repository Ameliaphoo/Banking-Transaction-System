package com.example.mybank;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)throws Exception
    {
        FXMLLoader fxmlloader=new FXMLLoader(getClass().getResource("/Fxml/login.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        Scene scene=new Scene(fxmlloader.load());
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}
