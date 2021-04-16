package app.view.controlPanel;

import app.view.controlPanel.panes.*;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;

public class AlgorithmsTabPane extends Accordion {
    private final String acoName = "Ant Colony Optimization Algorithm";
    private final String tabuName = "Tabu Search";
    private final String simulatingAnnealingName = "Simulating Annealing";
    private final String nearestNeighbourAlgorithmName = "Nearest Neighbour Algorithm";
    private final String graphCreationPaneName = "Graph";

    private Controlling controlPanel;

    public AlgorithmsTabPane(Controlling controlPanel) {
        this.controlPanel=controlPanel;
        this.initTabs();
    }

    private void initTabs() {
        TitledPane graphTab = new TitledPane(this.graphCreationPaneName, new GraphPane(controlPanel));
        TitledPane acoTab = new TitledPane(this.acoName, new AcoPane(controlPanel));
        TitledPane tabuTab = new TitledPane(this.tabuName, new TabuPane());
        TitledPane aSTab = new TitledPane(this.simulatingAnnealingName, new SAPane());
        TitledPane nNaTab = new TitledPane(this.nearestNeighbourAlgorithmName, new NNAPane());

        this.setExpandedPane(graphTab);
        this.getPanes().addAll(graphTab ,acoTab, tabuTab, aSTab, nNaTab);
    }
}

