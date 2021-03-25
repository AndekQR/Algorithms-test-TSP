package app.view.controlPanel;

import javafx.scene.layout.VBox;

public class ControlPanel extends VBox {

    public static double WIDTH=250;

    public ControlPanel() {
        this.init();
    }

    private void init() {
        this.setPrefWidth(WIDTH);
        this.setStyle("-fx-background-color: #e0afa0");

        AlgorithmsTabPane algorithmsTabPane=new AlgorithmsTabPane();
        this.getChildren().add(algorithmsTabPane);
    }
}
