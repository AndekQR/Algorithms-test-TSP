package app.controller.algorithms.sa;

import app.controller.graph.City;
import app.controller.graph.Country;
import app.controller.graph.Road;
import app.controller.helpers.Helpers;
import app.controller.utils.AlgorithmResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SimulatedAnnealingAlgorithm {

    private final Country country;
    private final SimulatedAnnealingParameters params;
    private double actualTemperature;
    private List<City> bestSolution;

    public SimulatedAnnealingAlgorithm(Country country, SimulatedAnnealingParameters params) {
        this.country = country;
        this.params = params;

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
            double actualSolutionDistance = calculateSolutionDistance(actualSolution);
            double newSolutionDistance = calculateSolutionDistance(newSolution);
            if (acceptactionProbability(actualSolutionDistance, newSolutionDistance) > Helpers.getRandomNumber(0D, 1D)) {
                actualSolution = new ArrayList<>(newSolution);
            }
            double bestSolutionDistance = calculateSolutionDistance(bestSolution);
            double newActualSolutionDistance = calculateSolutionDistance(actualSolution);

            if (newActualSolutionDistance < bestSolutionDistance) {
                bestSolution = new ArrayList<>(actualSolution);
            }
            actualTemperature = actualTemperature * (1 - params.getCoolingRate());
        }
        return new AlgorithmResult(getByRoads(bestSolution), bestSolution);
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

    private double calculateSolutionDistance(List<City> solution) {
        AtomicReference<Double> result = new AtomicReference<>(0D);
        Helpers.tupleIterator(solution, (city, city2) -> {
            Road road = city.getDirections().get(city2);
            result.updateAndGet(v -> v + road.getDistance());
        });
        return result.get();
    }

    private double acceptactionProbability(double actualSolutionDistance, double newSolutionDistance) {
        if (newSolutionDistance < actualSolutionDistance) return 1D;
        return Math.exp((actualSolutionDistance - newSolutionDistance) / actualTemperature);
    }

    private List<Road> getByRoads(List<City> cities) {
        List<Road> roads = new ArrayList<>();
        Helpers.tupleIterator(cities, (city, city2) -> {
            Road road = city.getDirections().get(city2);
            roads.add(road);
        });
        return roads;
    }

}
