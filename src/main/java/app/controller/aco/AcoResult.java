package app.controller.aco;

import app.controller.graph.City;
import app.controller.graph.Road;
import lombok.AllArgsConstructor;
import lombok.Getter;


import java.util.List;

@AllArgsConstructor
@Getter
public class AcoResult {
    private List<Road> bestRoad;
    private List<City> bestRoadAsCities;
}
