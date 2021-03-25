package app;

import app.countryCopy.*;
import app.view.controlPanel.ControlPanel;
import com.github.javafaker.Faker;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.IntStream;

@Slf4j
public class Main extends Application {

    public static double WIDTH=1024;
    public static double HEIGHT=768;

    public static void main(String[] args) {
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
        } catch (EdgeAlreadyExists | CityNotExist | RedundantCityName exception) {
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
        for (CityCopy city : country.getCities()) {
            for (CityCopy city1 : country.getCities()) {
                try {
                    country.addEdge(city.getName(), city1.getName(), faker.number().numberBetween(0, 50),
                            faker.number().randomDouble(2, 0,
                                    50));
                } catch (CityNotExist | RedundantCityName | EdgeAlreadyExists ignored) {

                }
            }

        }
        return country;
    }

    @Override
    public void start(Stage primaryStage) {
        CountryCopy copyGraph=createCopyGraph(5);

        ControlPanel controlPanel=new ControlPanel();

        HBox root=new HBox();
        root.getChildren().addAll(controlPanel, copyGraph.getView());
        Scene scene=new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource("../style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();


//        AcoAlgorithm acoAlgorithm=new AcoAlgorithm(copyGraph);
//        acoAlgorithm.solve();
    }
}
