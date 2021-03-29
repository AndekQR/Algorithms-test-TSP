package app.controller.graph;

import app.controller.helpers.Helpers;
import app.view.myGraphView.GraphViewUtilities;
import javafx.scene.Node;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Getter
@Slf4j
public class Country extends GraphViewUtilities {

    private final List<City> cities=new ArrayList<>();

    public void addEdge(String vertexOneLabel, String vertexTwoLabel, double weight, double initialPheromone) throws CityNotExist,
            RedundantCityName, EdgeAlreadyExists {
        Optional<City> vertexOne=cities.stream().filter(city -> city.getName().equals(vertexOneLabel)).findFirst();
        Optional<City> vertexTwo=
                cities.stream().filter(city -> city.getName().equals(vertexTwoLabel)).findFirst();

        if (vertexOneLabel.equals(vertexTwoLabel)) throw new RedundantCityName(vertexOneLabel);
        if (vertexOne.isEmpty()) throw new CityNotExist(vertexOneLabel);
        if (vertexTwo.isEmpty()) throw new CityNotExist(vertexTwoLabel);

        Road road=new Road(weight, initialPheromone, vertexOneLabel + "-" + vertexTwoLabel);

        vertexOne.get().addRoad(vertexTwo.get(), road);
        vertexTwo.get().addRoad(vertexOne.get(), road);

    }

    public void addEdge(City city1, City city2, double weight, double initialPheromone) throws RedundantCityName {

        if (city1.equals(city2)) throw new RedundantCityName(city1.getName());

        String name=city1.getName() + "-" + city2.getName();
        Road road=new Road(weight, initialPheromone, name);
        synchronized (this) {
            city1.addRoad(city2, road);
            city2.addRoad(city1, road);
//            road.initLine(city1, city2); TODO: krawędź można incjalizować podczas jej wyróżniania
        }
    }

    private boolean vertexExists(String label) {
        return this.cities.stream().anyMatch(city -> city.getName().equals(label));
    }

    private boolean roadExists(City cityOne, City cityTwo) {
        return cityOne.getDirections().containsKey(cityTwo);
    }

    public City getCity(String label) throws RedundantCityName { //TODO: nie powinno dodawać nowego wierzchołka
        Optional<City> vertex=this.cities.stream().filter(city -> city.getName().equals(label)).findFirst();
        if (vertex.isPresent()) return vertex.get();
        return this.createCity(label);
    }

    public City getCity(Integer id) {
        return this.getCities().get(id);
    }

    public Road getRoad(City city1, City city2) {
        Road road=city1.getDirections().get(city2);
        if (road == null) throw new RuntimeException("Road does not exists");
        return road;
    }

    public Integer countrySize() {
        return this.getCities().size();
    }

    public City createCity(String label) throws RedundantCityName {
        if (this.vertexExists(label)) throw new RedundantCityName(label);

        City city=new City(label);
        this.cities.add(city);
        return city;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder=new StringBuilder();
        cities.forEach(stringBuilder::append);
        return stringBuilder.toString();
    }

    //borderPane | scrollPane | canvas(Group) | cellLayer(Pane)
    public void addNodesToView() {
        ExecutorService executorService=Executors.newFixedThreadPool(12);
        this.initView();

        log.info("Adding nodes to view...");

        for (City city : this.cities) {
            this.addCellToDraw(city);
        }

        for (City city : this.cities) {
            city.getDirections().forEach((cityCopy1, roadCopy) -> {
                this.addEdgeToDraw(roadCopy, city, cityCopy1);
            });
        }
        log.info("Adding nodes to view completed");
        this.drawAddedNodes();
        executorService.submit(this::makeCellsDraggable);

        Helpers.awaitTerminationAfterShutdown(executorService);
    }

    @Override
    public Node getView() {
        addNodesToView();
        return this.getRootScrollPane();
    }
}
