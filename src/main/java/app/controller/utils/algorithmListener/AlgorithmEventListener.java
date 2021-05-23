package app.controller.utils.algorithmListener;

import app.controller.algorithms.aco.AcoParameters;
import app.controller.algorithms.sa.SimulatedAnnealingParameters;
import app.controller.algorithms.ts.TabuSearchParameters;
import app.controller.graph.City;

import java.util.List;

public interface AlgorithmEventListener {

    void iterationComplete(List<City> actualSolution, long iterationNumber);

    void algorithmStart(AcoParameters parameters);

    void algorithmStart(SimulatedAnnealingParameters parameters);

    void algorithmStart(TabuSearchParameters parameters);

    /**
     * Nearest Neighbour Algorithm
     */
    void algorithmStart();

    void algorithmStop(List<City> resultRoad);

    void saveResultToExcelFile();
}
