# game-of-life
 An implementation of Conway's Game of Life.

The main game logic is contained within Grid.java. There are two different output modes available, a GUI (GUIApplication.java) and a simple CLI (CLIApplication.java).

If you create a JAR you can run the game either of these commands:

`java -jar game-of-life.jar` runs with the GUI

`java -cp game-of-life.jar com.github.malpenhorn.gameoflife.GUIApplication` runs with the GUI

`java -cp game-of-life.jar com.github.malpenhorn.gameoflife.CLIApplication` runs with CLI
