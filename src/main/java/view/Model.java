package view;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Model {

    private Cell graphParent;

    private List<Cell> allCells;
    private List<Cell> addedCells;
    private List<Cell> removedCells;

    private List<Edge> allEdges;
    private List<Edge> addedEdges;
    private List<Edge> removedEdges;

    private Map<String, Cell> cellMap; // <id,cell>

    public Model() {

        graphParent=new Cell("_ROOT_");

        // clear model, create lists
        clear();
    }

    public void clear() {

        allCells=new ArrayList<>();
        addedCells=new ArrayList<>();
        removedCells=new ArrayList<>();

        allEdges=new ArrayList<>();
        addedEdges=new ArrayList<>();
        removedEdges=new ArrayList<>();

        cellMap=new HashMap<>(); // <id,cell>

    }

    public void clearAddedLists() {
        addedCells.clear();
        addedEdges.clear();
    }

    public void addCell(String id, CellType type) {

        switch (type) {

            case RECTANGLE:
                RectangleCell rectangleCell=new RectangleCell(id);
                addCell(rectangleCell);
                break;

            case TRIANGLE:
                TriangleCell circleCell=new TriangleCell(id);
                addCell(circleCell);
                break;

            case NAMED_CIRCLE:
                NamedCircleCell namedCircleCell=new NamedCircleCell(id, id);
                addCell(namedCircleCell);
                break;

            default:
                throw new UnsupportedOperationException("Unsupported type: " + type);
        }
    }

    private void addCell(Cell cell) {
        addedCells.add(cell);
        cellMap.put(cell.getCellId(), cell);
    }

    public void addEdge(String sourceId, String targetId, Double weight, Double pheromone) {
        Cell sourceCell=cellMap.get(sourceId);
        Cell targetCell=cellMap.get(targetId);

        Edge edge=new Edge(sourceCell, targetCell, weight, pheromone);

        addedEdges.add(edge);
    }


    /**
     * Attach all cells which don't have a parent to graphParent
     *
     * @param cellList
     */
    public void attachOrphansToGraphParent(List<Cell> cellList) {
        for (Cell cell : cellList) {
            if (cell.getParentCells().size() == 0) {
                graphParent.addCellChild(cell);
            }
        }

    }

    /**
     * Remove the graphParent reference if it is set
     *
     * @param cellList
     */
    public void disconnectFromGraphParent(List<Cell> cellList) {
        for (Cell cell : cellList) {
            graphParent.removeCellChild(cell);
        }
    }

    public void merge() {
        // cells
        allCells.addAll(addedCells);
        allCells.removeAll(removedCells);

        addedCells.clear();
        removedCells.clear();

        // edges
        allEdges.addAll(addedEdges);
        allEdges.removeAll(removedEdges);

        addedEdges.clear();
        removedEdges.clear();
    }
}
