import app.controller.graph.City;
import app.controller.graph.Road;
import app.view.myGraphView.DrawableEdge;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoadTest {

    @Test
    void drawableEdgeTest() {
        City cityOne = new City("cityOne");
        City cityTwo = new City("cityTwo");

        Road roadOne = new Road(1, 2, "edgeOne");
        Road roadTwo = new Road(1, 2, "edgeTwo");

        assertNotEquals(cityOne, cityTwo);
        assertNotEquals(roadOne, roadTwo);
        assertNotEquals((DrawableEdge) roadOne, (DrawableEdge) roadTwo);
    }
}
