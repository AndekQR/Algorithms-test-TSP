package countryCopy;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class CountryCopy {

    private List<CityCopy> country=new ArrayList<>();

    public void addEdge(String vertexOneLabel, String vertexTwoLabel, double weight, double initialPheromone) throws BuildingNotExists,
            RedundantCityName, EdgeAlreadyExists {
        Optional<CityCopy> vertexOne=country.stream().filter(city -> city.getName().equals(vertexOneLabel)).findFirst();
        Optional<CityCopy> vertexTwo=
                country.stream().filter(city -> city.getName().equals(vertexTwoLabel)).findFirst();

        if (vertexOneLabel.equals(vertexTwoLabel)) throw new RedundantCityName(vertexOneLabel);
        if (vertexOne.isEmpty()) throw new BuildingNotExists(vertexOneLabel);
        if (vertexTwo.isEmpty()) throw new BuildingNotExists(vertexTwoLabel);

        RoadCopy oneToTwoRoad=RoadCopy.builder()
                .distance(weight)
                .pheromone(initialPheromone)
                .build();

        vertexOne.get().addRoad(vertexTwo.get(), oneToTwoRoad);
        vertexTwo.get().addRoad(vertexOne.get(), oneToTwoRoad);

    }

    private boolean vertexExists(String label) {
        return this.country.stream().anyMatch(city -> city.getName().equals(label));
    }

    private boolean roadExists(String cityOneName, String cityTwoName) throws RedundantCityName {
        CityCopy city=getCity(cityOneName);
        CityCopy direction=getCity(cityTwoName);
        return city.getDirections().containsKey(direction);
    }

    public CityCopy getCity(String label) throws RedundantCityName { //TODO: nie powinno dodawać nowego wierzchołka
        Optional<CityCopy> vertex=this.country.stream().filter(city -> city.getName().equals(label)).findFirst();
        if (vertex.isPresent()) return vertex.get();
        return this.createCity(label);
    }

    public CityCopy getCity(Integer id) {
        return this.getCountry().get(id);
    }

    public RoadCopy getRoad(CityCopy city1, CityCopy city2) {
        RoadCopy roadCopy=city1.getDirections().get(city2);
        if (roadCopy == null) throw new RuntimeException("Road does not exists");
        return roadCopy;
    }

    public Integer countrySize() {
        return this.getCountry().size();
    }

    public CityCopy createCity(String label) throws RedundantCityName {
        if (this.vertexExists(label)) throw new RedundantCityName(label);

        CityCopy city=new CityCopy(label);
        this.country.add(city);
        return city;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder=new StringBuilder();
        country.forEach(stringBuilder::append);
        return stringBuilder.toString();
    }
}
