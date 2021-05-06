package app.controller.algorithms.ts;

import app.controller.algorithms.AlgorithmsUtils;
import app.controller.graph.City;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.*;

@EqualsAndHashCode(exclude = "algorithmUtils")
public class Swap {

    @Getter
    private final Set<City> swapSet;

    private final AlgorithmsUtils algorithmUtils;

    public Swap(City cityOne, City cityTwo) {
        swapSet = new HashSet<>();
        swapSet.add(cityOne);
        swapSet.add(cityTwo);
        this.algorithmUtils = new AlgorithmsUtils();
    }

    /**
     * zwraca nowy dystans po zastosowaniu swapa
     */
    public double distanceAfterSwapOn(final List<City> cityList) {
        ArrayList<City> cities = new ArrayList<>(cityList);
        ArrayList<City> citiesCopy = new ArrayList<>(swapSet);
        algorithmUtils.swap(cities, citiesCopy.get(0), citiesCopy.get(1));
        return algorithmUtils.calculateSolutionDistance(cities);
    }

    /**
     * zwraca nowe rozwiązanie, po zastosowaniu swapa
     */
    public List<City> solutionAfterSwapOn(final List<City> cityList) {
        ArrayList<City> cities = new ArrayList<>(cityList);
        ArrayList<City> citiesCopy = new ArrayList<>(swapSet);
        algorithmUtils.swap(cities, citiesCopy.get(0), citiesCopy.get(1));
        return cities;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (City city : this.swapSet) {
            stringBuilder.append(city.getName());
            stringBuilder.append("     ");
        }
        return stringBuilder.toString();
    }
}
