package app.view.myGraphView;

import app.controller.graph.City;
import app.controller.graph.Road;
import app.controller.helpers.LineType;
import javafx.scene.Group;
import javafx.scene.Node;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public abstract class GraphViewUtilities {

    private MouseGestures mouseGestures;
    @Getter(AccessLevel.PROTECTED)
    private ZoomableScrollPane rootScrollPane;
    private CellLayer cellLayer;

    private Set<DrawableCell> addedCells;
    private Set<DrawableEdge> addedEdges;

    public void initView() {
        this.addedCells = Collections.synchronizedSet(new HashSet<>());
        this.addedEdges = Collections.synchronizedSet(new HashSet<>());

        this.mouseGestures = new MouseGestures(this);


        Group canvas = new Group();
        cellLayer = new CellLayer();
        canvas.getChildren().add(cellLayer);

        rootScrollPane = new ZoomableScrollPane(cellLayer);
        rootScrollPane.setFitToWidth(true);
        rootScrollPane.setFitToHeight(true);

    }

    public void addCellToDraw(DrawableCell cell) {
        this.addedCells.add(cell);
    }

    public void addEdgeToDraw(Road edge, DrawableCell city, DrawableCell city1, String text, LineType type) {
        if (!edge.isInitialized()) {
            edge.initLine(city, city1);
            switch (type) {
                case NORMAL:
                    if (edge.isHighlighted()) return;
                    edge.drawNormalLine();
                    break;
                case HIGHLIGHTED:
                    edge.drawHighlightedLine();
                    break;
            }
            if (text != null && !text.isBlank()) edge.drawText(text);
        }

        if (addedEdges.contains(edge)) return;

        this.addedEdges.add(edge);
        this.cellLayer.getChildren().add(edge);

    }

    /**
     * W modelu grafu, krawędź z A do B to ta sama co z B do A
     *
     * @param edge - krawędź do sprawdzenia
     * @return jeżeli ta sama krawędź, true. W przeciwnym wypadku false
     */
    private boolean alreadyExists(City one, City two) {
        return this.addedEdges.stream().anyMatch(addedEdge -> {
            String sourceName = addedEdge.getSourceName();
            String targetName = addedEdge.getTargetName();
            if (one.getName().equals(sourceName) && two.getName().equals(targetName))
                return true;
            return one.getName().equals(targetName) && two.getName().equals(sourceName);
        });
    }

    public double getScale() {
        return this.rootScrollPane.getScaleValue();
    }

    protected void drawAddedNodes() {
//        log.info("Number of edges in view: " + this.addedEdges.size());
//        log.info("Number of vertices in view: " + this.addedCells.size());
//        if (((addedCells.size() * (addedCells.size() - 1)) / 2) != addedEdges.size()) {
//            log.error("Wrong number of edges added to view!");
//        }

        this.cellLayer.getChildren().addAll(this.addedCells);
//        this.cellLayer.getChildren().addAll(this.addedEdges);

    }

    public abstract Node getView();

    protected void makeCellsDraggable() {
        for (DrawableCell addedCell : this.addedCells) {
            this.mouseGestures.makeDraggable(addedCell);
        }
    }

}
