package com.github.malpenhorn.gameoflife;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Grid {
    private int visibleColumns;
    private int visibleRows;
    private Set<Cell> liveCells;
    private int generation = 0;

    public Grid(int visibleColumns, int visibleRows) {
        this.visibleColumns = visibleColumns;
        this.visibleRows = visibleRows;
        this.liveCells = new HashSet<>();
    }

    /**
     * Creates a random pattern of live cells, within the visible grid, as the starting sequence for the game
     */
    public void initialize(double livePercentage) {
        for (int x = 0; x < visibleColumns; x++) {
            for (int y = 0; y < visibleRows; y++) {
                if (Math.random() < livePercentage) {
                    Cell cell = new Cell(x, y);
                    liveCells.add(cell);
                }
            }
        }
    }

    /**
     * Sets the starting sequence for the game from a pre determined set of live cells
     */
    public void initialize(Set<Cell> liveCells) {
        this.liveCells = liveCells;
    }

    /**
     * Evolves the grid by one generation.
     * The rules for each live cell can be applied independently of each other.
     * Thereby allowing us to parallelize the computation.
     */
    public void evolve() {
        liveCells = liveCells.parallelStream()
                .map(this::getNextGenerationOfLiveCells)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        generation++;
    }

    private Set<Cell> getNextGenerationOfLiveCells(Cell cell) {
        Set<Cell> nextGeneration = new HashSet<>();
        Set<Cell> deadNeighbors = getDeadNeighbors(cell);

        // 1. Any live cell with two or three live neighbours survives.
        int numLiveNeighbors = 8 - deadNeighbors.size();
        if (numLiveNeighbors > 1 && numLiveNeighbors < 4) {
            nextGeneration.add(cell);
        }

        // 2. Any dead cell with three live neighbours becomes a live cell.
        for (Cell neighbor : deadNeighbors) {
            numLiveNeighbors = 8 - getDeadNeighbors(neighbor).size();
            if (numLiveNeighbors == 3) {
                nextGeneration.add(neighbor);
            }
        }

        // 3. All other live cells die in the next generation. Similarly, all other dead cells stay dead.
        return nextGeneration;
    }

    private Set<Cell> getDeadNeighbors(Cell cell) {
        Set<Cell> deadNeighbors = new HashSet<>();
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                Cell neighbor = new Cell(cell.getX() + x, cell.getY() + y);
                if (!liveCells.contains(neighbor) && !neighbor.equals(cell)) {
                    deadNeighbors.add(neighbor);
                }
            }
        }
        return deadNeighbors;
    }

    public int getVisibleColumns() {
        return visibleColumns;
    }

    public void setVisibleColumns(int visibleColumns) {
        this.visibleColumns = visibleColumns;
    }

    public int getVisibleRows() {
        return visibleRows;
    }

    public void setVisibleRows(int visibleRows) {
        this.visibleRows = visibleRows;
    }

    public Set<Cell> getLiveCells() {
        return liveCells;
    }

    public int getGeneration() {
        return generation;
    }
}
