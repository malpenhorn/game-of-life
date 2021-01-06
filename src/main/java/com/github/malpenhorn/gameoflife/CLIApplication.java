package com.github.malpenhorn.gameoflife;

public class CLIApplication {
    private final static int GRID_COLUMNS = 10;
    private final static int GRID_ROWS = 10;

    public static void main(String[] args) {
        Grid grid = new Grid(GRID_COLUMNS, GRID_ROWS);
        grid.initialize(0.25);
        GridPrinter printer = new CLIGridPrinter();
        while (true) {
            printer.print(grid);
            grid.evolve();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
