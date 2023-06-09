package com.example.final_12_lead;//package com.example.final_12_lead;
////
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class TestMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Thread thread = new Thread(()->{
            Platform.runLater(()->{
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(TestMain.class.getResource("LiveView - Copy.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
                stage.setMaximized(true);
                stage.setScene(scene);
                stage.getIcons().add(new Image(Objects.requireNonNull(TestMain.class.getResourceAsStream("logo.png"))));
                stage.show();
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            });
        });thread.start();
    }

    public static void main(String[] args) {
        launch();
    }
}
