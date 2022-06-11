package com.example.javafx;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.synedra.validatorfx.Check;
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

    @FXML
    Slider  slider2;

    @FXML
    HBox mediahbox;
    private File file;
    private Media media;
    private MediaPlayer mediaPlayer;
    private TreeItem<String>  prevItem = new TreeItem<String>();

    @FXML
    private SplitPane splitpane;
    @FXML
    private Label timer = new Label("00:00 / 00:00");

    @FXML
    private  MenuBar menubar;
    @FXML
    private ChoiceBox choicebox;

    @FXML
    private ImageView coverimage;
    @FXML
    private Label coverlabel;

    @FXML
    private  StackPane stackSidePane;
    @FXML
    private BorderPane borderpane;

    @FXML
    private Menu menutheme;
    @FXML
    private Button fullscreenbutton;
    private Node componentsPane;
    private SplitPane.Divider divider;
    private  Double divpos = 0.0;
    private Map<String, String> treemap = new HashMap<String, String>();
    private ImageView playNode,pauseNode,prevNode,nextNode, expand,compress;
    private  String labeltext;
    private  String hours_string,minutes_string,seconds_string;

    private  String hours_string_real,minutes_string_real,seconds_string_real;
    private long MIN_STATIONARY_TIME = 7000000000L ; // nanoseconds
    private boolean treeflag = false;
    private final String[] mediaspeed = {"0.5 x", "1 x", "2 x", "3 x", "4 x"};
    private RadioMenuItem[] themes = new RadioMenuItem[]{new RadioMenuItem("Theme 1"),new RadioMenuItem("Theme 2"), new RadioMenuItem("Theme 3")};
    private  String menuid;
    private Stage stage;
    private  Scene scene;
    private Boolean hide = false;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        ToggleGroup myToggleGroup = new ToggleGroup();

        themes[0].setToggleGroup(myToggleGroup);
        themes[1].setToggleGroup(myToggleGroup);
        themes[2].setToggleGroup(myToggleGroup);

        themes[0].setId("1");
        themes[1].setId("2");
        themes[2].setId("3");
        themes[0].setSelected(true);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                menuid = ((RadioMenuItem) e.getSource()).getId();
                if (((RadioMenuItem)e.getSource()).isSelected()) {
                    scene.getStylesheets().clear();

                    scene.getStylesheets().add(getClass().getResource("/themes/theme"+menuid+".css").toExternalForm());
                    System.out.println("/themes/theme"+menuid+".css");
                    System.out.println(scene);
                    System.out.println(stage);
                    stage.setScene(scene);
                    System.out.println("scene sceee");
                }
            }
        };


        themes[0].setOnAction(event);
        themes[1].setOnAction(event);
        themes[2].setOnAction(event);
        menutheme.getItems().addAll(themes);







        choicebox.getItems().addAll(mediaspeed);
        choicebox.getSelectionModel().select(1);
//        timer.setStyle("-fx-text-fill: white;");
//        mediahbox.setStyle("-fx-background-color: lightsteelblue;");


