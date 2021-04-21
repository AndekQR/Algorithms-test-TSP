package app.view.controlPanel.panes;

import app.controller.algorithms.sa.SimulatedAnnealingParameters;
import app.controller.helpers.Helpers;
import app.view.controlPanel.ControlPanel;
import app.view.controlPanel.Controlling;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SAPane extends VBox {

    private int temerature = 10000;
    private double coolingRate = 0.003;
    private final Controlling controlling;

    public SAPane(Controlling controlling) {
        this.controlling = controlling;

        setSpacing(5);
        Node solveButton = getSolveButton();
        Node temperatureControl = getTemperatureControl();
        Node collingRateControl = getCollingRateControl();

        this.getChildren().addAll(temperatureControl, collingRateControl, solveButton);
    }

    private Node getSolveButton() {
        Button button = new Button("Solve");
        button.setPrefWidth(ControlPanel.WIDTH);

        button.setOnMouseClicked(event -> {
            controlling.showProgressBar();

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(() -> {
                SimulatedAnnealingParameters parameters = SimulatedAnnealingParameters.builder()
                        .initialTemperature(temerature)
                        .coolingRate(coolingRate)
                        .build();
                controlling.solveBySimulatedAnnealing(parameters);
                controlling.hideProgressBar();
            });
            executorService.shutdown();
        });
        return button;
    }

    private Node getTemperatureControl() {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);

        Slider slider = new Slider(1000, 20000, 10000);
        slider.setMajorTickUnit(1000);
        slider.setMinorTickCount(1);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMaxWidth(ControlPanel.WIDTH - 20);

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            slider.setValue(Helpers.round(newValue.doubleValue(), 0));
            this.temerature = newValue.intValue();
        });


        Label label = new Label("Initial temperature");
        label.setLabelFor(slider);

        vBox.getChildren().addAll(label, slider);
        return vBox;
    }

    private Node getCollingRateControl() {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);

        Slider slider = new Slider(0.001, 0.01, 0.003);
        slider.setMajorTickUnit(0.002);
        slider.setMinorTickCount(1);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMaxWidth(ControlPanel.WIDTH - 20);

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            slider.setValue(Helpers.round(newValue.doubleValue(), 3));
            this.coolingRate = newValue.doubleValue();
        });


        Label label = new Label("Cooling rate");
        label.setLabelFor(slider);

        vBox.getChildren().addAll(label, slider);
        return vBox;
    }
}
