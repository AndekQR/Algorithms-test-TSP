package app.controller.utils;

import app.controller.graph.City;
import app.controller.graph.Country;
import app.controller.graph.RedundantCityName;
import app.controller.helpers.Helpers;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Slf4j
public class GraphCreator {

    private final ExecutorService executorService = Executors.newFixedThreadPool(6);

    public Country createFullGraph(int vertices, String name) {

        Country country = new Country(name);

        IntStream.range(0, vertices).forEach(i -> {
            try {
                country.createCity(String.valueOf(++i));
            } catch (Exception ignored) {
//                log.error(ignored.getLocalizedMessage());
            }
        });


        List<City> cities = country.getCities();
        for (City city : cities) {
            executorService.submit(() -> {
                for (City city1 : cities) {
                    try {
                        country.addEdge(city, city1, Helpers.getRandomNumber(0, 50),
                                Helpers.getRandomNumber(0, 15));
                    } catch (RedundantCityName ignored) {
//                            log.info(ignored.getLocalizedMessage());
                    }
                }
            });
        }

        Helpers.awaitTerminationAfterShutdown(executorService);

        log.info("Graph has been generated!");
        return country;
    }

    private Set<String> getCitiesNames(int number) {
        Faker faker = new Faker();
        Set<String> names = new HashSet<>();
        while (names.size() < number) {
            names.add(faker.address().city());
        }
        return names;
    }
}
