package aco;

public class AcoParameters {
    // Impact of pheromones on decision making.
    static double alpha=1;
    // Impact of distance on decision making.
    static double beta=5;
    // Evaporation rate of pheromones.
    static double evaporation=0.1;
    // Number of ants to run per generation.
    static int ants=100;
    // Number of generations.
    static int generations=100;

    static double randomFactor = 0.01;
    static double q = 500;
}
