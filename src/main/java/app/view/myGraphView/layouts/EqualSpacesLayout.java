package app.view.myGraphView.layouts;

import app.controller.graph.Country;
import app.view.myGraphView.DrawableCell;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@Slf4j
public class EqualSpacesLayout extends Layout {

    private Country graph;

    private double step=300;

    public EqualSpacesLayout(Country graph) {
        this.graph=graph;
    }

    @Override
    public void execute() {
        Collection<? extends DrawableCell> cells=graph.getCities();
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
