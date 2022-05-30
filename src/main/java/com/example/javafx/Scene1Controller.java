package com.example.javafx;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.w3c.dom.ls.LSOutput;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.toIntExact;

public class Scene1Controller implements Initializable {
    @FXML
    private TreeView treeview;
    @FXML
    private MediaView mediaview;
    @FXML
    private Button play,prev,next;

    @FXML
    Slider slider;

    @FXML
    private Button hidebutton,showbutton;
    private File file;
    private Media media;
    private MediaPlayer mediaPlayer;
    private TreeItem<String>  prevItem = new TreeItem<String>();

    @FXML
    private SplitPane splitpane;
    @FXML
    private Label timer = new Label("00:00 / 00:00");
    private Node componentsPane;
    private SplitPane.Divider divider;
    private  Double divpos = 0.0;
    private Map<String, String> treemap = new HashMap<String, String>();
    private ImageView playNode,pauseNode,prevNode,nextNode;
    private  int interval;

    private int elapseTime=0, hours=0, minutes=0, seconds=0;
    private  String hours_string,minutes_string,seconds_string;
    private  String hours_string_real,minutes_string_real,seconds_string_real;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        slider.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                changeCursor();
            }
        });

        slider.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                revertCursor();
            }
        });

       try{
           playNode = new ImageView(new Image(new FileInputStream("src/icons/play.png")));
           playNode.setFitWidth(20.0);
           playNode.setFitHeight(20.0);
           play.setGraphic(playNode);


           pauseNode = new ImageView(new Image(new FileInputStream("src/icons/pause.png")));
           pauseNode.setFitWidth(20.0);
           pauseNode.setFitHeight(20.0);


           prevNode = new ImageView(new Image(new FileInputStream("src/icons/previous.png")));
           prevNode.setFitWidth(20.0);
           prevNode.setFitHeight(20.0);
           prev.setGraphic(prevNode);

           nextNode = new ImageView(new Image(new FileInputStream("src/icons/next.png")));
           nextNode.setFitWidth(20.0);
           nextNode.setFitHeight(20.0);
           next.setGraphic(nextNode);
       }catch (Exception e)
       {
           e.printStackTrace();
       }



//        treeview.getParent().setVisible(false);
//        treeview.getParent().setManaged(false);

//        Media Player
        file = new  File("src/main/resources/com/example/javafx/reel.mp4");
        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);


        mediaview.getParent().layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
                mediaview.setFitHeight(newValue.getHeight());
                mediaview.setFitWidth(newValue.getWidth());

            }
        });

//        container.widthProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                System.out.println("chaingong");
//            }
//        });
//        mediaview.setFitHeight(mediaview.getParent().getHeight());
//        mediaview.setFitWidth(mediaview.getParent().getWidth());
//        DoubleProperty videow = mediaview.fitWidthProperty();
//        DoubleProperty videoh= mediaview.fitHeightProperty();


//        System.out.println(videow + "   " + videoh);
//        videow.bind(Bindings.selectDouble(mediaview.sceneProperty(),"width"));
//        videoh.bind(Bindings.selectDouble(mediaview.sceneProperty(),"height"));
//        mediaview.setFitWidth(videow);
        mediaview.setMediaPlayer(mediaPlayer);


//        int w = stage.getWidth(); // player.getMedia().getWidth();
//        int h = stage.getHeight(); // player.getMedia().getHeight();
//
//        // stage.setMinWidth(w);
//        // stage.setMinHeight(h);
//        // make the video conform to the size of the stage now...
//        player.setFitWidth(w);
//        player.setFitHeight(h);
        treeview.getParent().setVisible(false);
        treeview.getParent().setManaged(false);
        showbutton.getParent().setVisible(true);
        showbutton.getParent().setManaged(true);



        componentsPane=splitpane.getItems().get(0);
        divider = splitpane.getDividers().get(0);
//        System.out.println(componentsPane);
//        System.out.println(divider);
        setMediaSlider(mediaPlayer);


    }

