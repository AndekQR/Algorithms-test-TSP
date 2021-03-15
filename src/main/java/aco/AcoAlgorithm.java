package aco;

import countryCopy.CityCopy;
import countryCopy.CountryCopy;
import countryCopy.RoadCopy;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AcoAlgorithm {

    private final CountryCopy country;

    private Random random=new Random();

    private Collection<Ant> ants=new ArrayList<>();

    private List<RoadCopy> bestRoad;
    private Ant bestAnt;

    public AcoAlgorithm(CountryCopy country) {
        this.country=country;
    }

    public void solve() {
        IntStream.range(0, AcoParameters.generations).forEach(i -> {
            this.initAnts();
            for (int i1=0; i1 < this.country.getCountry().size(); i1++) {
                this.moveAnts();
                this.updateRoads();
                this.updateBestRoad();
            }
        });
        this.bestAnt.getVisitedCities().forEach(System.out::println);
    }

    private void updateRoads() {
        for (CityCopy cityCopy : this.country.getCountry()) {
            cityCopy.getDirections().forEach((cityCopy1, roadCopy) -> roadCopy.evaporatePheromones(AcoParameters.evaporation));
        }
        this.ants.forEach(ant -> ant.getLastTakenRoad().addPheromone(AcoParameters.q / ant.getTrailWeight()));
    }

    private void updateBestRoad() {
        this.bestAnt=this.ants.stream().sorted(Comparator.comparingDouble(Ant::getTrailWeight)).findFirst().get();
        this.bestRoad=this.bestAnt.getVisitedRoads();
    }

    private void moveAnts() {
        this.ants.forEach(ant -> {
            boolean visitedRandomCity=false;

            if (this.random.nextDouble() < AcoParameters.randomFactor) {
                int cityIndex=this.random.nextInt(this.country.countrySize()-1); //TODO: może być problem z countrySIze
                CityCopy randomCity=this.country.getCountry().get(cityIndex);

                if (ant.isVisitPossible(randomCity)) {
                    ant.visitCity(randomCity);
                    visitedRandomCity=true;
                }
            }

            if (!visitedRandomCity) {
                Map<CityCopy, Double> cityProbabilityMap=ant.getUnvisitedCities().stream()
                        .filter(city -> ant.isVisitPossible(city))
                        .collect(Collectors.toMap(
                                city -> city,
                                city -> this.calculateProbability(ant, city)
                        ));

                if (!cityProbabilityMap.isEmpty()) {
                    Optional<Map.Entry<CityCopy, Double>> first=
                            cityProbabilityMap.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue));
//                    Optional<Map.Entry<City, Double>> first=cityProbabilityMap.entrySet().stream().sorted(
//                            (Map.Entry<City, Double> c1, Map.Entry<City,
//                            Double> c2) -> (c2.getValue() > c1.getValue() ? 1 :
//                            (c1.getValue() < c2.getValue() ? -1 : 0))).findFirst();
                    if (first.isPresent()) {
                        ant.visitCity(first.get().getKey());
                    } else {
                        throw new RuntimeException("Cant visit any city");
                    }
                }
            }
        });
    }

    private Double calculateProbability(Ant ant, CityCopy city) {
        CityCopy antCurrentCity=ant.getCurrentCity();
        Optional<RoadCopy> road=this.getRoad(city, antCurrentCity);
        if (road.isPresent()) {
            Double visibility=1 / road.get().getDistance();
            Double intensity=road.get().getPheromone();

            return Math.pow(visibility, AcoParameters.beta) * Math.pow(intensity, AcoParameters.alpha);
        }
        return 0.0;
    }

    private Optional<RoadCopy> getRoad(CityCopy city1, CityCopy city2) {
//        return city1.getRoads().stream().filter(roadElement -> roadElement.getDirection().equals(city2)).findFirst();
        return Optional.ofNullable(city1.getDirections().get(city2));
    }

    private void initAnts() {
        List<CityCopy> cityList=this.country.getCountry();
        IntStream.range(0, AcoParameters.ants).forEach(i -> {
            CityCopy randomCity=cityList.get(this.random.nextInt(cityList.size() - 1));
            Ant ant=new Ant(randomCity, cityList);
            this.ants.add(ant);
        });
    }


}