//        treeview.getCellFactory().setStyle("-fx-background-color: #272822;");


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
        slider2.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                changeCursor2();
            }
        });

        slider2.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                revertCursor2();
            }
        });

       try{
           expand =  new ImageView(new Image(new FileInputStream("src/icons/expand.png")));
           expand.setFitWidth(18.0);
           expand.setFitHeight(18.0);

           compress =  new ImageView(new Image(new FileInputStream("src/icons/compress.png")));
           compress.setFitWidth(18.0);
           compress.setFitHeight(18.0);

           fullscreenbutton.setGraphic(expand);


           playNode = new ImageView(new Image(new FileInputStream("src/icons/playicon.png")));
           playNode.setFitWidth(15.0);
           playNode.setFitHeight(15.0);
           play.setGraphic(playNode);


           pauseNode = new ImageView(new Image(new FileInputStream("src/icons/pauseicon.png")));
           pauseNode.setFitWidth(15.0);
           pauseNode.setFitHeight(15.0);


           prevNode = new ImageView(new Image(new FileInputStream("src/icons/backwardicon.png")));
           prevNode.setFitWidth(15.0);
           prevNode.setFitHeight(15.0);
           prev.setGraphic(prevNode);

           nextNode = new ImageView(new Image(new FileInputStream("src/icons/fastforward.png")));
           nextNode.setFitWidth(15.0);
           nextNode.setFitHeight(15.0);
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
                coverimage.setFitHeight(newValue.getHeight());
                coverimage.setFitWidth(newValue.getWidth());
                if(!treeflag)
                {
                    splitpane.setDividerPosition(0,0);
                    System.out.println("full screen1  "+ splitpane.getDividerPositions());


                }
//                else if(treeflag && stage.isFullScreen()){
//                    splitpane.setDividerPosition(0,0.2);
//                    System.out.println("full screen2  ");
//                }
                if(!stage.isFullScreen())
                {
                    fullscreenbutton.setGraphic(expand);
                }
                System.out.println(" stage full screen "+ stage.isFullScreen());
                System.out.println("full screen 3 "+ splitpane.getDividerPositions());

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
        showbutton.setVisible(false);
        showbutton.setManaged(false);

        componentsPane=splitpane.getItems().get(0);
        divider = splitpane.getDividers().get(0);
//        splitpane.lookup(".split-pane-divider").setStyle("-fx-background: red;");

        //        System.out.println(componentsPane);
//        System.out.println(divider);

        slider.styleProperty().bind(Bindings.createStringBinding(() -> {
            double percentage = (slider.getValue() - slider.getMin()) / (slider.getMax() - slider.getMin()) * 100.0 ;
            return String.format("-slider-track-color: linear-gradient(to right, -slider-filled-track-color 0%%, "
                            + "-slider-filled-track-color %f%%, -fx-base %f%%, -fx-base 100%%);",
                    percentage, percentage);
        }, slider.valueProperty(), slider.minProperty(), slider.maxProperty()));

        slider2.styleProperty().bind(Bindings.createStringBinding(() -> {
            double percentage = (slider2.getValue() - slider2.getMin()) / (slider2.getMax() - slider2.getMin()) * 100.0 ;
            return String.format("-slider-track-color: linear-gradient(to right, -slider-filled-track-color 0%%, "
                            + "-slider-filled-track-color %f%%, -fx-base %f%%, -fx-base 100%%);",
                    percentage, percentage);
        }, slider2.valueProperty(), slider2.minProperty(), slider2.maxProperty()));


        setMediaSlider(mediaPlayer);

        BooleanProperty mouseMoving = new SimpleBooleanProperty();
        mouseMoving.addListener((obs, wasMoving, isNowMoving) -> {
            if (! isNowMoving) {
                System.out.println("Mouse stopped!");
                         if(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING){
                               mediahbox.setVisible(false);
                               mediahbox.setManaged(false);
                               slider.setVisible(false);
                               slider.setManaged(false);
                               menubar.setVisible(false);
                               menubar.setManaged(false);
                               if(treeflag){
                                   treeview.getParent().setVisible(false);
                                   treeview.getParent().setManaged(false);
                                   showbutton.setVisible(true);
                                   showbutton.setManaged(true);
                                   splitpane.setDividerPosition(0,0);
                                   hide = true;
                               }
                             System.out.println("treee 1111111"+treeflag + hide);
                           }
                System.out.println("treee "+treeflag);

            }
        });
        PauseTransition pause = new PauseTransition(Duration.millis(MIN_STATIONARY_TIME / 1_000_000));
        pause.setOnFinished(e -> {
            mouseMoving.set(false);
            System.out.println("lljkjkljl");
        });

        treeview.setOnMouseMoved(e -> {
            System.out.println("jdjdjdjd");
            mouseMoving.set(true);
            pause.playFromStart();
            if(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING){
               mediahbox.setVisible(true);
               mediahbox.setManaged(true);
               slider.setVisible(true);
               slider.setManaged(true);
               menubar.setVisible(true);
               menubar.setManaged(true);
               if(treeflag){
                   treeview.getParent().setVisible(true);
                   treeview.getParent().setManaged(true);
                   showbutton.setVisible(false);
                   showbutton.setManaged(false);
                   if(hide && stage.isFullScreen() )
                   {
                       splitpane.setDividerPosition(0,0.2);
                   }else if(hide)
                    {
                        splitpane.setDividerPosition(0,divpos);
                    }
                   hide = false;
               }
           }

        });
        splitpane.setOnMouseMoved(e -> {
            System.out.println("jdjdjdjd");
            mouseMoving.set(true);
            pause.playFromStart();
            if(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING){
                mediahbox.setVisible(true);
                mediahbox.setManaged(true);
                slider.setVisible(true);
                slider.setManaged(true);
                menubar.setVisible(true);
                menubar.setManaged(true);
                if(treeflag){
                    treeview.getParent().setVisible(true);
                    treeview.getParent().setManaged(true);
                    showbutton.setVisible(false);
                    showbutton.setManaged(false);
                    if(hide && stage.isFullScreen() )
                    {
                        splitpane.setDividerPosition(0,0.2);
                    }else if(hide)
                    {
                        splitpane.setDividerPosition(0,divpos);
                    }
                    hide = false;
                }
            }
        });

        choicebox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeSpeed(mediaPlayer);
            }
        });

    }

    public void mediaVolume(MediaPlayer funcMediaPlayer1){
        slider2.setValue(funcMediaPlayer1.getVolume()*100);
        slider2.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                funcMediaPlayer1.setVolume(slider2.getValue()/100);
            }
        });

    }

    public void changeSpeed(MediaPlayer funcMediaPlayer){
        if(funcMediaPlayer != null)
        {
            funcMediaPlayer.setRate(Double.parseDouble(((String)choicebox.getValue()).split("\\s+")[0]));
        }
    }
