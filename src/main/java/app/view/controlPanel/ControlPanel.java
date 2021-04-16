package app.view.controlPanel;

import app.controller.aco.AcoParameters;
import app.controller.aco.AcoResult;
import app.controller.graph.City;
import app.controller.graph.Country;
import app.controller.graph.Road;
import app.controller.utils.AlgorithmsMediator;
import app.view.myGraphView.GraphView;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class ControlPanel extends VBox implements Controlling {

    public static double WIDTH = 250;

    private Country selectedGraph = null;
    private final GraphView graphView;
    private final AlgorithmsMediator algorithmsMediator;
    private BooleanProperty progressBarShowing = new SimpleBooleanProperty(false);

    public ControlPanel(GraphView graphView) {
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

    private Optional<Country> getGraphForProcessingCopy() {
        Optional<Country> graphForProcessing = getGraphForProcessing();
        if (graphForProcessing.isEmpty()) return Optional.empty();
        else return Optional.of(new Country(graphForProcessing.get()));
    }

    @Override
    public void setGraphForProcessing(Country country) {
        this.selectedGraph = country;
        graphView.setGraphToShow(this.selectedGraph);
    }

    @Override
    public void solveByAco(AcoParameters parameters) {
        Optional<Country> graphForProcessingCopy = getGraphForProcessingCopy();
        if (graphForProcessingCopy.isEmpty()|| parameters == null) return;
        Country copyCountry = graphForProcessingCopy.get();
        graphView.setGraphToShow(copyCountry);

        AcoResult acoResult = algorithmsMediator.solveByAcoAlgorithm(copyCountry, parameters);
        List<City> cities = acoResult.getBestRoadAsCities(); //miast jest zawsze o jeden więcej od dróg, mrówka wraca
        // do początkowego miasta na końcu!!!!
        List<Road> roads = acoResult.getBestRoad();
       if (!roads.isEmpty()) {
           Platform.runLater(() -> {
               cities.get(0).highlight();
               for (int i = 0; i < roads.size(); i++) {
                   Road road = roads.get(i);
                   String text = i+1+"\n"+"Weight: " +road.getDistance() + "\n" + "Pheromone: " + road.getPheromone();
                   copyCountry.addEdgeToDraw(road, cities.get(i), cities.get(i+1), text);
               }
           });
       }
    }
}
