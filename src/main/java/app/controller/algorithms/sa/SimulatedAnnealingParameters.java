package app.controller.algorithms.sa;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SimulatedAnnealingParameters {

    private final double initialTemperature = 10000;
    private final double coolingRate = 0.003;
}
