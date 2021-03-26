package app.view.controlPanel.panes;

import app.controller.graph.Country;
import app.controller.utils.GraphCreator;
import app.view.controlPanel.ControlPanel;
import app.view.controlPanel.Controlling;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * Generowanie grafu o określonej ilości wierzchołków
 * Generowanie grafu przez klikanie
 * wyświetanie zapisanych grafów w liście
 */
public class GraphPane extends VBox {

    private Controlling controlPanel;

    public GraphPane(Controlling controlPanel) {
        this.controlPanel=controlPanel;
        RandomGraphGenerator randomGraphGenerator=new RandomGraphGenerator();
        this.getChildren().add(randomGraphGenerator);
    }

    class RandomGraphGenerator extends GridPane {

        private TextField noVerticesTextField;
        private TextField graphNameTextField;
        private Button generateButton;
        private Button saveButton;


        public RandomGraphGenerator() {
            this.noVerticesTextField=getFormattedTextField("Graph vertices");
            this.graphNameTextField=getFormattedTextField("Graph name");
            this.generateButton=getGenerateButton();
            this.saveButton=getSaveButton();

            this.setVgap(2);
            this.setPadding(new Insets(5, 0, 5, 0));


            this.add(noVerticesTextField, 0, 0, 1, 1);
            this.add(graphNameTextField, 0, 1, 1, 1);
            this.add(generateButton, 2, 0, 1, 2);
            this.add(saveButton, 0, 2, 3, 1);

            this.initActions();
        }

        private boolean isValueGood(String intValue) {
            int value;
            try {
                value=Integer.parseInt(intValue);
            } catch (NumberFormatException ignored) {
                return false;
            }

            if (value <= 0) return false;
            return true;
        }

        private void initActions() {
            this.noVerticesTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (isValueGood(newValue)) {
                    this.generateButton.setDisable(false);
                    this.saveButton.setDisable(false);
                    return;
                }
                this.generateButton.setDisable(true);
                this.saveButton.setDisable(true);
            });


            this.generateButton.setOnMouseClicked(event -> {
                String noVerticesText=noVerticesTextField.getText();
                int noVertices=Integer.parseInt(noVerticesText);
                Country fullGraph=GraphCreator.createFullGraph(noVertices);
                GraphPane.this.controlPanel.setGraphForProcessing(fullGraph);
            });
        }

        private TextField getFormattedTextField(String text) {
            TextField textField=new TextField();
            textField.setPromptText(text);
            textField.setPrefWidth(ControlPanel.WIDTH * 0.6);
            textField.setPrefHeight(25);
            return textField;
        }

        private Button getGenerateButton() {
            Button button=new Button("Generate");
            button.setPrefWidth(ControlPanel.WIDTH * 0.4);
            button.setPrefHeight(50);
            button.setDisable(true);
            return button;
        }

        private Button getSaveButton() {
            Button button=new Button("Save graph");
            button.setPrefHeight(25);
            button.setPrefWidth(ControlPanel.WIDTH);
            button.setDisable(true);
            return button;
        }
    }
}
