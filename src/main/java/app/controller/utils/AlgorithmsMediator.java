package app.controller.utils;


import app.controller.aco.AcoAlgorithm;
import app.controller.aco.AcoParameters;
import app.controller.aco.AcoResult;
import app.controller.graph.Country;

public class AlgorithmsMediator {

    public AlgorithmsMediator() {

    }

    public AcoResult solveByAcoAlgorithm(Country country, AcoParameters acoParameters) {
        AcoAlgorithm acoAlgorithm = new AcoAlgorithm(country, acoParameters);
        return acoAlgorithm.solve();
    }
}
