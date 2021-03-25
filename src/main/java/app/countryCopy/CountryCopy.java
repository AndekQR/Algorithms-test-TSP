package app.countryCopy;

import app.Main;
import app.view.controlPanel.ControlPanel;

import app.view.myGraphView.GraphView;
import app.view.myGraphView.MouseGestures;
import app.view.myGraphView.layouts.RandomLayout;
import app.view.myGraphView.layouts.Layout;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class CountryCopy extends GraphView {

    private final List<CityCopy> cities=new ArrayList<>();

    public void addEdge(String vertexOneLabel, String vertexTwoLabel, double weight, double initialPheromone) throws CityNotExist,
            RedundantCityName, EdgeAlreadyExists {
        Optional<CityCopy> vertexOne=cities.stream().filter(city -> city.getName().equals(vertexOneLabel)).findFirst();
        Optional<CityCopy> vertexTwo=
                cities.stream().filter(city -> city.getName().equals(vertexTwoLabel)).findFirst();

        if (vertexOneLabel.equals(vertexTwoLabel)) throw new RedundantCityName(vertexOneLabel);
        if (vertexOne.isEmpty()) throw new CityNotExist(vertexOneLabel);
        if (vertexTwo.isEmpty()) throw new CityNotExist(vertexTwoLabel);

        RoadCopy oneToTwoRoad=RoadCopy.builder()
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
        CityCopy city=getCity(cityOneName);
        CityCopy direction=getCity(cityTwoName);
        return city.getDirections().containsKey(direction);
    }

    public CityCopy getCity(String label) throws RedundantCityName { //TODO: nie powinno dodawać nowego wierzchołka
        Optional<CityCopy> vertex=this.cities.stream().filter(city -> city.getName().equals(label)).findFirst();
        if (vertex.isPresent()) return vertex.get();
        return this.createCity(label);
    }

    public CityCopy getCity(Integer id) {
        return this.getCities().get(id);
    }

    public RoadCopy getRoad(CityCopy city1, CityCopy city2) {
        RoadCopy roadCopy=city1.getDirections().get(city2);
        if (roadCopy == null) throw new RuntimeException("Road does not exists");
        return roadCopy;
    }

    public Integer countrySize() {
        return this.getCities().size();
    }

    public CityCopy createCity(String label) throws RedundantCityName {
        if (this.vertexExists(label)) throw new RedundantCityName(label);

        CityCopy city=new CityCopy(label);
        this.cities.add(city);
        return city;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder=new StringBuilder();
        cities.forEach(stringBuilder::append);
        return stringBuilder.toString();
    }

    private void addNodesToView() {
        for (CityCopy cityCopy : this.cities) {
            this.addCellToDraw(cityCopy);
            cityCopy.getDirections().forEach((cityCopy1, roadCopy) -> {
                this.addEdgeToDraw(roadCopy, cityCopy, cityCopy1);
            });
        }
    }

    //borderPane | scrollPane | canvas(Group) | cellLayer(Pane)
    @Override
    public Node getView() {
        this.initView();
        BorderPane root = new BorderPane();
        root.setMaxWidth(Main.WIDTH - ControlPanel.WIDTH);
        root.setMinWidth(Main.WIDTH - ControlPanel.WIDTH);
        this.addNodesToView();
        this.drawAddedNodes();
        this.makeCellsDraggable();
        root.setCenter(this.getRootScrollPane());
        Layout layout=new RandomLayout(this);
        layout.execute();
        return root;
    }
}
