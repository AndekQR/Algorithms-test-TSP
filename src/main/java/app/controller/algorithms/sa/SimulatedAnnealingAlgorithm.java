package app.controller.algorithms.sa;

import app.controller.algorithms.AlgorithmsUtils;
import app.controller.graph.City;
import app.controller.graph.Country;
import app.controller.helpers.Helpers;
import app.controller.utils.AlgorithmResult;

import java.util.ArrayList;
import java.util.List;

public class SimulatedAnnealingAlgorithm {

    private final Country country;
    private final SimulatedAnnealingParameters params;
    private final AlgorithmsUtils algorithmsUtils;
    private double actualTemperature;
    private List<City> bestSolution;

    public SimulatedAnnealingAlgorithm(Country country, SimulatedAnnealingParameters params) {
        this.country = country;
        this.params = params;
        this.algorithmsUtils = new AlgorithmsUtils();

        actualTemperature = params.getInitialTemperature();
    }

    private List<City> generateInitialSolution() {
        List<City> cities = country.getCities();
        if (cities.size() == 0) throw new IllegalArgumentException("No cities");
        return new ArrayList<>(cities);
    }

    public AlgorithmResult solve() {
        List<City> actualSolution = generateInitialSolution();
        bestSolution = new ArrayList<>(actualSolution);
        while (actualTemperature > 1) {
            List<City> newSolution = swapRandomCities(actualSolution);
            double actualSolutionDistance = algorithmsUtils.calculateSolutionDistance(actualSolution);
            double newSolutionDistance = algorithmsUtils.calculateSolutionDistance(newSolution);
            if (acceptactionProbability(actualSolutionDistance, newSolutionDistance) > Helpers.getRandomNumber(0D, 1D)) {
                actualSolution = new ArrayList<>(newSolution);
            }
            double bestSolutionDistance = algorithmsUtils.calculateSolutionDistance(bestSolution);
            double newActualSolutionDistance = algorithmsUtils.calculateSolutionDistance(actualSolution);

            if (newActualSolutionDistance < bestSolutionDistance) {
                bestSolution = new ArrayList<>(actualSolution);
            }
            actualTemperature = actualTemperature * (1 - params.getCoolingRate());
        }
        return new AlgorithmResult(algorithmsUtils.solutionByRoads(bestSolution), bestSolution);
    }

    /**
     * zwraca nowe rozwiązanie, nie modyfikując obecnego
     */
    private List<City> swapRandomCities(List<City> solution) {
        List<City> cities = new ArrayList<>(solution);
        int indexOne;
        int indexTwo;
        do {
            indexOne = Helpers.getRandomNumber(0, cities.size());
            indexTwo = Helpers.getRandomNumber(0, cities.size());
        } while (indexOne == indexTwo);

        City cityOne = cities.get(indexOne);
        City cityTwo = cities.get(indexTwo);

        cities.set(indexOne, cityTwo);
        cities.set(indexTwo, cityOne);

        return cities;
    }


    private double acceptactionProbability(double actualSolutionDistance, double newSolutionDistance) {
        if (newSolutionDistance < actualSolutionDistance) return 1D;
        return Math.exp((actualSolutionDistance - newSolutionDistance) / actualTemperature);
    }

}
