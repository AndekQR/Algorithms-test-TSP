package app.view.controlPanel;

import app.controller.aco.AcoParameters;
import app.controller.graph.Country;
import app.view.myGraphView.SelectingController;

import java.util.Optional;

public interface Controlling extends ProgressBarController, SelectingController {

    void setGraphForProcessing(Country country);
    Optional<Country> getGraphForProcessing();
    void solveByAco(AcoParameters parameters);
}
