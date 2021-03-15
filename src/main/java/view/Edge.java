package view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import lombok.Getter;

@Getter
public class Edge extends Group {

    private Cell source;
    private Cell target;
    private Double weight;
    private Double phermone;
    private Line line;

    public Edge(Cell source, Cell target, Double weight, Double phermone) {

        this.source=source;
        this.target=target;
        this.weight=weight;
        this.phermone=phermone;

        source.addCellChild(target);
        target.addCellParent(source);

        line=new Line();

        line.setStrokeWidth(3.0);
        line.setStroke(Color.rgb(46, 46, 46, 0.5));

        line.startXProperty().bind(source.layoutXProperty().add(source.getBoundsInParent().getWidth() / 2.0));
        line.startYProperty().bind(source.layoutYProperty().add(source.getBoundsInParent().getHeight() / 2.0));

        line.endXProperty().bind(target.layoutXProperty().add(target.getBoundsInParent().getWidth() / 2.0));
        line.endYProperty().bind(target.layoutYProperty().add(target.getBoundsInParent().getHeight() / 2.0));

        line.setOnMouseClicked(mouseEvent -> {
            line.setStroke(Color.YELLOW);
        });

        getChildren().add(line);

        Text text = new Text("Weight: "+weight+"\n"+"Pheromone: "+phermone);
        text.setStroke(Color.BLACK);
        text.setStrokeWidth(0.5);
        text.xProperty().bind(line.startXProperty().add(line.endXProperty()).divide(2.0));
        text.yProperty().bind(line.startYProperty().add(line.endYProperty()).divide(2.0));


        getChildren().add(text);
    }
}
