package com.example.javafx;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.security.Key;

import static javafx.scene.input.KeyCode.SPACE;

public class Main extends  Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scene1.fxml"));
        Parent root = (Parent)loader.load();
        Scene1Controller controller = (Scene1Controller) loader.getController();
        controller.setStage(primaryStage);
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
        {
            System.out.println("caling");
            controller.eventlist();
        };

        primaryStage.widthProperty().addListener(stageSizeListener);
        primaryStage.heightProperty().addListener(stageSizeListener);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                System.out.println(keyEvent.getCode());
                if(keyEvent.getCode() == SPACE)
                {
                    controller.play();
                }
            }
        });
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
