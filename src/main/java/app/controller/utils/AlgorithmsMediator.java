package app.controller.utils;


import app.controller.algorithms.Algorithm;
import app.controller.algorithms.aco.AcoAlgorithm;
import app.controller.algorithms.aco.AcoParameters;
import app.controller.algorithms.nn.NearestNeighbourAlgorithm;
import app.controller.algorithms.sa.SimulatedAnnealingAlgorithm;
import app.controller.algorithms.sa.SimulatedAnnealingParameters;
import app.controller.algorithms.ts.TabuSearchAlgorithm;
import app.controller.algorithms.ts.TabuSearchParameters;
import app.controller.graph.Country;
import app.controller.utils.algorithmListener.AlgorithmEventListener;

public class AlgorithmsMediator {

    public AlgorithmsMediator() {

    }

    public AlgorithmResult solveByAcoAlgorithm(Country country, AcoParameters acoParameters,
                                               AlgorithmEventListener algorithmEventListener) {
        Algorithm acoAlgorithm = new AcoAlgorithm(country, acoParameters);
        return acoAlgorithm.solve(algorithmEventListener);
    }

    public AlgorithmResult solveBySimulatedAnnealing(Country country, SimulatedAnnealingParameters parameters,
                                                     AlgorithmEventListener algorithmEventListener) {
        Algorithm simulatedAnnealingAlgorithm = new SimulatedAnnealingAlgorithm(country, parameters);
        return simulatedAnnealingAlgorithm.solve(algorithmEventListener);
    }

    public AlgorithmResult solveByTabuSearchAlgorithm(Country country, TabuSearchParameters parameters,
                                                      AlgorithmEventListener algorithmEventListener) {
        Algorithm tabuSearchAlgorithm = new TabuSearchAlgorithm(country, parameters);
        return tabuSearchAlgorithm.solve(algorithmEventListener);
    }

    public AlgorithmResult solveByNearestNeighbourAlgorithm(Country country,
                                                            AlgorithmEventListener algorithmEventListener) {
        Algorithm nnAlgorithm = new NearestNeighbourAlgorithm(country);
        return nnAlgorithm.solve(algorithmEventListener);
    }
}
