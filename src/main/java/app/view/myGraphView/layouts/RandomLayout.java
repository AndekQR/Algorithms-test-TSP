package app.view.myGraphView.layouts;

import app.Main;
import app.countryCopy.CountryCopy;
import app.view.controlPanel.ControlPanel;
import app.view.myGraphView.DrawableCell;
import app.view.myGraphView.layouts.Layout;


import java.util.List;
import java.util.Random;


public class RandomLayout extends Layout {

    CountryCopy graph;
    Random rnd=new Random();

    public RandomLayout(CountryCopy graph) {

        this.graph=graph;

    }

    public void execute() {

        List<? extends DrawableCell> cells=graph.getCities();


        for (DrawableCell cell : cells) {
            this.relocate(cell);
        }

    }

    private void relocate(DrawableCell cell) {
        double x=rnd.nextDouble() * (Main.WIDTH - ControlPanel.WIDTH);
        double y=rnd.nextDouble() * (Main.WIDTH - ControlPanel.WIDTH);

        cell.relocate(x, y);
    }

}
