package app.controller.utils.algorithmListener;

import app.controller.algorithms.AlgorithmsUtils;
import app.controller.algorithms.aco.AcoParameters;
import app.controller.algorithms.sa.SimulatedAnnealingParameters;
import app.controller.algorithms.ts.TabuSearchParameters;
import app.controller.graph.City;
import app.controller.utils.ExcelWriter;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Czas wykonywania algorytmu
 * Długość ścieżki w każdej iteracji
 * Dlugość drogi po zakończonym algorytmie
 */
public class DataLogger implements AlgorithmEventListener {

    private ExcelWriter excelWriter;
    private Instant startTIme;
    private final Instant instancedTime = Instant.now();

    public DataLogger() {
        this.excelWriter = new ExcelWriter();
    }

    @Override
    public void algorithmStart(AcoParameters parameters) {
        startTIme = Instant.now();
        excelWriter.algorithmName("ACO algorithm");

        Map<String, Double> paramMap = new HashMap<>();
        paramMap.put("Random factor", parameters.getRandomFactor());
        paramMap.put("Q", parameters.getQ());
        paramMap.put("Alpha", parameters.getAlpha());
        paramMap.put("Beta", parameters.getBeta());
        paramMap.put("Evoporation", parameters.getEvaporation());
        paramMap.put("Ants", (double) parameters.getAnts());
        paramMap.put("Generations", (double) parameters.getGenerations());
        excelWriter.writeParameters(paramMap);
    }

    @Override
    public void algorithmStart(SimulatedAnnealingParameters parameters) {
        startTIme = Instant.now();
        excelWriter.algorithmName("Simulated Annealing Algorithm");

        Map<String, Double> paramMap = new HashMap<>();
        paramMap.put("Initial temperature", parameters.getInitialTemperature());
        paramMap.put("Cooling rate", parameters.getCoolingRate());
        excelWriter.writeParameters(paramMap);
    }

    @Override
    public void algorithmStart(TabuSearchParameters parameters) {
        startTIme = Instant.now();
        excelWriter.algorithmName("Tabu Search Algorithm");

        Map<String, Double> paramMap = new HashMap<>();
        paramMap.put("Initial temperature", (double) parameters.getIterations());
        excelWriter.writeParameters(paramMap);
    }

    @Override
    public void iterationComplete(List<City> actualSolution, long iterationNumber) {
//        excelWriter.writeIteration(iterationNumber, AlgorithmsUtils.calculateSolutionDistance(actualSolution));
    }

    @Override
    public void algorithmStart() {
        startTIme = Instant.now();

        excelWriter.algorithmName("Nearest Neighbour Algorithm");
    }

    @Override
    public void algorithmStop(List<City> resultRoad) {
        Instant stopTime = Instant.now();
        double distance = AlgorithmsUtils.calculateSolutionDistance(resultRoad);
        if (startTIme != null) {
            long duration = Duration.between(startTIme, stopTime).toMillis();
            System.out.println(duration);
            excelWriter.writeResult(distance, resultRoad.size(), duration);

        } else {
            excelWriter.writeResult(distance, resultRoad.size(), 0);

        }
    }

    @Override
    public void saveResultToExcelFile() {
        if (this.excelWriter != null) {
            excelWriter.saveFile(instancedTime.toEpochMilli()+"");
        }
    }
}
