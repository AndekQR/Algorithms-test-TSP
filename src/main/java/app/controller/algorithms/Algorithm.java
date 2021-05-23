package app.controller.algorithms;

import app.controller.utils.AlgorithmResult;
import app.controller.utils.algorithmListener.AlgorithmEventListener;

public interface Algorithm {
    AlgorithmResult solve(AlgorithmEventListener algorithmEventListener);
}
