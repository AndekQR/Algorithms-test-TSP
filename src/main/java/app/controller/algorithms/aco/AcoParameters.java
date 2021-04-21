package app.controller.algorithms.aco;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class AcoParameters {

    private final double randomFactor = 0.01;
    private final double q = 500;
    // Impact of pheromones on decision making.
    private final double alpha = 1;
    // Impact of distance on decision making.
    private final double beta = 5;
    // Evaporation rate of pheromones.
    private final double evaporation = 0.1;
    // Number of ants to run per generation.
    private final int ants = 100;
    // Number of generations.
    private final int generations = 100;

}
