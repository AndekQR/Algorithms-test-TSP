package app.view.myGraphView.layouts;

import app.Main;
import app.controller.graph.Country;
import app.view.controlPanel.ControlPanel;
import app.view.myGraphView.DrawableCell;

import java.util.Collection;
import java.util.Random;


public class RandomLayout extends Layout {

    Country graph;
    Random rnd = new Random();

    public RandomLayout(Country graph) {

        this.graph = graph;

    }

    public void execute() {

        Collection<? extends DrawableCell> cells = graph.getCities();


        for (DrawableCell cell : cells) {
            this.relocate(cell);
        }


    }

    private void relocate(DrawableCell cell) {
        double x = rnd.nextDouble() * (Main.WIDTH - ControlPanel.WIDTH);
        double y = rnd.nextDouble() * (Main.WIDTH - ControlPanel.WIDTH);

        cell.relocate(x, y);
    }

}