public  void setMediaSlider(MediaPlayer funcMediaPlayer) {

   try {
       funcMediaPlayer.setOnReady(() -> {
           slider.setMin(0);
           slider.setMax(funcMediaPlayer.getMedia().getDuration().toSeconds());
           System.out.println("sec "+funcMediaPlayer.getMedia().getDuration().toSeconds());
           slider.setValue(0);
           hours_string_real = String.format("%02d",Math.round(funcMediaPlayer.getMedia().getDuration().toMinutes()/60)%60);


           if(Objects.equals(hours_string_real, "00"))
           {
               hours_string_real = "";
               System.out.println("yes hours real is 0");
           }else{
               hours_string_real += ":";
           }
           minutes_string_real = String.format("%02d",Math.round(funcMediaPlayer.getMedia().getDuration().toMinutes())%60);
           seconds_string_real = String.format("%02d",Math.round(funcMediaPlayer.getMedia().getDuration().toSeconds())%60);
           timer.setText("00:00"+" / "+ hours_string_real+minutes_string_real+":"+seconds_string_real);

           System.out.println("minutes_string_real "+minutes_string_real + "  seconds_string_real  " + seconds_string_real + " mile "+ funcMediaPlayer.getMedia().getDuration());
       });

       funcMediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
           @Override
           public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration t1) {
               Duration d = funcMediaPlayer.getCurrentTime();
//               System.out.println("d  "+d.toSeconds() + " max " + funcMediaPlayer.getMedia().getDuration().toSeconds());
//               if( Math.floor(d.toSeconds()) ==  Math.floor(funcMediaPlayer.getMedia().getDuration().toSeconds())){
//                   System.out.println("equallss");
//               }
               slider.setValue(d.toSeconds());
               System.out.println(d + "       iljfljdl     " + Math.round(d.toSeconds()) + "       ljjnzzzzzz     " + Math.round(d.toMinutes()));
               hours_string = String.format("%02d",Math.round(d.toHours())%60);
               System.out.println("hours === " + hours_string +"  ==-=-=  " + d.toHours());
               if(Objects.equals(hours_string, "00"))
               {
                   hours_string = "";
                   System.out.println("yes hours is 0");
               }else{
                   hours_string += ":";
               }

               minutes_string = String.format("%02d",Math.round(d.toMinutes())%60);
               seconds_string = String.format("%02d",Math.round(d.toSeconds())%60);
////               timer.setText(Double.toString(d.toMinutes()).replaceAll(".",":"));
               timer.setText(hours_string+minutes_string+":"+seconds_string +" / "+ hours_string_real+minutes_string_real+":"+seconds_string_real);
               System.out.println(hours_string+minutes_string+":"+seconds_string +" / "+ hours_string_real+minutes_string_real+":"+seconds_string_real);
           }
       });
       funcMediaPlayer.setOnEndOfMedia(()->{
           System.out.println("stop");
           play.setGraphic(playNode);
           timer.setText("00:00 / "+hours_string_real+minutes_string_real+":"+seconds_string_real);
       });

       slider.setOnMousePressed(new EventHandler<MouseEvent>() {
           @Override
           public void handle(MouseEvent mouseEvent) {
               mediaPlayer.seek(Duration.seconds(slider.getValue()));
           }
       });
       slider.setOnMouseDragged(new EventHandler<MouseEvent>() {
           @Override
           public void handle(MouseEvent mouseEvent) {
               mediaPlayer.seek(Duration.seconds(slider.getValue()));
           }
       });

   }catch (Exception e)
   {
       e.printStackTrace();
   }

}
    public void openFolder() throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);


        if(selectedDirectory == null){
            //No Directory selected
            System.out.println("null");
        }else{
            TreeItem<String> root = new TreeItem<>(selectedDirectory.getName().toString());
            root = displayDirectoryContents(selectedDirectory);
            treeview.setShowRoot(false);
            treeview.setRoot(root);
            showButton();
//            System.out.println(selectedDirectory.getAbsolutePath());
////            System.out.println(Arrays.stream(selectedDirectory.list()).count());
////            Array[] pathnames = Arrays.stream(selectedDirectory.list());
//            String[] pathnames = selectedDirectory.list();
//            for (String pathname : pathnames) {
//                // Print the names of files and directories
//                System.out.println(pathname);
//            }
//            =================================================================
//            BiPredicate<Path, BasicFileAttributes> Matcher = (path,attributes) -> {
////                System.out.println("attr " + attributes);
////                System.out.println("path "+path);
////                System.out.println("path"+path);
//                return path.toString().contains(".mp4") || attributes.isDirectory() ;
//            };
//            Files.find(Paths.get(selectedDirectory.getAbsolutePath()),Integer.MAX_VALUE,Matcher).forEach((element)->{
//                System.out.println(element);
//            });
            //==================================================================

//            try (Stream<Path> walk = Files.walk(Paths.get("src/main/java/com/example/javafx"))) {
//                // We want to find only regular files
//                List<String> result = walk.filter(Files::isRegularFile)
//                        .map(x -> x.toString()).collect(Collectors.toList());
//
//                result.forEach(System.out::println);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            System.out.println(Arrays.stream(selectedDirectory.listFiles()).count());

        }
    }


    public  TreeItem<String> displayDirectoryContents(File dir) {


        TreeItem<String> rootItem1 = new TreeItem<>(dir.getName().toString());
        try {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory() && !file.isHidden()) {

//                    rootItem1.getChildren().add(new TreeItem<>(file.getName().toString()));
                    System.out.println("directory:  " + file.getCanonicalPath());

                    rootItem1.getChildren().add(displayDirectoryContents(file));
                } else if((file.toString().contains(".mp4") || file.toString().contains(".mp3") ) && !file.isHidden()){
                    System.out.println("uri   ==="+ file.toURI() + " -- "+ file.toURI().toURL());
                    treemap.put(file.getName().toString(), file.toURI().toString());
                    rootItem1.getChildren().add(new TreeItem<>(file.getName().toString()));
                    System.out.println("file:  " + file.getCanonicalPath());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
//        rootItem.getChildren().add(rootItem1);
        return rootItem1;
    }

    public void eventlist()
    {
        System.out.println("eventlist");

        System.out.println(divpos);
        splitpane.setDividerPosition(0,divpos);
//        System.out.println(divider.setPosition(0.5));

    }
    public void selectItem(){
        TreeItem<String> treeItem = (TreeItem<String>) treeview.getSelectionModel().getSelectedItem();

        if(treeItem != null && prevItem != treeItem)
        {
//            System.out.println(treemap.get(treeItem.toString()));
            System.out.println(treeItem.getValue());
            prevItem = treeItem;


            if(treeItem.getValue().toString().contains(".mp4") || treeItem.getValue().toString().contains(".mp3")){
//                System.out.println("tree path "+treeItem.getValue().toString() + "--" );

               try{
                   play.setGraphic(playNode);

                   media = new Media(treemap.get(treeItem.getValue().toString()));
                   mediaPlayer = new MediaPlayer(media);
                   mediaview.setMediaPlayer((mediaPlayer));
                   setMediaSlider(mediaPlayer);
               }catch (Exception e){
                    e.printStackTrace();
               }


            }



        }
    }
    public void hideButton(){


        treeview.getParent().setVisible(false);
        treeview.getParent().setManaged(false);
        showbutton.getParent().setVisible(true);
        showbutton.getParent().setManaged(true);
        splitpane.setDividerPosition(0,0);
        componentsPane.setVisible(false);
        componentsPane.setManaged(false);
//        splitpane.getItems().remove(componentsPane);

         divpos = divider.getPosition();

    }
    public void showButton(){

        treeview.getParent().setVisible(true);
        treeview.getParent().setManaged(true);
        showbutton.getParent().setVisible(false);
        showbutton.getParent().setManaged(false);
        componentsPane.setVisible(true);
        componentsPane.setManaged(true);
//        splitpane.getItems().add(0, componentsPane);
        splitpane.setDividerPosition(0,0.3);
        divpos = divider.getPosition();
    }
    public void chooseFile(){
        System.out.println("clicked on New");
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Only MP4 or MP3", "*.mp4","*.mp3");
        chooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = chooser.showOpenDialog(null);

        if (file != null) {
            System.out.println(file.getPath() + "====" + file.toURI().toString() );
            media = new Media(file.toURI().toString());
            play.setGraphic(playNode);
            mediaPlayer = new MediaPlayer(media);
            mediaview.setMediaPlayer((mediaPlayer));
            setMediaSlider(mediaPlayer);
        }else{
            System.out.println("file not found");
        }
    }

    public void play()
    {
        MediaPlayer.Status status  = mediaPlayer.getStatus();
        System.out.println(MediaPlayer.Status.PLAYING);
        if(status == MediaPlayer.Status.PLAYING)
        {
            mediaPlayer.pause();
            play.setGraphic(playNode);
        }else{
            mediaPlayer.play();
            play.setGraphic(pauseNode);
        }


    }
    public void prevMedia()
    {

        System.out.println("prev");
    }
    public void nextMedia()
    {

        System.out.println("next");
    }
    public void changeCursor(){
        slider.setStyle("-fx-cursor: move;");
        System.out.println("draged");
    }
    public void revertCursor(){
        slider.setStyle("-fx-cursor: hand;");
        System.out.println("drageddddd");
    }

}
