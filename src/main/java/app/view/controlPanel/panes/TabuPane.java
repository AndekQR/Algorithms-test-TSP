package app.view.controlPanel.panes;

import app.controller.algorithms.sa.SimulatedAnnealingParameters;
import app.controller.algorithms.ts.TabuSearchParameters;
import app.controller.helpers.Helpers;
import app.view.controlPanel.ControlPanel;
import app.view.controlPanel.Controlling;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TabuPane extends VBox {

    private Controlling controlling;
    private long iterations;

    public TabuPane(Controlling controlling) {
        this.controlling = controlling;

        setSpacing(5);
        Node solveButton = getSolveButton();
        Node iterationsControl = getIterationsControl();
        this.getChildren().addAll(iterationsControl, solveButton);

        controlling.getProgressBarProperty().addListener((observable, oldValue, newValue) -> {
            solveButton.setDisable(newValue);
            iterationsControl.setDisable(newValue);
        });

    }

    private Node getSolveButton() {
        Button button = new Button("Solve");
        button.setPrefWidth(ControlPanel.WIDTH);

        button.setOnMouseClicked(event -> {
            controlling.showProgressBar();

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(() -> {
                TabuSearchParameters tabuSearchParameters = TabuSearchParameters.builder()
                        .iterations(this.iterations)
                        .build();
                controlling.solveByTabuSearch(tabuSearchParameters);
                controlling.hideProgressBar();
            });
            executorService.shutdown();
        });
        return button;
    }

    private Node getIterationsControl() {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);

        Slider slider = new Slider(200, 2000, 400);
        slider.setMajorTickUnit(300);
        slider.setMinorTickCount(1);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMaxWidth(ControlPanel.WIDTH - 20);

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            slider.setValue(Helpers.round(newValue.doubleValue(), 0));
            this.iterations = newValue.longValue();
        });
        this.iterations = (long)slider.getValue();

        Label label = new Label("Iterations");
        label.setLabelFor(slider);

        vBox.getChildren().addAll(label, slider);
        return vBox;
    }
}
