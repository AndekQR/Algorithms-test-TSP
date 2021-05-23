package app.controller.algorithms.ts;

import app.controller.algorithms.Algorithm;
import app.controller.algorithms.AlgorithmsUtils;
import app.controller.graph.City;
import app.controller.graph.Country;
import app.controller.utils.AlgorithmResult;
import app.controller.utils.algorithmListener.AlgorithmEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TabuSearchAlgorithm implements Algorithm {

    private final Country country;
    private final TabuSearchParameters params;
    private List<City> bestSolution = new ArrayList<>();
    private double bestSolutionDistance;
    private List<City> currentSolution;
    private double currentSolutionDistance;
    private final TabuList tabuList;
    private final ExecutorService executorService;


    public TabuSearchAlgorithm(Country country, TabuSearchParameters params) {
        this.tabuList = new TabuList();
        this.country = country;
        this.params = params;
        this.executorService = Executors.newSingleThreadExecutor();
    }


    @Override
    public AlgorithmResult solve(AlgorithmEventListener algorithmEventListener) {
        executorService.submit(() -> algorithmEventListener.algorithmStart(params));
        long iteration = 0;
        initPaths();
        while (iteration < params.getIterations()) {

            List<Swap> possibleSwaps = generatePossibleSwaps(currentSolution);
            Swap bestSwap = chooseBestSwap(possibleSwaps);
            if (bestSwap != null) {
                tabuList.addTabuSwap(bestSwap);
                List<City> newSolution = bestSwap.solutionAfterSwapOn(currentSolution);
                setNewCurrentSolution(newSolution);
            }

            if (currentSolutionDistance < bestSolutionDistance) {
                setNewBestSolution(currentSolution);
            }

            long finalIteration = iteration;
            executorService.submit(() -> algorithmEventListener.iterationComplete(bestSolution, finalIteration));
            iteration++;
        }
        executorService.submit(() -> algorithmEventListener.algorithmStop(bestSolution));
        executorService.shutdown();
        return new AlgorithmResult(AlgorithmsUtils.solutionByRoads(bestSolution), bestSolution);
    }

    private void setNewBestSolution(List<City> solution) {
        this.bestSolution = new ArrayList<>(solution);
        this.bestSolutionDistance = AlgorithmsUtils.calculateSolutionDistance(solution);
    }

    private void setNewCurrentSolution(List<City> solution) {
        this.currentSolution = new ArrayList<>(solution);
        this.currentSolutionDistance = AlgorithmsUtils.calculateSolutionDistance(solution);
    }

    private void initPaths() {
        List<City> randomPath = AlgorithmsUtils.generateRandomPath(country);
        setNewCurrentSolution(randomPath);
        setNewBestSolution(randomPath);
    }

    private List<Swap> generatePossibleSwaps(List<City> cities) {
        List<Swap> possibleSwaps = new ArrayList<>();
        for (City source : cities) {
            for (City target : cities) {
                if (source != target) {
                    Swap swap = new Swap(source, target);
                    possibleSwaps.add(swap);
                }
            }
        }
        return possibleSwaps;
    }

    private Swap chooseBestSwap(List<Swap> swaps) {
        Swap bestSwap = null;
        double bestSwapDistance = 999999.9999;
        for (Swap swap : swaps) {
            double distanceAfterSwap = swap.distanceAfterSwapOn(currentSolution);
            if (distanceAfterSwap < bestSwapDistance && !tabuList.isTabu(swap)) {
                bestSwap = swap;
                bestSwapDistance = distanceAfterSwap;
            }
        }
        if (bestSwap != null) tabuList.addTabuSwap(bestSwap);
        return bestSwap;
    }
}
