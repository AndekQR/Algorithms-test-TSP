import cityUtils.City;
import cityUtils.Country;
import cityUtils.Road;
import countryCopy.CityCopy;
import countryCopy.CountryCopy;
import view.CellType;
import view.Graph;
import view.Model;

public class GraphViewAdapter {

    public static Graph toViewGraph(CountryCopy country) {
        Graph graph = new Graph();
        Model model=graph.getModel();

        graph.beginUpdate();

        for (CityCopy city : country.getCountry()) {
            model.addCell(city.getName(), CellType.NAMED_CIRCLE);
        }
        for (CityCopy city : country.getCountry()) {
            city.getDirections().forEach((cityCopy, roadCopy) -> {
                model.addEdge(city.getName(), cityCopy.getName(), roadCopy.getDistance(), roadCopy.getPheromone());
            });
        }

        graph.endUpdate();
        return graph;
    }
}
