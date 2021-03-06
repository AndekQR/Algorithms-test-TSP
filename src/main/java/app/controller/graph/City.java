package app.controller.graph;

import app.view.myGraphView.DrawableCell;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Slf4j
@NoArgsConstructor
public class City extends DrawableCell {

    private Map<City, Road> directions;

    public City(String cityName) {
        this.name = cityName;
        this.directions = new ConcurrentHashMap<>();
        this.initView();
    }

    public void addRoad(City direction, Road road) {
        directions.put(direction, road);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("City name: " + name + "  ");
        directions.forEach((cityCopy, roadCopy) -> {
            stringBuilder.append(name).append(" -" + roadCopy.getDistance() + "-> ").append(cityCopy.getName()).append(
                    " | ");
        });
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;

        City city = (City) o;

        if (!name.equals(city.name)) return false;
        return directions.equals(city.directions);
    }

    @Override
    public int hashCode() {
        int result = 31 * name.hashCode();

//        for (City city : this.directions.keySet()) {
//            result+=city.getName().hashCode();
//        }
        result += this.directions.values().hashCode();
        return result;
    }

}
