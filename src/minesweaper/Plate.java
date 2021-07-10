package minesweaper;

import javafx.scene.control.Button;

import java.util.ArrayList;

public class Plate extends Button {
    private ArrayList<Plate> neighbors;
    private boolean hasBomb;
    private int numOfBombs;

    public Plate() {
        this.neighbors = new ArrayList<>();
        this.hasBomb = hasBomb;
        this.numOfBombs = numOfBombs;

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

    public boolean isHasBomb() {
        return hasBomb;
    }

    public void setHasBomb(boolean hasBomb) {
        this.hasBomb = hasBomb;
    }


}