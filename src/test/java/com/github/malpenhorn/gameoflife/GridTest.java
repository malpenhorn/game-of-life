package com.github.malpenhorn.gameoflife;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GridTest {
    private final static int GRID_COLUMNS = 3;
    private final static int GRID_ROWS = 3;

    @Test
    public void testUninitializedGrid() {
        Grid grid = new Grid(GRID_COLUMNS, GRID_ROWS);

        assertEquals(GRID_COLUMNS, grid.getVisibleColumns());
        assertEquals(GRID_ROWS, grid.getVisibleRows());
        assertTrue(grid.getLiveCells().isEmpty());
    }

    @Test
    public void testRandomNoneLiveInitializedGrid() {
        Grid grid = new Grid(GRID_COLUMNS, GRID_ROWS);
        grid.initialize(0.0);

        assertTrue(grid.getLiveCells().isEmpty());
    }

    @Test
    public void testRandomAllLiveInitializeGrid() {
        Grid grid = new Grid(GRID_COLUMNS, GRID_ROWS);
        grid.initialize(1.0);

        assertEquals(9, grid.getLiveCells().size());
    }

    @Test
    public void testPreDeterminedInitializedGrid() {
        Set<Cell> seed = new HashSet<>();
        seed.add(new Cell(0,0));
        seed.add(new Cell(0, 2));
        seed.add(new Cell(2, 0));
        seed.add(new Cell(2, 2));

        Grid grid = new Grid(GRID_COLUMNS, GRID_ROWS);
        grid.initialize(seed);

        assertEquals(4, grid.getLiveCells().size());
    }

    @Test
    public void testSetVisibleGridColumnsAndRows() {
        Grid grid = new Grid(GRID_COLUMNS, GRID_ROWS);
        grid.setVisibleColumns(5);
        grid.setVisibleRows(5);
        assertEquals(5, grid.getVisibleColumns());
        assertEquals(5, grid.getVisibleRows());
    }

    @Test
    public void testGenerationCounter() {
        Grid grid = new Grid(GRID_COLUMNS, GRID_ROWS);
        assertEquals(0, grid.getGeneration());
        grid.evolve();
        assertEquals(1, grid.getGeneration());
        grid.evolve();
        assertEquals(2, grid.getGeneration());
    }

    /**
     *      *  .  *         .  .  .
     *      .  .  .   ->    .  .  .
     *      *  .  *         .  .  .
     */
    @Test
    public void testLiveCellWithNoLiveNeighborsDies() {
        Set<Cell> seed = new HashSet<>();
        seed.add(new Cell(0,0));
        seed.add(new Cell(0, 2));
        seed.add(new Cell(2, 0));
        seed.add(new Cell(2, 2));

        Grid grid = new Grid(GRID_COLUMNS, GRID_ROWS);
        grid.initialize(seed);
        grid.evolve();

        assertTrue(grid.getLiveCells().isEmpty());
    }

    /**
     *      *  .  .         .  .  .
     *      .  *  .   ->    .  .  .
     *      .  .  .         .  .  .
     */
    @Test
    public void testLiveCellWithOneLiveNeighborDies() {
        Set<Cell> seed = new HashSet<>();
        seed.add(new Cell(0,0));
        seed.add(new Cell(0, 1));

        Grid grid = new Grid(GRID_COLUMNS, GRID_ROWS);
        grid.initialize(seed);
        grid.evolve();

        assertTrue(grid.getLiveCells().isEmpty());
    }

    /**
     *      *  .  .         .  .  .
     *      .  *  .   ->    .  *  .
     *      .  .  *         .  .  .
     */
    @Test
    public void testLiveCellWithTwoLiveNeighborsSurvive() {
        Set<Cell> seed = new HashSet<>();
        seed.add(new Cell(0,0));
        seed.add(new Cell(1, 1));
        seed.add(new Cell(2, 2));

        Grid grid = new Grid(GRID_COLUMNS, GRID_ROWS);
        grid.initialize(seed);
        grid.evolve();

        Set<Cell> liveCells = grid.getLiveCells();
        assertEquals(1, liveCells.size());
        assertTrue(liveCells.contains(new Cell(1,1)));
    }

    /**
     *      *  *  .         *  *  .
     *      *  *  .   ->    *  *  .
     *      .  .  .         .  .  .
     */
    @Test
    public void testLiveCellWithThreeLiveNeighborsSurvive() {
        Set<Cell> seed = new HashSet<>();
        seed.add(new Cell(0,0));
        seed.add(new Cell(0, 1));
        seed.add(new Cell(1, 0));
        seed.add(new Cell(1, 1));

        Grid grid = new Grid(GRID_COLUMNS, GRID_ROWS);
        grid.initialize(seed);
        grid.evolve();

        Set<Cell> liveCells = grid.getLiveCells();
        assertEquals(4, liveCells.size());
        assertTrue(liveCells.contains(new Cell(0,0)));
        assertTrue(liveCells.contains(new Cell(0,1)));
        assertTrue(liveCells.contains(new Cell(1,0)));
        assertTrue(liveCells.contains(new Cell(1,1)));
    }

    /**
     *      *  .  *         .  *  .
     *      .  *  .   ->    *  .  *
     *      *  .  *         .  *  .
     */
    @Test
    public void testLiveCellWithMoreThanThreeLiveNeighborsDies() {
        Set<Cell> seed = new HashSet<>();
        seed.add(new Cell(0,0));
        seed.add(new Cell(0, 2));
        seed.add(new Cell(2, 0));
        seed.add(new Cell(2, 2));
        seed.add(new Cell(1, 1));

        Grid grid = new Grid(GRID_COLUMNS, GRID_ROWS);
        grid.initialize(seed);
        grid.evolve();

        // The live cells dies and dead cells with 3 neighbors become live
        Set<Cell> liveCells = grid.getLiveCells();
        assertEquals(4, liveCells.size());
        assertTrue(liveCells.contains(new Cell(0,1)));
        assertTrue(liveCells.contains(new Cell(1,0)));
        assertTrue(liveCells.contains(new Cell(2,1)));
        assertTrue(liveCells.contains(new Cell(1,2)));
    }

    /**
     *      *  .  *         .  .  .
     *      .  .  .   ->    .  *  .
     *      .  .  *         .  .  .
     */
    @Test
    public void testDeadCellWithThreeLiveNeighborsBecomesALiveCell() {
        Set<Cell> seed = new HashSet<>();
        seed.add(new Cell(0,0));
        seed.add(new Cell(2, 0));
        seed.add(new Cell(2, 2));

        Grid grid = new Grid(GRID_COLUMNS, GRID_ROWS);
        grid.initialize(seed);
        grid.evolve();

        Set<Cell> liveCells = grid.getLiveCells();
        assertEquals(1, liveCells.size());
        assertTrue(liveCells.contains(new Cell(1,1)));
    }
}
