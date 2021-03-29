package app.view.controlPanel;

import app.controller.graph.City;
import app.controller.graph.Country;
import app.controller.utils.AlgorithmsMediator;
import app.view.myGraphView.GraphView;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class ControlPanel extends VBox implements Controlling {

    public static double WIDTH=250;

    private Country selectedGraph=null;
    private GraphView graphView;
    private AlgorithmsMediator algorithmsMediator;

    public ControlPanel(GraphView graphView) {
        this.graphView=graphView;
        this.algorithmsMediator = new AlgorithmsMediator();
        this.init();

    }

    private void init() {
        this.setPrefWidth(WIDTH);
        this.setStyle("-fx-background-color: #e0afa0");

        AlgorithmsTabPane algorithmsTabPane=new AlgorithmsTabPane(this);
        this.getChildren().add(algorithmsTabPane);
    }

    @Override
    public Optional<Country> getGraphForProcessing() {
        return Optional.of(this.selectedGraph);
    }

    @Override
    public void setGraphForProcessing(Country country) {
        this.selectedGraph= country;
        graphView.setGraphToShow(this.selectedGraph);
    }
}
