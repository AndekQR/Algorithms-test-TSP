package app.controller.aco;

import app.controller.graph.City;
import app.controller.graph.Country;
import app.controller.graph.Road;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AcoAlgorithm {

    private final Country country;
    private final Random random = new Random();
    private final Collection<Ant> ants = new ArrayList<>();
    private AcoParameters acoParameters;
    private Ant bestAnt;

    public AcoAlgorithm(Country country, AcoParameters acoParameters) {
        this.country = country;
        this.acoParameters = acoParameters;
    }

    public AcoResult solve() {
        IntStream.range(0, acoParameters.getGenerations()).forEach(i -> {
            this.initAnts();
            for (int i1 = 0; i1 < this.country.getCities().size(); i1++) {
                this.moveAnts();
                this.updateRoads();
                this.updateBestAnt();
            }
        });
        return new AcoResult(getBestRoad(), getBestRoadAsCities());
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
            Double trailWeight = ant.getTrailWeight();
            if (Double.compare(trailWeight, 0) == 0) ant.getLastTakenRoad().addPheromone(acoParameters.getQ());
            else ant.getLastTakenRoad().addPheromone(acoParameters.getQ() / ant.getTrailWeight());
        });
    }

    private void updateBestAnt() {
        this.bestAnt = this.ants.stream().sorted(Comparator.comparingDouble(Ant::getTrailWeight)).findFirst().get();
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
                    Optional<Map.Entry<City, Double>> first =
                            cityProbabilityMap.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue));
                    ant.visitCity(first.get().getKey());
                }
            }
        });
    }

    private Double calculateProbability(Ant ant, City city) {
        City antCurrentCity = ant.getCurrentCity();
        Optional<Road> road = this.getRoad(city, antCurrentCity);
        if (road.isPresent()) {
            Double visibility = 1 / road.get().getDistance();
            Double intensity = road.get().getPheromone();

            return Math.pow(visibility, acoParameters.getBeta()) * Math.pow(intensity, acoParameters.getAlpha());
        }
        return 0.0;
    }

    private Optional<Road> getRoad(City city1, City city2) {
//        return city1.getRoads().stream().filter(roadElement -> roadElement.getDirection().equals(city2)).findFirst();
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
