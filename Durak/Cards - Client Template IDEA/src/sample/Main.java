package sample;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

/**
 *
 * @author rcortez
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../sample/FXMLDocument.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Client");
        stage.setScene(scene);
        stage.setResizable(false);
//        stage.setMaxHeight(900);
//        stage.setMaxWidth(900);
        stage.show();
        JMetro jMetro = new JMetro(Style.DARK);
        jMetro.setParent(root);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
