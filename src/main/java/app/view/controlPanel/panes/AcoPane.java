package app.view.controlPanel.panes;

import app.controller.aco.AcoParameters;
import app.controller.helpers.Helpers;
import app.view.controlPanel.ControlPanel;
import app.view.controlPanel.Controlling;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class AcoPane extends VBox {

    private Controlling controlPanel;
    private double alpha = 1;
    private double beta = 5;
    private double evaporation = 0.1;
    private int ants = 100;
    private int generations = 100;
    private final Button solveButton = getSolveButton();

    public AcoPane(Controlling controlPanel) {
        this.controlPanel = controlPanel;

        setSpacing(5);

        Node alphaControl = getAlphaControl();
        Node betaControl = getBetaControl();
        Node evaporationControl = getEvaporationControl();
        Node antsControl = getAntsControl();
        Node generationsControl = getGenerationsControl();

        this.getChildren().addAll(alphaControl, betaControl, evaporationControl, antsControl, generationsControl,
                solveButton);
    }

    private Button getSolveButton() {
        Button button = new Button();
        button.setText("Solve");
        button.setPrefWidth(ControlPanel.WIDTH);
        button.setOnMouseClicked(event -> {
            controlPanel.showProgressBar();

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(() -> {
                AcoParameters acoParameters = AcoParameters.builder()
                        .alpha(alpha)
                        .beta(beta)
                        .evaporation(evaporation)
                        .ants(ants)
                        .generations(generations)
                        .build();
                controlPanel.solveByAco(acoParameters);
                controlPanel.hideProgressBar();
            });
            executorService.shutdown();
        });
        return button;
    }

    private Node getAlphaControl() {
        VBox vBox = new VBox();

        Slider slider = new Slider(0, 10, 1);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            slider.setValue(Math.round(newValue.doubleValue()));
            this.alpha = newValue.intValue();
        });

        Label label = new Label("Alpha");
        label.setLabelFor(slider);

        vBox.getChildren().addAll(label, slider);
        return vBox;
    }

    private Node getBetaControl() {
        VBox vBox = new VBox();

        Slider slider = new Slider(0, 10, 5);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            slider.setValue(Math.round(newValue.doubleValue()));
            this.beta = newValue.intValue();
        });

        Label label = new Label("Beta");
        label.setLabelFor(slider);

        vBox.getChildren().addAll(label, slider);
        return vBox;
    }

    private Node getEvaporationControl() {
        VBox vBox = new VBox();

        Slider slider = new Slider(0, 1, 0.1);
        slider.setMajorTickUnit(0.1);
        slider.setMinorTickCount(0);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            slider.setValue(Helpers.round(newValue.doubleValue(), 1));
            this.evaporation = newValue.doubleValue();
        });


        Label label = new Label("Evaporation rate");
        label.setLabelFor(slider);

        vBox.getChildren().addAll(label, slider);
        return vBox;
    }


    private Node getAntsControl() {
        VBox vBox = new VBox();

        TextField antsNumberTextField = new TextField("100");
        antsNumberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                this.ants = Integer.parseInt(newValue);
                solveButton.setDisable(false);
            } catch (NumberFormatException exception) {
                solveButton.setDisable(true);
            }
        });

        Label label = new Label("Number of ants");
        label.setLabelFor(antsNumberTextField);

        vBox.getChildren().addAll(label, antsNumberTextField);
        return vBox;
    }

    private Node getGenerationsControl() {
        VBox vBox = new VBox();

        TextField generationNumberTextField = new TextField("100");
        generationNumberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                this.generations = Integer.parseInt(newValue);
                solveButton.setDisable(false);
            } catch (NumberFormatException exception) {
                solveButton.setDisable(true);
            }
        });

        Label label = new Label("Number of generations");
        label.setLabelFor(generationNumberTextField);

        vBox.getChildren().addAll(label, generationNumberTextField);
        return vBox;
    }
}
