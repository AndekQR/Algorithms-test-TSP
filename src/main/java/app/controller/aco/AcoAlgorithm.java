package app.controller.aco;

import app.controller.graph.City;
import app.controller.graph.Country;
import app.controller.graph.Road;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AcoAlgorithm {

    private final Country country;

    private final Random random=new Random();

    private final Collection<Ant> ants=new ArrayList<>();

    private List<Road> bestRoad;
    private Ant bestAnt;

    public AcoAlgorithm(Country country) {
        this.country=country;
    }

    public void solve() {
        IntStream.range(0, AcoParameters.generations).forEach(i -> {
            this.initAnts();
            for (int i1=0; i1 < this.country.getCities().size(); i1++) {
                this.moveAnts();
                this.updateRoads();
                this.updateBestRoad();
            }
        });
        this.bestAnt.getVisitedCities().forEach(System.out::println);
    }

    private void updateRoads() {
        for (City city : this.country.getCities()) {
            city.getDirections().forEach((cityCopy1, roadCopy) -> roadCopy.evaporatePheromones(AcoParameters.evaporation));
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
                int cityIndex=this.random.nextInt(this.country.countrySize() - 1); //TODO: może być problem z countrySIze
                City randomCity=this.country.getCities().get(cityIndex);

                if (ant.isVisitPossible(randomCity)) {
                    ant.visitCity(randomCity);
                    visitedRandomCity=true;
                }
            }

            if (!visitedRandomCity) {
                Map<City, Double> cityProbabilityMap=ant.getUnvisitedCities().stream()
                        .filter(city -> ant.isVisitPossible(city))
                        .collect(Collectors.toMap(
                                city -> city,
                                city -> this.calculateProbability(ant, city)
                        ));

                if (!cityProbabilityMap.isEmpty()) {
                    Optional<Map.Entry<City, Double>> first=
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

    private Double calculateProbability(Ant ant, City city) {
        City antCurrentCity=ant.getCurrentCity();
        Optional<Road> road=this.getRoad(city, antCurrentCity);
        if (road.isPresent()) {
            Double visibility=1 / road.get().getDistance();
            Double intensity=road.get().getPheromone();

            return Math.pow(visibility, AcoParameters.beta) * Math.pow(intensity, AcoParameters.alpha);
        }
        return 0.0;
    }

    private Optional<Road> getRoad(City city1, City city2) {
//        return city1.getRoads().stream().filter(roadElement -> roadElement.getDirection().equals(city2)).findFirst();
        return Optional.ofNullable(city1.getDirections().get(city2));
    }

    private void initAnts() {
        List<City> cityList=this.country.getCities();
        IntStream.range(0, AcoParameters.ants).forEach(i -> {
            City randomCity=cityList.get(this.random.nextInt(cityList.size() - 1));
            Ant ant=new Ant(randomCity, cityList);
            this.ants.add(ant);
        });
    }


}
