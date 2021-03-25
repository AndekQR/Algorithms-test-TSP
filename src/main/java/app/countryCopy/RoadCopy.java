package app.countryCopy;

import app.helpers.Helpers;
import app.view.myGraphView.DrawableCell;
import app.view.myGraphView.DrawableEdge;
import lombok.*;
import lombok.experimental.Helper;

@EqualsAndHashCode(callSuper=false)
@Getter
@Setter
@Builder
public class RoadCopy extends DrawableEdge {
    private Double distance;
    private Double pheromone;

    public Double addPheromone(Double pheromones) {
        double newValue=pheromones + this.pheromone;
        double rounded=Helpers.round(newValue, 2);
        setPheromone(rounded);
        return rounded;
    }

    public Double evaporatePheromones(Double evaporationRate) {
        double newValue=this.pheromone * evaporationRate;
        double rounded=Helpers.round(newValue, 2);
        this.setPheromone(rounded);
        return rounded;
    }

    public void initLineDraw(DrawableCell source, DrawableCell target) {
        super.initLine(source, target, this.distance, this.pheromone);
    }
}
