package app.view.controlPanel;

import app.controller.graph.Country;

import java.util.Optional;

public interface Controlling {

    void setGraphForProcessing(Country country);
    Optional<Country> getGraphForProcessing();
}
