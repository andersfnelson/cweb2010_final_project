package sample;
import javafx.scene.image.Image;
import org.json.simple.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.simple.parser.JSONParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        //Setting up window
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Dad Jokes");
        Scene scene = new Scene(root);

        //Defines style sheet
        scene.getStylesheets().add(Main.class.getResource("sample.css").toExternalForm());
        primaryStage.setScene(scene);
        //Doesn't change Dock icon
        //primaryStage.getIcons().add(new Image("file:/Users/andersnelson/Desktop/Project 4 git working/src/sample/background.jpg"));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);


    }
}

//Adding comment

