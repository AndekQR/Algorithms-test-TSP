package view;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class NamedCircleCell extends Cell {

    private final double circleWidth = 40;

    public NamedCircleCell(String cellId, String name) {
        super(cellId);

        Text text = new Text(name);
        text.setWrappingWidth(circleWidth);
        Circle circle = new Circle(circleWidth);
        circle.setStroke(Color.YELLOW);
        circle.setFill(Color.WHITESMOKE);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(circle, text);
        setView(stackPane);

    }


}
