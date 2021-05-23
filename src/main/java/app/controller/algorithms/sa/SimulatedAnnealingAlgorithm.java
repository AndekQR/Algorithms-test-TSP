package app.controller.algorithms.sa;

import app.controller.algorithms.Algorithm;
import app.controller.algorithms.AlgorithmsUtils;
import app.controller.graph.City;
import app.controller.graph.Country;
import app.controller.helpers.Helpers;
import app.controller.utils.AlgorithmResult;
import app.controller.utils.algorithmListener.AlgorithmEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulatedAnnealingAlgorithm implements Algorithm {

    private final Country country;
    private final SimulatedAnnealingParameters params;
    private double actualTemperature;
    private List<City> bestSolution;
    private final ExecutorService executorService;


    public SimulatedAnnealingAlgorithm(Country country, SimulatedAnnealingParameters params) {
        this.country = country;
        this.params = params;
        this.executorService = Executors.newSingleThreadExecutor();
        actualTemperature = params.getInitialTemperature();
    }

    private List<City> generateInitialSolution() {
        List<City> cities = country.getCities();
        if (cities.size() == 0) throw new IllegalArgumentException("No cities");
        return new ArrayList<>(cities);
    }

    @Override
    public AlgorithmResult solve(AlgorithmEventListener algorithmEventListener) {
        executorService.submit(() -> algorithmEventListener.algorithmStart(params));
        int iteration = 0;
        List<City> actualSolution = generateInitialSolution();
        bestSolution = new ArrayList<>(actualSolution);
        while (actualTemperature > 1) {
            List<City> newSolution = swapRandomCities(actualSolution);
            double actualSolutionDistance = AlgorithmsUtils.calculateSolutionDistance(actualSolution);
            double newSolutionDistance = AlgorithmsUtils.calculateSolutionDistance(newSolution);
            if (acceptactionProbability(actualSolutionDistance, newSolutionDistance) > Helpers.getRandomNumber(0D, 1D)) {
                actualSolution = new ArrayList<>(newSolution);
            }
            double bestSolutionDistance = AlgorithmsUtils.calculateSolutionDistance(bestSolution);
            double newActualSolutionDistance = AlgorithmsUtils.calculateSolutionDistance(actualSolution);

            if (newActualSolutionDistance < bestSolutionDistance) {
                bestSolution = new ArrayList<>(actualSolution);
            }
            actualTemperature = actualTemperature * (1 - params.getCoolingRate());
            int finalIteration = iteration;
            executorService.submit(() -> algorithmEventListener.iterationComplete(bestSolution, finalIteration));
            iteration++;
        }
        executorService.submit(() -> algorithmEventListener.algorithmStop(bestSolution));
        executorService.shutdown();
        return new AlgorithmResult(AlgorithmsUtils.solutionByRoads(bestSolution), bestSolution);
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