public  void setMediaSlider(MediaPlayer funcMediaPlayer) {

   try {
       funcMediaPlayer.setOnReady(() -> {
           slider.setMin(0);
           slider.setMax(funcMediaPlayer.getMedia().getDuration().toSeconds());
           System.out.println("sec "+funcMediaPlayer.getMedia().getDuration().toSeconds());
           slider.setValue(0);
           hours_string_real = String.format("%02d",Math.round(funcMediaPlayer.getMedia().getDuration().toMinutes()/60)%60);

           changeSpeed(funcMediaPlayer);
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

//setting media Volume
           mediaVolume(mediaPlayer);

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
           slider.setMin(0);
           funcMediaPlayer.seek(new Duration(0.0));
           funcMediaPlayer.stop();
           timer.setText("00:00 / "+hours_string_real+minutes_string_real+":"+seconds_string_real);
           if(funcMediaPlayer.getStatus() == MediaPlayer.Status.PLAYING){
               mediahbox.setVisible(true);
               mediahbox.setManaged(true);
               slider.setVisible(true);
               slider.setManaged(true);
               menubar.setVisible(true);
               menubar.setManaged(true);
               if(treeflag){
                   treeview.getParent().setVisible(true);
                   treeview.getParent().setManaged(true);
                   showbutton.setVisible(false);
                   showbutton.setManaged(false);
                   splitpane.setDividerPosition(0,divpos);
               }
           }
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

                    if(doDirContainFile(file))
                    {
                        rootItem1.getChildren().add(displayDirectoryContents(file));
                    }
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

    public boolean doDirContainFile(File dir)
    {
        try {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory() && !file.isHidden()) {

//                    rootItem1.getChildren().add(new TreeItem<>(file.getName().toString()));
                    System.out.println("directory:  " + file.getCanonicalPath());

                    if(doDirContainFile(file))
                    {
                        return doDirContainFile(file);
                    }
                } else if((file.toString().contains(".mp4") || file.toString().contains(".mp3") ) && !file.isHidden()){
                    System.out.println("Mp 4 or 3 file found !");
                    return true;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
    public void eventlist()
    {
        System.out.println("eventlist");

        System.out.println(divpos);
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
                   if(mediaPlayer != null)
                   {
                       mediaPlayer.dispose();
                   }
                   mediaPlayer = new MediaPlayer(media);
                   mediaview.setMediaPlayer((mediaPlayer));

                   setMediaSlider(mediaPlayer);

                   if(treemap.get(treeItem.getValue().toString()).contains(".mp3"))
                   {
                       labeltext = "";
                       ObservableMap<String,Object> meta_data= mediaPlayer.getMedia().getMetadata();

                       meta_data.addListener(new MapChangeListener<String,Object>(){
                           @Override
                           public void onChanged(Change<? extends String, ? extends Object> ch) {

                               if(ch.wasAdded()){

                                   String key=ch.getKey();
                                   Object value=ch.getValueAdded();
                                   System.out.println(key);
                                   switch (key) {
                                       case "album" -> System.out.println("Album: " + value.toString());
                                       case "artist" -> {
                                           System.out.println("Artist: " + value.toString());
                                           labeltext = value.toString().trim()+" : ";
                                       }
                                       case "title" -> {
                                           System.out.println("Title: " + value.toString());
                                           labeltext += value.toString();

                                       }
                                       case "year" -> System.out.println("Year: " + value.toString());
                                       case "image" -> {
                                           System.out.println((Image) value);
                                           coverimage.setImage((Image) value);
                                       }
                                   }
                                   coverlabel.setText(labeltext);

                               }
                           }
                       });
                   }
               }catch (Exception e){
                    e.printStackTrace();
               }


            }



        }
    }
    public void hideButton(){


        treeview.getParent().setVisible(false);
        treeview.getParent().setManaged(false);
        showbutton.setVisible(true);
        showbutton.setManaged(true);
        treeflag = false;
        splitpane.setDividerPosition(0,0);
        componentsPane.setVisible(false);
        componentsPane.setManaged(false);
//        splitpane.getItems().remove(componentsPane);

         divpos = divider.getPosition();

    }
    public void showButton(){

        treeview.getParent().setVisible(true);
        treeview.getParent().setManaged(true);
        showbutton.setVisible(false);
        showbutton.setManaged(false);
        treeflag = true;
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
            if(mediaPlayer != null)
            {
                mediaPlayer.dispose();
            }
            mediaPlayer = new MediaPlayer(media);
            mediaview.setMediaPlayer((mediaPlayer));
            setMediaSlider(mediaPlayer);

           if(file.toURI().toString().contains(".mp3"))
           {
               labeltext = "";
               ObservableMap<String,Object> meta_data= mediaPlayer.getMedia().getMetadata();

               meta_data.addListener(new MapChangeListener<String,Object>(){
                   @Override
                   public void onChanged(Change<? extends String, ? extends Object> ch) {

                       if(ch.wasAdded()){

                           String key=ch.getKey();
                           Object value=ch.getValueAdded();
                           System.out.println(key);
                           switch (key) {
                               case "album" -> System.out.println("Album: " + value.toString());
                               case "artist" -> {
                                   System.out.println("Artist: " + value.toString());
                                   labeltext = value.toString().trim()+" : ";
                               }
                               case "title" -> {
                                   System.out.println("Title: " + value.toString());
                                   labeltext += value.toString();

                               }
                               case "year" -> System.out.println("Year: " + value.toString());
                               case "image" -> {
                                   System.out.println((Image) value);
                                   coverimage.setImage((Image) value);
                               }
                           }
                           coverlabel.setText(labeltext);

                       }
                   }
               });
           }


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
    public void backMedia()
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.seek(new Duration((mediaPlayer.getCurrentTime().toSeconds() - 10)*1000 ));
        }
        System.out.println("prev");
    }
    public void forwardMedia()
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.seek(new Duration((mediaPlayer.getCurrentTime().toSeconds() + 10)*1000 ));

        }
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
    public void changeCursor2(){
        slider2.setStyle("-fx-cursor: move;");
        System.out.println("draged");
    }
    public void revertCursor2(){
        slider2.setStyle("-fx-cursor: hand;");
        System.out.println("drageddddd");
    }

    public void changeTheme(){

    }
    public void setStage(Stage primarystage){
        stage = primarystage;
    }
    public void setScene(Scene primaryscene){
        scene = primaryscene;
    }
    public void closeWindow(){
        stage.close();
    }

    public void toggleScreen(){
        if(stage.isFullScreen())
        {
            stage.setFullScreen(false);
            if(treeflag)
            {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // here goes your code to delay
                        splitpane.setDividerPositions(0.3);
                        cancel();
                    }
                }, 200);
//                / stage.getScene().getWidth());
            }
