package app.controller.utils;


import app.controller.algorithms.aco.AcoAlgorithm;
import app.controller.algorithms.aco.AcoParameters;
import app.controller.algorithms.sa.SimulatedAnnealingAlgorithm;
import app.controller.algorithms.sa.SimulatedAnnealingParameters;
import app.controller.graph.Country;

public class AlgorithmsMediator {

    public AlgorithmsMediator() {

    }

    public AlgorithmResult solveByAcoAlgorithm(Country country, AcoParameters acoParameters) {
        AcoAlgorithm acoAlgorithm = new AcoAlgorithm(country, acoParameters);
        return acoAlgorithm.solve();
    }

    public AlgorithmResult solveBySimulatedAnnealing(Country country, SimulatedAnnealingParameters parameters) {
        SimulatedAnnealingAlgorithm simulatedAnnealingAlgorithm = new SimulatedAnnealingAlgorithm(country, parameters);
        return simulatedAnnealingAlgorithm.solve();
    }
}
