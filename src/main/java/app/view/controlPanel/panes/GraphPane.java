package app.view.controlPanel.panes;

import app.controller.graph.Country;
import app.controller.utils.GraphCreator;
import app.db.Database;
import app.view.controlPanel.ControlPanel;
import app.view.controlPanel.Controlling;
import app.view.controlPanel.ProgressBarController;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Generowanie grafu o określonej ilości wierzchołków
 * Generowanie grafu przez klikanie
 * wyświetanie zapisanych grafów w liście
 */
public class GraphPane extends VBox {

    private final Controlling controlPanel;
    private final ListView<String> listView;
    private final Database database;
    private final ExecutorService executor;

    public GraphPane(Controlling controlPanel) {
        this.controlPanel = controlPanel;
        this.database = new Database();
        this.executor = Executors.newSingleThreadExecutor();
        RandomGraphGenerator randomGraphGenerator = new RandomGraphGenerator();
        this.getChildren().add(randomGraphGenerator);

        this.listView = createListView();
        this.getChildren().add(listView);
    }

    private ListView<String> createListView() {
        ListView<String> listView = new ListView<>();
        List<String> graphsNames = database.getGraphsNames();
        for (String graphsName : graphsNames) {
            listView.getItems().add(graphsName);
        }

        listView.setOnMouseClicked(event -> {
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null && !selectedItem.isBlank()) {
                controlPanel.showProgressBar();
                Country graph = database.getGraph(selectedItem);
                controlPanel.setGraphForProcessing(graph);
                controlPanel.hideProgressBar();
            }
        });
        return listView;
    }

    class RandomGraphGenerator extends GridPane {

        private final TextField noVerticesTextField;
        private final TextField graphNameTextField;
        private final Button generateButton;
        private final Button saveButton;
        private Country latestGeneratedGraph;


        public RandomGraphGenerator() {
            this.noVerticesTextField = getFormattedTextField("Graph vertices");
            this.graphNameTextField = getFormattedTextField("Graph name");
            this.generateButton = getGenerateButton();
            this.saveButton = getSaveButton();

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
                value = Integer.parseInt(intValue);
            } catch (NumberFormatException ignored) {
                return false;
            }

            return value > 0;
        }


        private void initActions() {
            this.noVerticesTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (isValueGood(newValue) && !graphNameTextField.getText().isBlank()) {
                    this.generateButton.setDisable(false);
                    return;
                }
                this.generateButton.setDisable(true);
                this.saveButton.setDisable(true);
            });

            graphNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (isValueGood(noVerticesTextField.getText()) && !newValue.isBlank()) {
                    this.generateButton.setDisable(false);
                    return;
                }
                this.generateButton.setDisable(true);
                this.saveButton.setDisable(true);
            });


            this.generateButton.setOnMouseClicked(event -> {
                String noVerticesText = noVerticesTextField.getText();
                int noVertices = Integer.parseInt(noVerticesText);
                String name = graphNameTextField.getText();
                latestGeneratedGraph = new GraphCreator().createFullGraph(noVertices, name);
                GraphPane.this.controlPanel.setGraphForProcessing(latestGeneratedGraph);
                if (!graphNameTextField.getText().isBlank() && latestGeneratedGraph != null)
                    saveButton.setDisable(false);
            });

            saveButton.setOnMouseClicked(event -> {
                if (latestGeneratedGraph != null) {
                    listView.getItems().add(latestGeneratedGraph.getName());
                    controlPanel.showProgressBar();
                    executor.submit(() -> {
                        database.saveGraph(latestGeneratedGraph);
                        controlPanel.hideProgressBar();
                    });
                    noVerticesTextField.clear();
                    graphNameTextField.clear();
                }
            });
        }


        private TextField getFormattedTextField(String text) {
            TextField textField = new TextField();
            textField.setPromptText(text);
            textField.setPrefWidth(ControlPanel.WIDTH * 0.6);
            textField.setPrefHeight(25);
            return textField;
        }

        private Button getGenerateButton() {
            Button button = new Button("Generate");
            button.setPrefWidth(ControlPanel.WIDTH * 0.4);
            button.setPrefHeight(50);
            button.setDisable(true);
            return button;
        }

        private Button getSaveButton() {
            Button button = new Button("Save graph");
            button.setPrefHeight(25);
            button.setPrefWidth(ControlPanel.WIDTH);
            button.setDisable(true);
            return button;
        }
    }
}
