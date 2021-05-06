package app.view.controlPanel;

import app.controller.algorithms.aco.AcoParameters;
import app.controller.algorithms.sa.SimulatedAnnealingParameters;
import app.controller.algorithms.ts.TabuSearchParameters;
import app.controller.graph.Country;
import app.view.myGraphView.SelectingController;

import java.util.Optional;

public interface Controlling extends ProgressBarController, SelectingController {

    Optional<Country> getGraphForProcessing();

    void setGraphForProcessing(Country country);

    void solveByAco(AcoParameters parameters);

    void solveBySimulatedAnnealing(SimulatedAnnealingParameters parameters);

    void solveByTabuSearch(TabuSearchParameters parameters);

    void solveByNearestNeighbourAlgorithm();
}
