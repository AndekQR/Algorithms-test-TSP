package app.view.myGraphView;

import app.Main;
import app.countryCopy.CityCopy;
import app.countryCopy.RoadCopy;
import app.view.controlPanel.ControlPanel;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import lombok.Getter;

import javax.naming.ldap.Control;
import java.util.HashSet;
import java.util.Set;

public abstract class GraphView {

    private MouseGestures mouseGestures;
    @Getter
    private ZoomableScrollPane rootScrollPane;
    private CellLayer cellLayer;

    private Set<DrawableCell> addedCells;
    private Set<DrawableEdge> addedEdges;


    public void initView() {
        this.addedCells=new HashSet<>();
        this.addedEdges=new HashSet<>();
        this.mouseGestures = new MouseGestures(this);


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

    public void addEdgeToDraw(RoadCopy edge, CityCopy cityCopy, CityCopy cityCopy1) {
        //hashcode może być inne ze względu na odwrócony source i target
        if (!alreadyExists(cityCopy, cityCopy1)) {
            edge.initLineDraw(cityCopy, cityCopy1);
            this.addedEdges.add(edge);
        }
    }

    /**
     * W modelu grafu, krawędź z A do B to ta sama co z B do A
     * @param edge - krawędź do sprawdzenia
     * @return jeżeli ta sama krawędź, true. W przeciwnym wypadku false
     */
    private boolean alreadyExists(CityCopy one, CityCopy two) {
        for (DrawableEdge addedEdge : this.addedEdges) {
            String sourceName=addedEdge.getSourceName();
            String targetName=addedEdge.getTargetName();
            if (one.getName().equals(sourceName) && two.getName().equals(targetName)) return true;
            if (one.getName().equals(targetName) && two.getName().equals(sourceName)) return true;
        }
        return false;
//        for (DrawableEdge next : this.addedEdges) {
//            boolean sourceSource=next.getSourceName().equals(edge.getSourceName());
//            boolean sourceTarget=next.getSourceName().equals(edge.getTargetName());
//            boolean targetTarget=next.getTargetName().equals(edge.getTargetName());
//            boolean targetSource=next.getTargetName().equals(edge.getSourceName());
//            if (sourceSource && targetTarget) return true;
//            if (sourceTarget && targetSource) return true;
//        }
//        return false;
    }

    public double getScale() {
        return this.rootScrollPane.getScaleValue();
    }

    protected void drawAddedNodes() {
        this.cellLayer.getChildren().addAll(this.addedCells);
        this.cellLayer.getChildren().addAll(this.addedEdges);
    }

    public abstract Node getView();

    protected void makeCellsDraggable() {
        for (DrawableCell addedCell : this.addedCells) {
            this.mouseGestures.makeDraggable(addedCell);
        }
    }

}
