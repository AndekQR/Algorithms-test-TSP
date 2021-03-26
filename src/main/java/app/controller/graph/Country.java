package app.controller.graph;

import app.view.myGraphView.GraphViewUtilities;
import javafx.scene.Node;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
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

        Road oneToTwoRoad=Road.builder()
                .distance(weight)
                .pheromone(initialPheromone)
                .build();

        vertexOne.get().addRoad(vertexTwo.get(), oneToTwoRoad);
        vertexTwo.get().addRoad(vertexOne.get(), oneToTwoRoad);

    }

    private boolean vertexExists(String label) {
        return this.cities.stream().anyMatch(city -> city.getName().equals(label));
    }

    private boolean roadExists(String cityOneName, String cityTwoName) throws RedundantCityName {
        City city=getCity(cityOneName);
        City direction=getCity(cityTwoName);
        return city.getDirections().containsKey(direction);
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
        this.initView();

        for (City city : this.cities) {
            this.addCellToDraw(city);
            city.getDirections().forEach((cityCopy1, roadCopy) -> {
                this.addEdgeToDraw(roadCopy, city, cityCopy1);
            });
        }
        this.drawAddedNodes();
        this.makeCellsDraggable();
    }

    @Override
    public Node getView() {
        addNodesToView();
        return this.getRootScrollPane();
    }
}
