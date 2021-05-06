package app.controller.algorithms.aco;

import app.controller.algorithms.Algorithm;
import app.controller.graph.City;
import app.controller.graph.Country;
import app.controller.graph.Road;
import app.controller.utils.AlgorithmResult;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AcoAlgorithm implements Algorithm {

    private final Country country;
    private final Random random = new Random();
    private final Collection<Ant> ants = new ArrayList<>();
    private final AcoParameters acoParameters;
    private Ant bestAnt;

    public AcoAlgorithm(Country country, AcoParameters acoParameters) {
        this.country = country;
        this.acoParameters = acoParameters;
    }

    @Override
    public AlgorithmResult solve() {
        IntStream.range(0, acoParameters.getGenerations()).forEach(i -> {
            this.initAnts();
            for (int i1 = 0; i1 < this.country.getCities().size(); i1++) {
                this.moveAnts();
                this.updateRoads();
                this.updateBestAnt();
            }
        });
        return new AlgorithmResult(getBestRoad(), getBestRoadAsCities());
    }

    private List<City> getBestRoadAsCities() {
        return bestAnt.getVisitedCities();
    }

    private List<Road> getBestRoad() {
        return bestAnt.getVisitedRoads();
    }

    private void updateRoads() {
        for (City city : this.country.getCities()) {
            city.getDirections().forEach((cityCopy1, roadCopy) -> roadCopy.evaporatePheromones(acoParameters.getEvaporation()));
        }
        this.ants.forEach(ant -> {
            double currentRoadWeight = ant.getCurrentRoadsWeight();
            if (Double.compare(currentRoadWeight, 0) == 0) ant.getLastTakenRoad().addPheromone(acoParameters.getQ());
            else ant.getLastTakenRoad().addPheromone(acoParameters.getQ() / ant.getCurrentRoadsWeight());
        });
    }

    private void updateBestAnt() {
        this.bestAnt = this.ants.stream().sorted(Comparator.comparingDouble(Ant::getCurrentRoadsWeight)).findFirst().get();
    }

    private void moveAnts() {
        this.ants.forEach(ant -> {
            boolean visitedRandomCity = false;

            if (this.random.nextDouble() < acoParameters.getRandomFactor()) {
                int cityIndex = this.random.nextInt(this.country.countrySize() - 1); //TODO: może być problem z countrySIze
                City randomCity = this.country.getCities().get(cityIndex);

                if (ant.isVisitPossible(randomCity)) {
                    ant.visitCity(randomCity);
                    visitedRandomCity = true;
                }
            }

            if (!visitedRandomCity) {
                Map<City, Double> cityProbabilityMap = ant.getUnvisitedCities().stream()
                        .filter(ant::isVisitPossible)
                        .collect(Collectors.toMap(
                                city -> city,
                                city -> this.calculateProbability(ant, city)
                        ));

                if (!cityProbabilityMap.isEmpty()) {
                    Optional<Map.Entry<City, Double>> highestProbability =
                            cityProbabilityMap.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue));
                    ant.visitCity(highestProbability.get().getKey());
                }
            }
        });
    }

    private Double calculateProbability(Ant ant, City city) {
        City antCurrentCity = ant.getCurrentCity();
        Optional<Road> road = this.getRoad(city, antCurrentCity);
        if (road.isPresent()) {
            double visibility = 1 / road.get().getDistance();
            double intensity = road.get().getPheromone();
            return Math.pow(visibility, acoParameters.getBeta()) * Math.pow(intensity, acoParameters.getAlpha());
        }
        return 0.0;
    }

    private Optional<Road> getRoad(City city1, City city2) {
        return Optional.ofNullable(city1.getDirections().get(city2));
    }

    private void initAnts() {
        List<City> cityList = this.country.getCities();
        IntStream.range(0, acoParameters.getAnts()).forEach(i -> {
            City randomCity = cityList.get(this.random.nextInt(cityList.size() - 1));
            Ant ant = new Ant(randomCity, cityList);
            this.ants.add(ant);
        });
    }


}
