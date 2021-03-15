import aco.AcoAlgorithm;
import com.github.javafaker.Faker;
import countryCopy.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import view.*;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class Main extends Application {
    public static void main(String[] args) {
//        Country randomCountry=createRandomCountry(5);
//        Country myCountry = getMyCountry();
//        System.out.println(getMyCountry());

//        Country myCountry=createFullGraph(5);
//        System.out.println(myCountry);
//        System.out.println("------------------------");
//        AcoAlgorithm acoAlgorithm = new AcoAlgorithm(myCountry);
//        acoAlgorithm.solve();

        launch(args);


    }

    private static CountryCopy getMyCountryCopy() {
        CountryCopy country=new CountryCopy();

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
        } catch (EdgeAlreadyExists | BuildingNotExists | RedundantCityName exception) {
            exception.printStackTrace();
            log.error(exception.getLocalizedMessage());
        }

        return country;
    }


    private static CountryCopy createCopyGraph(int vertices) {
        Faker faker=new Faker();
        CountryCopy country=new CountryCopy();
        IntStream.range(0, vertices).forEach(i -> {
            try {
                country.createCity(faker.address().city());
            } catch (RedundantCityName ignored) {

            }
        });
        for (CityCopy city : country.getCountry()) {
            for (CityCopy city1 : country.getCountry()) {
                try {
                    country.addEdge(city.getName(), city1.getName(), faker.number().numberBetween(0, 50),
                            faker.number().randomDouble(2, 0,
                                    50));
                } catch (BuildingNotExists | RedundantCityName | EdgeAlreadyExists ignored) {

                }
            }

        }
        return country;
    }

//    private static Country createFullGraph(int vertices) {
//        Faker faker=new Faker();
//        Country country=new Country();
//        IntStream.range(0, vertices).forEach(i -> {
//            try {
//                country.createCity(faker.address().city());
//            } catch (RedundantCityName ignored) {
//
//            }
//        });
//        for (City city : country.getCountry()) {
//            List<City> restOfCities=country.getCountry().stream().filter(city1 -> !city1.equals(city)).collect(Collectors.toList());
//            for (City rest : restOfCities) {
//                try {
//                    country.addEdge(city.getName(), rest.getName(), faker.number().numberBetween(0, 50),
//                            faker.number().randomDouble(2, 0,
//                                    50));
//                } catch (BuildingNotExists | RedundantCityName | EdgeAlreadyExists ignored) {
//
//                }
//            }
//        }
//        return country;
//    }
//
//    private static Country createRandomCountry(int buildings) {
//        Country country=new Country();
//        Faker faker=new Faker(Locale.ENGLISH);
//        for (int i=0; i < buildings; i++) {
//            try {
//                country.createCity(faker.address().city());
//            } catch (RedundantCityName redundantCityName) {
//                log.error(redundantCityName.getLocalizedMessage());
//            }
//        }
//        List<String> cityNames=country.getCountry().stream().map(City::getName).collect(Collectors.toList());
//        country.getCountry().forEach(city -> {
//            int citiesToConnect=faker.number().numberBetween(cityNames.size() / 4, cityNames.size() / 2);
//            for (int i=0; i < citiesToConnect; i++) {
//                int cityIndex=faker.number().numberBetween(0, cityNames.size() - 1);
//                try {
//                    country.addEdge(city.getName(), cityNames.get(cityIndex), 10, 26);
//                } catch (BuildingNotExists | EdgeAlreadyExists | RedundantCityName exception) {
//                    log.error(exception.getLocalizedMessage());
//                }
//            }
//
//        });
//        return country;
//    }

    //    Graph graph = new Graph();
    @Override
    public void start(Stage primaryStage) {
//        CountryCopy copyGraph=createCopyGraph(5);
        CountryCopy copyGraph=getMyCountryCopy();


        Graph graph=GraphViewAdapter.toViewGraph(copyGraph);

        BorderPane root=new BorderPane();
        root.setCenter(graph.getScrollPane());
        Scene scene=new Scene(root, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

//        addGraphComponents();

        Layout layout=new RandomLayout(graph);
        layout.execute();

        AcoAlgorithm acoAlgorithm = new AcoAlgorithm(copyGraph);
        acoAlgorithm.solve();
    }

}
