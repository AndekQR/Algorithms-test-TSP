package app.controller.utils;

import app.controller.graph.*;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.IntStream;

@Slf4j
public class GraphCreator {

    public static Country getMyCountryCopy() {
        Country country=new Country();

        try {
            country.createCity("a");
            country.createCity("b");
            country.createCity("c");
            country.createCity("d");
            country.createCity("e");

            country.addEdge("a", "b", 10, 24);
            country.addEdge("a", "c", 10, 24);
            country.addEdge("c", "e", 10, 24);
            country.addEdge("d", "e", 10, 24);
            country.addEdge("d", "b", 10, 24);
            country.addEdge("d", "c", 10, 24);
        } catch (EdgeAlreadyExists | CityNotExist | RedundantCityName exception) {
            exception.printStackTrace();
            log.error(exception.getLocalizedMessage());
        }

        return country;
    }

    public static Country createFullGraph(int vertices) {
        Faker faker=new Faker();
        Country country=new Country();
        IntStream.range(0, vertices).forEach(i -> {
            try {
                country.createCity(faker.address().city());
            } catch (RedundantCityName ignored) {

            }
        });
        for (City city : country.getCities()) {
            for (City city1 : country.getCities()) {
                try {
                    country.addEdge(city.getName(), city1.getName(), faker.number().numberBetween(0, 50),
                            faker.number().randomDouble(2, 0,
                                    50));
                } catch (CityNotExist | RedundantCityName | EdgeAlreadyExists exception) {
                    log.error(exception.getLocalizedMessage());
                }
            }

        }
        return country;
    }
}
