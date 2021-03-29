package app.controller.graph;

import app.controller.helpers.Helpers;
import app.view.myGraphView.DrawableCell;
import app.view.myGraphView.DrawableEdge;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class Road extends DrawableEdge {

    private final String name;
    private Double distance;
    private Double pheromone;

    public Road(double weight, double pheromone, String edgeName) {
        this.distance = weight;
        this.pheromone = pheromone;
        this.name = edgeName;
    }

    public Double addPheromone(Double pheromones) {
        double newValue=pheromones + this.pheromone;
        double rounded=Helpers.round(newValue, 2);
//        setPheromone(rounded);
        this.pheromone = rounded;
        return rounded;
    }

    public Double evaporatePheromones(Double evaporationRate) {
        double newValue=this.pheromone * evaporationRate;
        double rounded=Helpers.round(newValue, 2);
//        this.setPheromone(rounded);
        this.pheromone = rounded;
        return rounded;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Road)) return false;
        if (!super.equals(o)) return false;

        Road road=(Road) o;

        if (!distance.equals(road.distance)) return false;
        if (!name.equals(road.name)) return false;
        return pheromone.equals(road.pheromone);
    }

    @Override
    public int hashCode() {
        int result=super.hashCode();
//        int result=0;
        result=31 * result + distance.hashCode();
        result=31 * result + pheromone.hashCode();
        result=31*result+name.hashCode();
        return result;
    }
}
