package app.controller.utils;

import app.controller.graph.City;
import app.controller.graph.Road;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class AlgorithmResult {

    private final List<Road> bestRoad;
    private final List<City> bestRoadAsCities;
}
