package app;

import app.db.Database;
import app.view.controlPanel.ControlPanel;
import app.view.myGraphView.GraphView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main extends Application {

    public static double WIDTH=1024;
    public static double HEIGHT=768;

    public static void main(String[] args) {
        launch(args);
    }



    @Override
    public void start(Stage primaryStage) {
        GraphView graphView = new GraphView();
        ControlPanel controlPanel=new ControlPanel(graphView);

        HBox root=new HBox();
        root.getChildren().addAll(controlPanel, graphView);
        Scene scene=new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource("../style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

//        AcoAlgorithm acoAlgorithm=new AcoAlgorithm(copyGraph);
//        acoAlgorithm.solve();
    }
}
