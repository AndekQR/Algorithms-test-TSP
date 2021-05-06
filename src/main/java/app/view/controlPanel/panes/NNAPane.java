package app.view.controlPanel.panes;

import app.controller.algorithms.sa.SimulatedAnnealingParameters;
import app.view.controlPanel.ControlPanel;
import app.view.controlPanel.Controlling;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NNAPane extends VBox {

    private Controlling controlling;

    public NNAPane(Controlling controlling) {
        this.controlling = controlling;

        this.setSpacing(5);
        Node solveButton = getSolveButton();
        this.getChildren().add(solveButton);
    }

    private Node getSolveButton() {
        Button button = new Button("Solve");
        button.setPrefWidth(ControlPanel.WIDTH);

        button.setOnMouseClicked(event -> {
            controlling.showProgressBar();

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(() -> {
                controlling.solveByNearestNeighbourAlgorithm();
                controlling.hideProgressBar();
            });
            executorService.shutdown();
        });
        return button;
    }
}
