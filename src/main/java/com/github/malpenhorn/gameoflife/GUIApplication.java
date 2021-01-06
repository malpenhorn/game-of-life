package com.github.malpenhorn.gameoflife;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class GUIApplication extends Application {
    private final static int INITIAL_GRID_COLUMNS = 10;
    private final static int INITIAL_GRID_ROWS = 10;

    private GridPane controls;
    private Slider cellLivePercentSlider;
    private Slider speedSlider;
    private Slider gridSizeSlider;
    private Thread simulationThread;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Game of Life");
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(close -> {
            Platform.exit();
            System.exit(0);
        });

        createControls(primaryStage);
        startNewSimulation(primaryStage);
    }

    private void createControls(Stage primaryStage) {
        controls = new GridPane();
        controls.setPadding(new Insets(10, 10, 10, 10));
        controls.setHgap(20);
        controls.setVgap(20);

        cellLivePercentSlider = getCellLivePercentSlider();
        Label cellLivePercentLabel = new Label("Cell live percent: ");
        HBox cellLivePercentControl = new HBox(cellLivePercentLabel, cellLivePercentSlider);

        speedSlider = getSpeedSlider();
        Label speedLabel = new Label("Speed: ");
        HBox speedControl = new HBox(speedLabel, speedSlider);

        gridSizeSlider = getGridSizeSlider();
        Label gridSizeLabel = new Label("Grid size: ");
        HBox gridSizeControl = new HBox(gridSizeLabel, gridSizeSlider);

        Button restartButton = new Button("Restart");
        restartButton.setDefaultButton(true);
        restartButton.setOnAction(actionEvent ->  {
            simulationThread.interrupt();
            startNewSimulation(primaryStage);
        });

        controls.add(restartButton, 0, 0);
        controls.add(cellLivePercentControl, 1, 0);
        controls.add(speedControl, 0, 1);
        controls.add(gridSizeControl, 1, 1);
    }

    private Slider getCellLivePercentSlider() {
        Slider speedControl = new Slider(0, 100, 30);
        speedControl.setShowTickLabels(true);
        speedControl.setShowTickMarks(true);
        speedControl.setMajorTickUnit(20);
        speedControl.setMinorTickCount(0);
        speedControl.setBlockIncrement(5);
        return speedControl;
    }

    private Slider getSpeedSlider() {
        Slider speedControl = new Slider(0, 20, 10);
        speedControl.setShowTickLabels(true);
        speedControl.setShowTickMarks(true);
        speedControl.setMajorTickUnit(5);
        speedControl.setMinorTickCount(1);
        speedControl.setBlockIncrement(1);
        return speedControl;
    }

    private Slider getGridSizeSlider() {
        Slider gridSizeControl = new Slider(0, 4, 0);
        gridSizeControl.setShowTickLabels(true);
        gridSizeControl.setShowTickMarks(true);
        gridSizeControl.setMajorTickUnit(1);
        gridSizeControl.setMinorTickCount(0);
        gridSizeControl.setBlockIncrement(1);
        gridSizeControl.setSnapToTicks(true);
        return gridSizeControl;
    }

    private void startNewSimulation(Stage primaryStage) {
        Grid grid = new Grid(INITIAL_GRID_COLUMNS, INITIAL_GRID_ROWS);
        grid.initialize(cellLivePercentSlider.getValue() / 100);
        GridPrinter printer = new GUIGridPrinter(primaryStage, controls, INITIAL_GRID_COLUMNS, INITIAL_GRID_ROWS);
        simulationThread = new Thread(() -> {
            boolean running = true;
            while (running) {
                int gridSize = (int) Math.pow(2, (int) gridSizeSlider.getValue()) * 10;
                Platform.runLater(() -> {
                    grid.setVisibleColumns(gridSize);
                    grid.setVisibleRows(gridSize);
                    printer.print(grid);
                    grid.evolve();
                });

                try {
                    Thread.sleep(2100 - (int) speedSlider.getValue() * 100);
                } catch (InterruptedException e) {
                    // Current simulation is terminated
                    running = false;
                }
            }
        });
        simulationThread.start();
    }
}
