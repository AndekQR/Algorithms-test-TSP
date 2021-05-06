package app.controller.algorithms.sa;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SimulatedAnnealingParameters {

    private double initialTemperature = 10000;
    private double coolingRate = 0.003;
}
