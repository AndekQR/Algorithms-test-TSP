package app.view.myGraphView;

import app.Main;
import app.controller.graph.City;
import app.controller.graph.Country;
import app.view.controlPanel.ControlPanel;
import app.view.myGraphView.layouts.EqualSpacesLayout;
import app.view.myGraphView.layouts.Layout;
import javafx.scene.layout.BorderPane;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GraphView extends BorderPane {

    @Getter
    private Country displayedGraph;

    public GraphView() {
        this.setMaxWidth(Main.WIDTH - ControlPanel.WIDTH);
        this.setMinWidth(Main.WIDTH - ControlPanel.WIDTH);
    }

    public void setGraphToShow(Country country) {
        this.displayedGraph=country;

        if (this.displayedGraph != null) {
            this.setCenter(this.displayedGraph.getView());
            Layout layout=new EqualSpacesLayout(displayedGraph);
            layout.execute();
        }
    }
}
