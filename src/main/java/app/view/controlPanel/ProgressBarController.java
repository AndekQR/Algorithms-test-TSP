package app.view.controlPanel;

import javafx.beans.property.BooleanProperty;

public interface ProgressBarController {

    void showProgressBar();

    void hideProgressBar();

    BooleanProperty getProgressBarProperty();

}
