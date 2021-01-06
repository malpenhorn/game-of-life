package com.github.malpenhorn.gameoflife;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;

public class GUIGridPrinter implements GridPrinter {
    private final static Image LIVE_CELL = createImage(Color.BLACK);
    private final static Image DEAD_CELL = createImage(Color.WHITE);

    private final int initialGridColumns;
    private final int initialGridRows;
    private final Slider gridSizeControl;
    private final Label generationLabel;
    private final Label liveThreadsLabel;
    private final ImageView[][] maxVisibleImageViewGrid;
    private final VBox vBox;

    private ImageView[][] displayedGrid;
    private Set<Cell> liveCells;

    public GUIGridPrinter(Stage stage, GridPane controls, int initialGridColumns, int initialGridRows) {
        this.initialGridColumns = initialGridColumns;
        this.initialGridRows = initialGridRows;
        liveCells = new HashSet<>();
        displayedGrid = new ImageView[0][0];
        gridSizeControl = (Slider) ((HBox) controls.getChildren().get(3)).getChildren().get(1);
        generationLabel = new Label();
        liveThreadsLabel = new Label();

        int gridSize = (int) Math.pow(2, (int) gridSizeControl.getMax()) * 10;
        maxVisibleImageViewGrid = new ImageView[gridSize][gridSize];
        initializeMaxVisibleImageViewGrid(gridSize);

        HBox labels = new HBox(generationLabel, liveThreadsLabel);
        labels.setSpacing(100);
        labels.setPadding(new Insets(10, 10, 10, 10));

        vBox = new VBox(createGridPane(), labels, controls);
        stage.setScene(new Scene(vBox, 500, 700));
        stage.show();
    }

    private void initializeMaxVisibleImageViewGrid(int gridSize) {
        for (int y = 0 ; y < gridSize; y++) {
            for (int x = 0 ; x < gridSize; x++) {
                ImageView imageView = new ImageView(DEAD_CELL);
                maxVisibleImageViewGrid[x][y] = imageView;
            }
        }
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(1);
        gridPane.setVgap(1);
        gridPane.setStyle("-fx-background-color: grey;");
        gridPane.setAlignment(Pos.CENTER);

        gridPane.setMinSize(500, 500);
        gridPane.setMaxSize(500, 500);
        gridPane.setPrefSize(500, 500);
        return gridPane;
    }

    @Override
    public void print(Grid grid) {
        updateGrid(grid);
        liveCells = grid.getLiveCells();
        generationLabel.setText("Generation: " + grid.getGeneration());
        liveThreadsLabel.setText("Live cells: " + liveCells.size());
    }

    private void updateGrid(Grid grid) {
        int cellXTranspose = (grid.getVisibleColumns() - initialGridColumns) / 2;
        int cellYTranspose = (grid.getVisibleRows() - initialGridRows) / 2;

        if (displayedGrid.length == grid.getVisibleColumns() && displayedGrid[0].length == grid.getVisibleRows()) {
            for (Cell cell : liveCells) {
                if (isCellWithinTheVisibleGrid(grid, cell, cellXTranspose, cellYTranspose)) {
                    displayedGrid[cellXTranspose + cell.getX()][cellYTranspose + cell.getY()].setImage(DEAD_CELL);
                }
            }
            for (Cell cell : grid.getLiveCells()) {
                if (isCellWithinTheVisibleGrid(grid, cell, cellXTranspose, cellYTranspose)) {
                    displayedGrid[cellXTranspose + cell.getX()][cellYTranspose + cell.getY()].setImage(LIVE_CELL);
                }
            }
        }
        else {
            GridPane gridPane = createGridPane();
            double cellSize = Math.pow(2, 5 - (int) gridSizeControl.getValue());
            ImageView[][] printableGrid = new ImageView[grid.getVisibleColumns()][grid.getVisibleRows()];
            for (int y = 0; y < grid.getVisibleRows(); y++) {
                for (int x = 0; x < grid.getVisibleColumns(); x++) {
                    ImageView imageView = maxVisibleImageViewGrid[x][y];
                    imageView.setFitWidth(cellSize);
                    imageView.setFitHeight(cellSize);
                    imageView.setImage(DEAD_CELL);
                    printableGrid[x][y] = imageView;
                    gridPane.add(imageView, x, y);
                }
            }
            for (Cell cell : grid.getLiveCells()) {
                if (isCellWithinTheVisibleGrid(grid, cell, cellXTranspose, cellYTranspose)) {
                    printableGrid[cellXTranspose + cell.getX()][cellYTranspose + cell.getY()].setImage(LIVE_CELL);
                }
            }
            displayedGrid = printableGrid;
            vBox.getChildren().set(0, gridPane);
        }
    }

    private boolean isCellWithinTheVisibleGrid(Grid grid, Cell cell, int cellXTranspose, int cellYTranspose) {
        int x = cellXTranspose + cell.getX();
        int y = cellYTranspose + cell.getY();
        if (x >= 0 && x < grid.getVisibleColumns() && y >= 0 && y < grid.getVisibleRows()) {
            return true;
        }
        return false;
    }

    private static Image createImage(Color color) {
        WritableImage image = new WritableImage(1, 1);
        image.getPixelWriter().setColor(0, 0, color);
        return image;
    }
}
