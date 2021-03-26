package app.view.myGraphView.layouts;

import app.controller.graph.Country;
import app.view.myGraphView.DrawableCell;

import java.util.List;

public class EqualSpacesLayout extends Layout {

    private Country graph;

    private double step=300;

    public EqualSpacesLayout(Country graph) {
        this.graph=graph;
    }

    @Override
    public void execute() {
        List<? extends DrawableCell> cells=graph.getCities();
        double currentX=1;
        double currentY=1;

        double maxInRow=Math.ceil(Math.sqrt(cells.size()));

        for (DrawableCell cell : cells) {
            if (currentX > maxInRow) {
                currentY++;
                currentX=1;
            }

            cell.relocate(currentX * step, currentY * step);

            currentX++;

        }
    }
}
