package app.view.myGraphView;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
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

    protected void initView(){
        StackPane stackPane=new StackPane();
        stackPane.getChildren().addAll(getCircleNode(), getTextNode());
        setView(stackPane);

        this.centerX = getCenterXBinding();
        this.centerY = getCenterYBinding();
    }

    private Text getTextNode() {
        Text text=new Text(name);
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
