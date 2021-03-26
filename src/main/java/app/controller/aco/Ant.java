package app.controller.aco;


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

    private City currentCity;
    private final City initialCity;
    private Road lastTakenRoad;

    private final List<Road> visitedRoads=new LinkedList<>();
    private final List<City> visitedCities=new LinkedList<>();
    private final List<City> unvisitedCities=new LinkedList<>();

    private Double trailWeight=0.0;

    public Ant(City initialCity, Collection<City> cities) {
        this.currentCity=initialCity;
        this.initialCity=initialCity;
        this.visitedCities.add(initialCity);

        this.unvisitedCities.addAll(cities);
    }

    public void visitCity(City city) {
        Optional<Road> optionalRoad=this.getRoad(currentCity, city);

        optionalRoad.ifPresentOrElse(road -> {
            this.trailWeight+=road.getDistance();
            this.visitedRoads.add(road);

            this.currentCity=city;
            this.lastTakenRoad=road;
            this.unvisitedCities.remove(city);
            this.visitedCities.add(city);
        }, () -> {
        });

    }

    private Optional<Road> getRoad(City city1, City city2) {
//        return city1.getRoads().stream().filter(roadElement -> roadElement.getDirection().equals(city2)).findFirst();
        return Optional.ofNullable(city1.getDirections().get(city2));
    }

    public boolean isVisitPossible(City city) {
        if (!city.equals(initialCity)) {
            return this.unvisitedCities.contains(city) && this.getRoad(currentCity, city).isPresent();
        } else {
            return this.unvisitedCities.size() == 1 && !this.currentCity.equals(city);
        }
    }
}
