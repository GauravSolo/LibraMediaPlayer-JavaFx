package com.example.javafx;
import com.almasb.fxgl.net.Connection;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Objects;
import static javafx.scene.input.KeyCode.*;

public class Main extends  Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scene1.fxml"));
        System.out.println("path string  ==="+ getClass().getResource("scene1.fxml")+getClass().getResource("scene1.fxml").toString());
        Parent root = (Parent)loader.load();
        Scene1Controller controller = (Scene1Controller) loader.getController();

        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
        {
            System.out.println("caling");
            controller.eventlist();
        };

        primaryStage.maximizedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                System.out.println("maximized:" + t1.booleanValue());
                controller.toggleScreen2(t1.booleanValue());
            }
        });

        primaryStage.widthProperty().addListener(stageSizeListener);
        primaryStage.heightProperty().addListener(stageSizeListener);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/themes/theme1.css")).toExternalForm());
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                System.out.println(keyEvent.getCode());
                if(keyEvent.getCode() == SPACE) {
                    controller.play();
                }
            }
        });






        controller.setStage(primaryStage);
        controller.setScene(scene);
        primaryStage.setTitle("Libra MediaPlayer");
        primaryStage.getIcons().add(new Image("newicon.png"));
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }



}
