import app.controller.graph.City;
import app.controller.graph.Country;
import app.controller.graph.Road;
import app.controller.utils.GraphCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class GraphCreatorTest {

    static GraphCreator graphCreator;

    @BeforeAll
    static void init() {
        graphCreator=new GraphCreator();
    }

    @Test
    void checkGraph() {
        int vertices=100;
        Country fullGraph=graphCreator.createFullGraph(vertices);
        int createdEdges = 0;
        for (City city : fullGraph.getCities()) {
            createdEdges += city.getDirections().size();
        }
        int should=(vertices * (vertices - 1)) / 2;
        assertEquals(vertices, fullGraph.countrySize().intValue());
        assertEquals(should, createdEdges/2);

        List<Road> collect=fullGraph.getCities().stream()
                .flatMap(city -> city.getDirections().values().stream())
                .collect(Collectors.toList());
        Set<Road> badEdges=collect.stream().filter(i -> Collections.frequency(collect, i) != 2)
                .collect(Collectors.toSet());
        assertTrue(badEdges.isEmpty());
    }

}
