package app.controller.graph;

import app.view.myGraphView.DrawableCell;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class City extends DrawableCell {

    private Map<City, Road> directions;

    public City(String cityName) {
        this.name=cityName;
        this.directions=new HashMap<>();
        this.initView();
    }

    public void addRoad(City direction, Road road) {
        if (direction.getName().equals(name)) return;
        directions.put(direction, road);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("City name: " + name + "\n");
        directions.forEach((cityCopy, roadCopy) -> {
            stringBuilder.append(name).append(" -" + roadCopy.getDistance() + "-> ").append(cityCopy.getName()).append(
                    "\n");
        });
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;

        City city=(City) o;

        if (!name.equals(city.name)) return false;
        return directions.equals(city.directions);
    }

    @Override
    public int hashCode() {
        int result=name.hashCode();
        result=31 * result;
        return result;
    }

}
