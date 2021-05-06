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

public class AlgorithmsMediator {

    public AlgorithmsMediator() {

    }

    public AlgorithmResult solveByAcoAlgorithm(Country country, AcoParameters acoParameters) {
        Algorithm acoAlgorithm = new AcoAlgorithm(country, acoParameters);
        return acoAlgorithm.solve();
    }

    public AlgorithmResult solveBySimulatedAnnealing(Country country, SimulatedAnnealingParameters parameters) {
        Algorithm simulatedAnnealingAlgorithm = new SimulatedAnnealingAlgorithm(country, parameters);
        return simulatedAnnealingAlgorithm.solve();
    }

    public AlgorithmResult solveByTabuSearchAlgorithm(Country country, TabuSearchParameters parameters) {
        Algorithm tabuSearchAlgorithm = new TabuSearchAlgorithm(country, parameters);
        return tabuSearchAlgorithm.solve();
    }

    public AlgorithmResult solveByNearestNeighbourAlgorithm(Country country) {
        Algorithm nnAlgorithm = new NearestNeighbourAlgorithm(country);
        return nnAlgorithm.solve();
    }
}
