package com.github.malpenhorn.gameoflife;

public class CLIGridPrinter implements GridPrinter {
    @Override
    public void print(Grid grid) {
        int[][] printableGrid = getPrintableGrid(grid);
        for (int y = 0; y < grid.getVisibleRows(); y++) {
            for (int x = 0; x < grid.getVisibleColumns(); x++) {
                if (printableGrid[x][y] == 0) { // The cell is dead
                    System.out.print(" . ");
                }
                else {
                    System.out.print(" * "); // The cell is live
                }
            }
            System.out.println();
        }
        System.out.println("Generation: " + grid.getGeneration());
        System.out.println("Live cells: " + grid.getLiveCells().size());
        System.out.println("______________________________");
    }

    private int[][] getPrintableGrid(Grid grid) {
        int[][] printableGrid = new int[grid.getVisibleColumns()][grid.getVisibleRows()];
        for (Cell cell : grid.getLiveCells()) {
            if (isCellWithinTheVisibleGrid(grid, cell)) {
                printableGrid[cell.getX()][cell.getY()] = 1;
            }
        }
        return printableGrid;
    }

    private boolean isCellWithinTheVisibleGrid(Grid grid, Cell cell) {
        int x = cell.getX();
        int y = cell.getY();
        if (x >= 0 && x < grid.getVisibleColumns() && y >= 0 && y < grid.getVisibleRows()) {
            return true;
        }
        return false;
    }
}
