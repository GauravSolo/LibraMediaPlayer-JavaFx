package com.example.javafx;


import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.Media;
import javafx.util.Duration;

public class Controller implements Initializable {
    @FXML
    private TreeView treeview;
    @FXML
    private  MediaView mediaview;
    @FXML
    private Button play,pause,reset;

    private File file;
    private  Media media;
    private MediaPlayer mediaPlayer;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        TreeItem<String> rootItem = new TreeItem<>("Files");

        TreeItem<String> branchItem1 = new TreeItem<>("Pictures");
        TreeItem<String> branchItem2 = new TreeItem<>("Videos");
        TreeItem<String> branchItem3 = new TreeItem<>("Music");

        TreeItem<String> leafItem1 = new TreeItem<>("Picture1");
        TreeItem<String> leafItem2 = new TreeItem<>("Picture2");
        TreeItem<String> leafItem3 = new TreeItem<>("Video1");
        TreeItem<String> leafItem4 = new TreeItem<>("Video2");
        TreeItem<String> leafItem5 = new TreeItem<>("Music1");
        TreeItem<String> leafItem6 = new TreeItem<>("Music2");

        branchItem1.getChildren().addAll(leafItem1, leafItem2);
        branchItem2.getChildren().addAll(leafItem3, leafItem4);
        branchItem3.getChildren().addAll(leafItem5, leafItem6);

        rootItem.getChildren().addAll(branchItem1, branchItem2, branchItem3);
        treeview.setShowRoot(false);
        treeview.setRoot(rootItem);



//        Media Player
        file = new  File("src/main/resources/com/example/javafx/sampleVideo.mp4");
        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        DoubleProperty videow = mediaview.fitWidthProperty();
        DoubleProperty videoh= mediaview.fitHeightProperty();


        System.out.println(videow + "   " + videoh);
        videow.bind(Bindings.selectDouble(mediaview.sceneProperty(),"width"));
        videoh.bind(Bindings.selectDouble(mediaview.sceneProperty(),"height"));
//        mediaview.setFitWidth(videow);
        mediaview.setMediaPlayer((mediaPlayer));


//        int w = stage.getWidth(); // player.getMedia().getWidth();
//        int h = stage.getHeight(); // player.getMedia().getHeight();
//
//        // stage.setMinWidth(w);
//        // stage.setMinHeight(h);
//        // make the video conform to the size of the stage now...
//        player.setFitWidth(w);
//        player.setFitHeight(h);

    }
    public void selectItem(){
        TreeItem<String> treeItem = (TreeItem<String>) treeview.getSelectionModel().getSelectedItem();
        if(treeItem != null)
        {
            System.out.println(treeItem.getValue());

        }
    }
    public void newMethod(){
        System.out.println("clicked on New");
    }

    public void play()
    {
        mediaPlayer.play();
    }
    public void pause()
    {
        mediaPlayer.pause();

    }
    public void reset()
    {
        if(mediaPlayer.getStatus() != MediaPlayer.Status.READY)
        {
            mediaPlayer.seek(Duration.seconds(0.0));
        }
    }
}
