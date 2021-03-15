package view;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Cell extends Pane {

    private String cellId;

    private List<Cell> childrenCells=new ArrayList<>();
    private List<Cell> parentCells=new ArrayList<>();

    private Node view;

    public Cell(String cellId) {
        this.cellId=cellId;
    }

    public void addCellChild(Cell cell) {
        childrenCells.add(cell);
    }

    public void addCellParent(Cell cell) {
        parentCells.add(cell);
    }

    public void removeCellChild(Cell cell) {
        childrenCells.remove(cell);
    }

    public Node getView() {
        return this.view;
    }

    public void setView(Node view) {
        this.view=view;
        getChildren().add(view);

    }
}
