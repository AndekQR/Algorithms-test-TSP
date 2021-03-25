package app.aco;


import app.countryCopy.CityCopy;
import app.countryCopy.RoadCopy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Getter
@Slf4j
public class Ant {

    private CityCopy currentCity;
    private final CityCopy initialCity;
    private RoadCopy lastTakenRoad;

    private final List<RoadCopy> visitedRoads=new LinkedList<>();
    private final List<CityCopy> visitedCities=new LinkedList<>();
    private final List<CityCopy> unvisitedCities=new LinkedList<>();

    private Double trailWeight=0.0;

    public Ant(CityCopy initialCity, Collection<CityCopy> cities) {
        this.currentCity=initialCity;
        this.initialCity=initialCity;
        this.visitedCities.add(initialCity);

        this.unvisitedCities.addAll(cities);
    }

    public void visitCity(CityCopy city) {
        Optional<RoadCopy> optionalRoad=this.getRoad(currentCity, city);

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

    private Optional<RoadCopy> getRoad(CityCopy city1, CityCopy city2) {
//        return city1.getRoads().stream().filter(roadElement -> roadElement.getDirection().equals(city2)).findFirst();
        return Optional.ofNullable(city1.getDirections().get(city2));
    }

    public boolean isVisitPossible(CityCopy city) {
        if (!city.equals(initialCity)) {
            return this.unvisitedCities.contains(city) && this.getRoad(currentCity, city).isPresent();
        } else {
            return this.unvisitedCities.size() == 1 && !this.currentCity.equals(city);
        }
    }
}
