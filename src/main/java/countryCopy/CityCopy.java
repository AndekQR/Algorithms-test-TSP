package countryCopy;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class CityCopy {
    private String name;
    private Map<CityCopy, RoadCopy> directions;

    public CityCopy(String cityName) {
        this.name = cityName;
        this.directions=new HashMap<>();
    }

    public void addRoad(CityCopy direction, RoadCopy road) {
        if (direction.getName().equals(name)) return;
        directions.put(direction, road);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("City name: "+name+"\n");
        directions.forEach((cityCopy, roadCopy)-> {
            stringBuilder.append(name).append(" -"+roadCopy.getDistance()+"-> ").append(cityCopy.getName()).append(
                    "\n");
        });
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CityCopy)) return false;

        CityCopy cityCopy=(CityCopy) o;

        if (!name.equals(cityCopy.name)) return false;
        return directions.equals(cityCopy.directions);
    }

    @Override
    public int hashCode() {
        int result=name.hashCode();
        result=31 * result;
        return result;
    }
}
