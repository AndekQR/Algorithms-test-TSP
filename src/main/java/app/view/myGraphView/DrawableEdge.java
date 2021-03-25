package app.view.myGraphView;

import javafx.beans.InvalidationListener;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper=false)
public abstract class DrawableEdge extends Group {

    private DrawableCell source;
    private DrawableCell target;
    private Line line;
    private Text lineText;


    protected void initLine(DrawableCell source, DrawableCell target, Double distance, Double pheromone) {
        this.source=source;
        this.target=target;

        line=new Line();
        line.setStrokeWidth(3.0);
        line.setStroke(Color.valueOf("#bcb8b1"));

        InvalidationListener listener=observable -> {
            updateStartXYLine();
            updateEndXYLine();
        };

        target.getCenterX().addListener(listener);
        target.getCenterY().addListener(listener);
        source.getCenterX().addListener(listener);
        source.getCenterY().addListener(listener);
        listener.invalidated(null);

        getChildren().add(line);

        this.lineText = getTextLine();
        getChildren().add(lineText);
        updateLineText(distance,pheromone);
    }

    private Text getTextLine() {
        Text text=new Text();
        text.setStyle("-fx-background-color: white");
        text.setStrokeWidth(0.2);
        text.setStyle("-fx-font-size: 10");
        text.setStyle("-fx-font-family: 'Trebuchet MS'");
        text.xProperty().bind(line.startXProperty().add(line.endXProperty()).divide(2.0));
        text.yProperty().bind(line.startYProperty().add(line.endYProperty()).divide(2.0));

        return text;
    }

    public Point2D getDirection(DrawableCell c1, DrawableCell c2) {
        return new Point2D(c2.getCenterX().get() - c1.getCenterX().get(), c2.getCenterY().get() - c1.getCenterY().get()).normalize();
    }

    private void updateStartXYLine() {
        Point2D dir=getDirection(source, target);
        Point2D diff=dir.multiply(DrawableCell.circleRadius);
        line.setStartX(source.getCenterX().get() + diff.getX());
        line.setStartY(source.getCenterY().get() + diff.getY());
    }

    private void updateEndXYLine() {
        Point2D dir=getDirection(target, source);
        Point2D diff=dir.multiply(DrawableCell.circleRadius);
        line.setEndX(target.getCenterX().get() + diff.getX());
        line.setEndY(target.getCenterY().get() + diff.getY());
    }

    public void updateLineText(Double distance, Double pheromone) {
        if (lineText != null) {
            this.lineText.setText("Weight: " + distance + "\n" + "Pheromone: " + pheromone);
        }
    }

    public String getSourceName() {
        return this.source.getName();
    }

    public String getTargetName() {
        return this.target.getName();
    }

}
