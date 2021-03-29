package app.view.myGraphView;

import app.controller.graph.City;
import app.controller.graph.Road;
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
        this.addedCells=Collections.synchronizedSet(new HashSet<>());
        this.addedEdges=Collections.synchronizedSet(new HashSet<>());

        this.mouseGestures=new MouseGestures(this);


        Group canvas=new Group();
        cellLayer=new CellLayer();
        canvas.getChildren().add(cellLayer);

        rootScrollPane=new ZoomableScrollPane(cellLayer);
        rootScrollPane.setFitToWidth(true);
        rootScrollPane.setFitToHeight(true);

    }

    public void addCellToDraw(DrawableCell cell) {
        this.addedCells.add(cell);
    }

    public void addEdgeToDraw(Road edge, City city, City city1) {
        //hashcode może być inne ze względu na odwrócony source i target
//        if (!alreadyExists(city, city1)) {
////            System.out.println(edge);
//            edge.initLineDraw(city, city1, edge.getDistance(), edge.getPheromone());
//            this.addedEdges.add(edge);
////            System.out.println("Dodane: "+city.getName()+" to "+city1.getName());
//
//        }
//        if (!this.addedEdges.contains(edge)) {
//            edge.initLineDraw(city, city1, edge.getDistance(), edge.getPheromone());
            this.addedEdges.add(edge);

//        }
    }

    /**
     * W modelu grafu, krawędź z A do B to ta sama co z B do A
     *
     * @param edge - krawędź do sprawdzenia
     * @return jeżeli ta sama krawędź, true. W przeciwnym wypadku false
     */
    private boolean alreadyExists(City one, City two) {
        return this.addedEdges.stream().anyMatch(addedEdge -> {
            String sourceName=addedEdge.getSourceName();
            String targetName=addedEdge.getTargetName();
            if (one.getName().equals(sourceName) && two.getName().equals(targetName))
                return true;
            if (one.getName().equals(targetName) && two.getName().equals(sourceName))
                return true;
            return false;
        });
    }

    public double getScale() {
        return this.rootScrollPane.getScaleValue();
    }

    protected void drawAddedNodes() {
        log.info("Number of edges in view: " + this.addedEdges.size());
        log.info("Number of vertices in view: " + this.addedCells.size());
        if (((addedCells.size() * (addedCells.size() - 1)) / 2) != addedEdges.size()) {
            log.error("Wrong number of edges added to view!");
        }

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
