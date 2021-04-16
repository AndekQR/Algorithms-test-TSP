package app.view.controlPanel;

import app.controller.aco.AcoParameters;
import app.controller.graph.Country;

import java.util.Optional;

public interface Controlling extends ProgressBarController {

    void setGraphForProcessing(Country country);
    Optional<Country> getGraphForProcessing();
    void solveByAco(AcoParameters parameters);
}
