package app.view.controlPanel;

import app.controller.aco.AcoParameters;
import app.controller.aco.AcoResult;
import app.controller.graph.City;
import app.controller.graph.Country;
import app.controller.graph.Road;
import app.controller.helpers.LineType;
import app.controller.utils.AlgorithmsMediator;
import app.view.myGraphView.DrawableCell;
import app.view.myGraphView.GraphView;
import app.view.myGraphView.SelectingController;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ControlPanel extends VBox implements Controlling {

    public static double WIDTH = 250;
    private final GraphView graphView;
    private final AlgorithmsMediator algorithmsMediator;
    private Country selectedGraph = null;
    private Country graphUsedNow = null;
    private final BooleanProperty progressBarShowing = new SimpleBooleanProperty(false);
    private final List<DrawableCell> selectedCells;
    private Road selectedRoad;

    public ControlPanel(GraphView graphView) {
        selectedCells = new ArrayList<>();
        this.graphView = graphView;
        this.algorithmsMediator = new AlgorithmsMediator();
        this.init();
    }

    private void init() {
        this.setPrefWidth(WIDTH);
        this.setStyle("-fx-background-color: #e0afa0");
        this.setAlignment(Pos.TOP_CENTER);

        AlgorithmsTabPane algorithmsTabPane = new AlgorithmsTabPane(this);
        this.getChildren().add(algorithmsTabPane);

    }

    /**
     * Wywoływać tylko z wątku JavaFX
     */
    @Override
    public void showProgressBar() {
        if (progressBarShowing.get()) return;

        progressBarShowing.set(true);

        StackPane stackPane = new StackPane();
        stackPane.setBackground(new Background(new BackgroundFill(Color.valueOf("#94949B"), null, null)));
        stackPane.setStyle("-fx-padding: 5px");

        ImageView imageView = new ImageView();
        Image image = new Image(this.getClass().getResource("../../../progress_bar.gif").toExternalForm());
        imageView.setImage(image);
        imageView.setFitWidth(ControlPanel.WIDTH - 50);
        imageView.setFitHeight(40);

        stackPane.getChildren().add(imageView);

        this.getChildren().add(0, stackPane);

    }


    @Override
    public void hideProgressBar() {
        if (progressBarShowing.get()) {
            Platform.runLater(() -> {
                this.getChildren().remove(0);
                progressBarShowing.set(false);
            });
        }
    }

    @Override
    public BooleanProperty getProgressBarProperty() {
        return progressBarShowing;
    }

    @Override
    public Optional<Country> getGraphForProcessing() {
        return Optional.ofNullable(selectedGraph);
    }

    @Override
    public void setGraphForProcessing(Country country) {
        this.selectedGraph = country;
        this.graphUsedNow = country;
        setCellSelectable(selectedGraph.getCities());
        graphView.setGraphToShow(this.selectedGraph);
    }

    private void setCellSelectable(List<City> cities) {
        for (City city : cities) {
            city.initSelecting(this);
        }
    }

    private Optional<Country> getGraphForProcessingCopy() {
        Optional<Country> graphForProcessing = getGraphForProcessing();
        if (graphForProcessing.isEmpty()) return Optional.empty();
        else {
            Country country = new Country(graphForProcessing.get());
            graphUsedNow = country;
            setCellSelectable(country.getCities());
            graphView.setGraphToShow(country);
            return Optional.of(country);
        }
    }

    @Override
    public void select(DrawableCell cell) {
        if (selectedCells.size() >= 2) {
            ArrayList<DrawableCell> copySelectedCells = new ArrayList<>(selectedCells);
            for (DrawableCell copySelectedCell : copySelectedCells) {
                copySelectedCell.unSelect();
            }
            selectedRoad.removeNormalLine();
        }
        selectedCells.add(cell);

        if (selectedCells.size() == 2) {
            if (graphUsedNow != null) {
                DrawableCell drawableCell = selectedCells.get(0);
                DrawableCell drawableCellOne = selectedCells.get(1);
                Optional<Road> road = graphUsedNow.getRoad(drawableCell.getName(), drawableCellOne.getName());
                if (road.isPresent()) {
                    Road road1 = road.get();
                    String text =
                            "Weight: " + road1.getDistance() + "\n" + "Pheromone: " + road1.getPheromone();
                    graphUsedNow.addEdgeToDraw(road1, drawableCell, drawableCellOne, text, LineType.NORMAL);
                    selectedRoad = road1;
                }
            }
        }
    }

    @Override
    public void unSelect(DrawableCell cell) {
        selectedCells.remove(cell);
        if (selectedRoad != null) selectedRoad.removeNormalLine();
    }


    @Override
    public void solveByAco(AcoParameters parameters) {
        Optional<Country> graphForProcessingCopy = getGraphForProcessingCopy();
        if (graphForProcessingCopy.isEmpty() || parameters == null) return;

        AcoResult acoResult = algorithmsMediator.solveByAcoAlgorithm(graphUsedNow, parameters);
        List<City> cities = acoResult.getBestRoadAsCities(); //miast jest zawsze o jeden więcej od dróg, mrówka wraca
        // do początkowego miasta na końcu!!!!
        List<Road> roads = acoResult.getBestRoad();
        if (!roads.isEmpty()) {
            Platform.runLater(() -> {
                cities.get(0).highlight();
                for (int i = 0; i < roads.size(); i++) {
                    Road road = roads.get(i);
                    String text = i + 1 + "\n" + "Weight: " + road.getDistance() + "\n" + "Pheromone: " + road.getPheromone();
                    graphUsedNow.addEdgeToDraw(road, cities.get(i), cities.get(i + 1), text, LineType.HIGHLIGHTED);
                }
            });
        }
    }
}
