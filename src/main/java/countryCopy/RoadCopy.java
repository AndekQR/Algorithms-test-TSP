package countryCopy;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoadCopy {
    private Double distance;
    private Double pheromone;

    public Double addPheromone(Double pheromones) {
        setPheromone(pheromones + this.pheromone);
        return this.pheromone;
    }

    public Double evaporatePheromones(Double evaporationRate) {
        this.setPheromone(this.pheromone * evaporationRate);
        return this.pheromone;
    }
}
