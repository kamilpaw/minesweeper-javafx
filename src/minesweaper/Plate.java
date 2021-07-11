package minesweaper;
import javafx.scene.control.Button;
import java.util.ArrayList;


public class Plate extends Button {
    private final ArrayList<Plate> neighbors;
    private boolean hasBomb;
    private int numOfBombs;
    private boolean isFlagged;

    public Plate() {
        this.neighbors = new ArrayList<>();
    }

    public void addNeighbors(Plate p) {
        this.neighbors.add(p);
    }

    public int getNumOfBombs() {
        return numOfBombs;
    }

    public void setNumOfBombs(int numOfBombs) {
        this.numOfBombs = numOfBombs;
    }

    public ArrayList<Plate> getNeighbors() {
        return neighbors;
    }

    public boolean HasBomb() {
        return hasBomb;
    }

    public boolean isFlagged() {
        return  isFlagged;
    }

    public void setFlagged(){
        this.isFlagged = true;
    }

    public void setUnflagged(){
        this.isFlagged = false;
    }

    public void setHasBomb(boolean hasBomb) {
        this.hasBomb = hasBomb;
    }
}
