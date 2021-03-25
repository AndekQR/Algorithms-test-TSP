package app.view.controlPanel;

import app.view.controlPanel.algorithmsPanes.AcoPane;
import app.view.controlPanel.algorithmsPanes.NNAPane;
import app.view.controlPanel.algorithmsPanes.SAPane;
import app.view.controlPanel.algorithmsPanes.TabuPane;
import javafx.scene.control.Accordion;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;

public class AlgorithmsTabPane extends Accordion {
    private final String acoName = "Ant Colony Optimization Algorithm";
    private final String tabuName = "Tabu Search";
    private final String simulatingAnnealingName = "Simulating Annealing";
    private final String nearestNeighbourAlgorithmName = "Nearest Neighbour Algorithm";
    private final String graphCreationPaneName = "Graph";

    public AlgorithmsTabPane() {
        this.initTabs();
    }

    private void initTabs() {
        TitledPane graphTab = new TitledPane(this.graphCreationPaneName, new GraphCreationPane());
        TitledPane acoTab = new TitledPane(this.acoName, new AcoPane());
        TitledPane tabuTab = new TitledPane(this.tabuName, new TabuPane());
        TitledPane aSTab = new TitledPane(this.simulatingAnnealingName, new SAPane());
        TitledPane nNaTab = new TitledPane(this.nearestNeighbourAlgorithmName, new NNAPane());

        this.getPanes().addAll(graphTab ,acoTab, tabuTab, aSTab, nNaTab);
    }
}

