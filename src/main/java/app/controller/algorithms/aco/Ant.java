package app.controller.algorithms.aco;


import app.controller.graph.City;
import app.controller.graph.Road;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Getter
@Slf4j
public class Ant {

    private final City initialCity;
    private final List<Road> visitedRoads = new LinkedList<>();
    private final List<City> visitedCities = new LinkedList<>();
    private final List<City> unvisitedCities = new LinkedList<>();
    private City currentCity;
    private Road lastTakenRoad;
    private Double currentRoadsWeight = 0.0;


    public Ant(City initialCity, Collection<City> cities) {
        this.currentCity = initialCity;
        this.initialCity = initialCity;
        this.visitedCities.add(initialCity);
        this.unvisitedCities.addAll(cities);
    }

    public void visitCity(City city) {
        Optional<Road> optionalRoad = this.getRoad(currentCity, city);

        optionalRoad.ifPresent(road -> {
            this.currentRoadsWeight += road.getDistance();
            this.visitedRoads.add(road);

            this.currentCity = city;
            this.lastTakenRoad = road;
            this.unvisitedCities.remove(city);
            this.visitedCities.add(city);
        });

    }

    private Optional<Road> getRoad(City city1, City city2) {
        return Optional.ofNullable(city1.getDirections().get(city2));
    }

    public boolean isVisitPossible(City city) {
        return !visitedCities.contains(city) && getRoad(currentCity, city).isPresent();

//        if (!city.equals(initialCity)) {
//            return this.unvisitedCities.contains(city) && this.getRoad(currentCity, city).isPresent();
//        } else {
//            return this.unvisitedCities.size() == 1 && !this.currentCity.equals(city);
//        }
    }
}
