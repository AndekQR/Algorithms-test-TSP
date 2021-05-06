package app.controller.algorithms;

import app.controller.graph.City;
import app.controller.graph.Country;
import app.controller.graph.Road;
import app.controller.helpers.Helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AlgorithmsUtils {

    public List<City> generateRandomPath(final Country country) {
        List<City> cities = country.getCities();
        List<City> result = new ArrayList<>();
        int startCitIndex = Helpers.getRandomNumber(0, cities.size());
        City startCity = cities.get(startCitIndex);
        result.add(startCity);

        List<City> availableCities = new ArrayList<>();
        for (int i = 0; i < cities.size(); i++) {
            if (i != startCitIndex) availableCities.add(cities.get(i));
        }

        while (!availableCities.isEmpty()) {
            int randomNumber = Helpers.getRandomNumber(0, availableCities.size());
            City city = availableCities.get(randomNumber);
            result.add(city);
            availableCities.remove(city);
        }
        return result;
    }

    public double calculateSolutionDistance(final List<City> solution) {
        AtomicReference<Double> result = new AtomicReference<>(0D);
        Helpers.tupleIterator(solution, (city, city2) -> {
            if (!city.equals(city2)) {
                Road road = city.getDirections().get(city2);
                result.updateAndGet(v -> v + road.getDistance());
            }
        });
        return result.get();
    }

    public void swap(List<City> cities, City one, City two) {
        if (!(cities.contains(one) || cities.contains(two))) return;

        int cityOneIndex = cities.indexOf(one);
        int cityTwoIndex = cities.indexOf(two);

        Collections.swap(cities, cityOneIndex, cityTwoIndex);
    }

    public List<Road> solutionByRoads(List<City> cities) {
        List<Road> roads = new ArrayList<>();
        Helpers.tupleIterator(cities, (city, city2) -> {
            Road road = city.getDirections().get(city2);
            roads.add(road);
        });
        return roads;
    }
}
