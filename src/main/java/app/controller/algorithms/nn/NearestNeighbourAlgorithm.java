package app.controller.algorithms.nn;

import app.controller.algorithms.Algorithm;
import app.controller.graph.City;
import app.controller.graph.Country;
import app.controller.graph.Road;
import app.controller.helpers.Helpers;
import app.controller.utils.AlgorithmResult;
import app.controller.utils.algorithmListener.AlgorithmEventListener;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NearestNeighbourAlgorithm implements Algorithm {

    private final Country country;

    private final List<City> bestRoadAsCities;
    private final List<Road> bestRoad;
    private final ExecutorService executorService;


    public NearestNeighbourAlgorithm(Country country) {
        this.country = country;
        this.bestRoad = new ArrayList<>();
        this.bestRoadAsCities = new ArrayList<>();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public AlgorithmResult solve(AlgorithmEventListener algorithmEventListener) {
        executorService.submit(() -> algorithmEventListener.algorithmStart());
        List<City> cities = country.getCities();
        City randomCity = getRandomCity(cities);
        bestRoadAsCities.add(randomCity);
        Optional<Entry<City, Road>> cityWithShortestPath = findCityWithShortestPath(randomCity);
        while (cityWithShortestPath.isPresent()) {
            Entry<City, Road> cityRoadEntry = cityWithShortestPath.get();
            bestRoadAsCities.add(cityRoadEntry.getKey());
            bestRoad.add(cityRoadEntry.getValue());
            cityWithShortestPath = findCityWithShortestPath(cityRoadEntry.getKey());
        }
        executorService.submit(() -> algorithmEventListener.algorithmStop(bestRoadAsCities));
        executorService.shutdown();
        return new AlgorithmResult(bestRoad, bestRoadAsCities);
    }

    private City getRandomCity(List<City> cities) {
        int randomCityIndex = Helpers.getRandomNumber(0, cities.size());
        return cities.get(randomCityIndex);
    }

    private Optional<Entry<City, Road>> findCityWithShortestPath(City origin) {
        Map<City, Road> directions = origin.getDirections();
        return directions.entrySet().stream()
                .filter(cityRoadEntry -> !bestRoadAsCities.contains(cityRoadEntry.getKey()))
                .min(Comparator.comparingDouble(value -> value.getValue().getDistance()));
    }
}
