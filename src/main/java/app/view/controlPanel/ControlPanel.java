package app.view.controlPanel;

import app.controller.aco.AcoParameters;
import app.controller.aco.AcoResult;
import app.controller.graph.City;
import app.controller.graph.Country;
import app.controller.graph.Road;
import app.controller.utils.AlgorithmsMediator;
import app.view.myGraphView.GraphView;
import javafx.application.Platform;
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
    private GraphView graphView;
    private AlgorithmsMediator algorithmsMediator;
    private boolean progressBarShowing = false;

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
        if (progressBarShowing) return;

        progressBarShowing = true;

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
        if (progressBarShowing) {
            Platform.runLater(() -> {
                this.getChildren().remove(0);
                progressBarShowing = false;
            });
        }
    }

    @Override
    public Optional<Country> getGraphForProcessing() {
        return Optional.ofNullable(selectedGraph);
    }

    @Override
    public void setGraphForProcessing(Country country) {
        this.selectedGraph = country;
        graphView.setGraphToShow(this.selectedGraph);
    }

    @Override
    public void solveByAco(AcoParameters parameters) {
        if (getGraphForProcessing().isEmpty()|| parameters == null) return;
        Country country = getGraphForProcessing().get();

        AcoResult acoResult = algorithmsMediator.solveByAcoAlgorithm(country, parameters);
        List<City> cities = acoResult.getBestRoadAsCities(); //miast jest zawsze o jeden więcej od dróg, mrówka wraca
        // do początkowego miasta na końcu!!!!
        List<Road> roads = acoResult.getBestRoad();
        Platform.runLater(() -> {
            country.clearEdges();
            for (int i = 0; i < roads.size(); i++) {
                Road road = roads.get(i);
                country.addEdgeToDraw(road, cities.get(i), cities.get(i+1));
            }
        });
    }
}
