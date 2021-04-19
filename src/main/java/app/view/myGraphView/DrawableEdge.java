package app.view.myGraphView;

import javafx.beans.InvalidationListener;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(callSuper=false)
public abstract class DrawableEdge extends Group {

    private DrawableCell source;
    private DrawableCell target;
    private Line line;
    private Text lineText;
    private boolean initialized;
    private boolean highlighted = false;


    public void initLine(DrawableCell source, DrawableCell target) {
        this.source=source;
        this.target=target;
        this.setCache(true);

        line=new Line();
        line.setStrokeWidth(3.0);

        setListener();

        initialized = true;
        getChildren().add(line);
    }


    public void drawText(String text) {
        this.lineText = getTextLine();
        getChildren().add(lineText);
        lineText.setText(text);
    }

    private void setListener() {
        InvalidationListener listener=observable -> {
            updateStartXYLine();
            updateEndXYLine();
        };

        target.getCenterX().addListener(listener);
        target.getCenterY().addListener(listener);
        source.getCenterX().addListener(listener);
        source.getCenterY().addListener(listener);
        listener.invalidated(null);
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

    public String getSourceName() {
        return this.source.getName();
    }

    public String getTargetName() {
        return this.target.getName();
    }


    public void drawHighlightedLine() {
        highlighted = true;
        this.line.setStroke(Color.rgb(230, 57, 70, 0.5));
    }

    public void drawNormalLine() {
        line.setStroke(Color.rgb(189, 185, 178, 0.2));
    }

    public void removeNormalLine() {
        if (!highlighted) {
            this.getChildren().remove(line);
            this.getChildren().remove(lineText);
            initialized = false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DrawableEdge)) return false;

        DrawableEdge that=(DrawableEdge) o;

        if (source == null && (that.source != null)) return false;
        if (target == null && (that.target != null)) return false;
        if (that.source == null && (source != null)) return false;
        if (that.target == null && (target != null)) return false;

        if (source != null && target != null) {
            if (
                    (!source.equals(that.source) && !target.equals(that.target)) ||
                            (!source.equals(that.target) && !target.equals(that.source)))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result=source != null ? source.hashCode() * 31 : 0;
        result=result + (target != null ? target.hashCode() * 31 : 0);
        return result;
    }

}
