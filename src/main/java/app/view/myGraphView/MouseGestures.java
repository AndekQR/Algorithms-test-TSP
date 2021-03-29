package app.view.myGraphView;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class MouseGestures {

    final DragContext dragContext=new DragContext();
    GraphViewUtilities graph;

    EventHandler<MouseEvent> onMousePressedEventHandler=event -> {
        Node node=(Node) event.getSource();
        double scale=graph.getScale();

        dragContext.x=node.getBoundsInParent().getMinX() * scale - event.getScreenX();
        dragContext.y=node.getBoundsInParent().getMinY() * scale - event.getScreenY();
    };

    EventHandler<MouseEvent> onMouseDraggedEventHandler=event -> {
        Node node=(Node) event.getSource();

        double offsetX=event.getScreenX() + dragContext.x;
        double offsetY=event.getScreenY() + dragContext.y;

        double scale=graph.getScale();

        offsetX/=scale;
        offsetY/=scale;

        node.relocate(offsetX, offsetY);
    };

    EventHandler<MouseEvent> onMouseReleasedEventHandler=event -> {
    };

    public MouseGestures(GraphViewUtilities graph) {
        this.graph=graph;
    }

    public void makeDraggable(final Node node) {
        node.setOnMousePressed(onMousePressedEventHandler);
        node.setOnMouseDragged(onMouseDraggedEventHandler);
        node.setOnMouseReleased(onMouseReleasedEventHandler);

    }

    static class DragContext {
        double x;
        double y;
    }
}