//            System.out.println("get divdideress "+splitpane.getDividers());
//            System.out.println("get divdideress "+splitpane.getDividers().get(0));
//            System.out.println("get divdideress "+splitpane.set);
            fullscreenbutton.setGraphic(expand);
        }else{
            stage.setFullScreen(true);
            if(treeflag)
            {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // here goes your code to delay
                        splitpane.setDividerPositions(0.2);
                        cancel();
                    }
                }, 200);
//                / stage.getScene().getWidth());
            }
            fullscreenbutton.setGraphic(compress);
        }
    }

    public void createAboutStage(){
        Parent root2;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("scene2.fxml"));
            Scene scene2 = new Scene(fxmlLoader.load(), 605, 405);
            Stage stage2 = new Stage();
            stage2.setTitle("Contact Us");
            stage2.setScene(scene2);
            stage2.show();
            // Hide this current window (if this is what you want)
//            stage.hide();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createFeedbackStage(){
        Parent root2;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("scene3.fxml"));
            Scene scene2 = new Scene(fxmlLoader.load(), 605, 450);
            Stage stage2 = new Stage();
            stage2.setTitle("Feedback");
            stage2.setScene(scene2);
            stage2.show();
            // Hide this current window (if this is what you want)
//            stage.hide();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createAboutSection(){
        Parent root3;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("scene4.fxml"));
            Scene scene3 = new Scene(fxmlLoader.load(), 605, 405);
            Stage stage3 = new Stage();
            stage3.setTitle("About");
            stage3.setScene(scene3);
            stage3.show();
            // Hide this current window (if this is what you want)
//            stage.hide();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



}
