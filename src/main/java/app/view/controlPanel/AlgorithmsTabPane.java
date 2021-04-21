package app.view.controlPanel;

import app.view.controlPanel.panes.*;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;

public class AlgorithmsTabPane extends Accordion {

    private static final String acoName = "Ant Colony Optimization Algorithm";
    private static final String tabuName = "Tabu Search";
    private static final String simulatingAnnealingName = "Simulating Annealing";
    private static final String nearestNeighbourAlgorithmName = "Nearest Neighbour Algorithm";
    private static final String graphCreationPaneName = "Graph";

    private final Controlling controlPanel;

    public AlgorithmsTabPane(Controlling controlPanel) {
        this.controlPanel = controlPanel;
        this.initTabs();
    }

    private void initTabs() {
        TitledPane graphTab = new TitledPane(graphCreationPaneName, new GraphPane(controlPanel));
        TitledPane acoTab = new TitledPane(acoName, new AcoPane(controlPanel));
        TitledPane tabuTab = new TitledPane(tabuName, new TabuPane());
        TitledPane aSTab = new TitledPane(simulatingAnnealingName, new SAPane(controlPanel));
        TitledPane nNaTab = new TitledPane(nearestNeighbourAlgorithmName, new NNAPane());

        this.setExpandedPane(graphTab);
        this.getPanes().addAll(graphTab, acoTab, tabuTab, aSTab, nNaTab);
    }
}

