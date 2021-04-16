package app.view.myGraphView;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper=false)
public abstract class DrawableCell extends Pane {

    public static final double circleRadius = 40;

    @Getter
    protected String name;
    private Node view;

    @Getter
    private DoubleBinding centerX;
    @Getter
    private DoubleBinding centerY;
    private Circle circle;

    protected void initView(){
        StackPane stackPane=new StackPane();
        this.circle = getCircleNode();
        stackPane.getChildren().addAll(circle, getTextNode());
        stackPane.setAlignment(Pos.CENTER);
        setView(stackPane);

        this.centerX = getCenterXBinding();
        this.centerY = getCenterYBinding();
    }

    public void highlight() {
        circle.setStroke(Color.rgb(230, 57, 70, 0.5));
        circle.setStrokeWidth(3);
    }

    private Text getTextNode() {
        Text text=new Text(name);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setWrappingWidth(circleRadius);
        text.setStyle("-fx-font-size: 10");
        text.setStyle("-fx-font-family: 'Trebuchet MS'");
        return text;
    }

    private Circle getCircleNode() {
        Circle circle=new Circle(circleRadius);
        circle.setStroke(Color.valueOf("#463f3a"));
        circle.setStrokeWidth(1);
        circle.setFill(Color.valueOf("#bcb8b1"));
        return circle;
    }

    public Node getView() {
        return this.view;
    }

    public void setView(Node view) {
        this.view=view;
        getChildren().add(view);

    }

    private DoubleBinding getCenterXBinding() {
        return this.layoutXProperty().add(this.getBoundsInParent().getWidth() / 2.0);
    }

    private DoubleBinding getCenterYBinding() {
        return this.layoutYProperty().add(this.getBoundsInParent().getWidth() / 2.0);
    }
}
