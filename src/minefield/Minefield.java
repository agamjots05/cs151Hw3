package minefield;

import java.io.Serializable;
import mvc.*;

public class Minefield extends Model implements Serializable {
    private static final long serialVersionUID = 1L;

    private Tile[][] minefield;
    public boolean isGameOver;
    public boolean isGameWon;
    private Heading heading;
    private int positionX;
    private int positionY;

    public static Integer WORLD_SIZE = 20;
    private Integer minRows = 0;
    private Integer minCols = 0;
    private Integer maxRows = WORLD_SIZE - 1;
    private Integer maxCols = WORLD_SIZE - 1;

    // Minefield constructor:
    public Minefield() {
        // The class example has a 20 x 20 board.
        this.minefield = new Tile[WORLD_SIZE][WORLD_SIZE];

        for (int i = minRows; i <= maxRows; i++) {
            for (int j = minCols; j <= maxCols; j++) {
                minefield[i][j] = new Tile();
            }
        }

        this.isGameOver = false;
        this.isGameWon = false;

        // Set the starting position & end goal.
        this.positionX = minCols;
        this.positionY = minRows;
        this.minefield[minRows][minCols].setMine();
        this.minefield[minRows][minCols].setVisited();
        this.minefield[maxRows][maxCols].setMine();
        this.minefield[maxRows][maxCols].setGoal();

        // Call method to count adjacent mines and send to tile.
        this.minefield[minRows][minCols].updateAdjacentMines(countAdjacentMines(positionX, positionY));
    }

    // Setters and getters:
    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    // Add these methods for MinefieldView
    public Tile getTileAt(int row, int col) {
        if (isWithinBounds(col, row)) {
            return minefield[row][col];
        }
        return null;
    }

    public void setTileAt(int row, int col, Tile tile) {
        if (isWithinBounds(col, row)) {
            minefield[row][col] = tile;
        }
    }

    public Point getCurrentPosition() {
        return new Point(positionX, positionY);
    }

    public void setCurrentPosition(int row, int col) {
        if (isWithinBounds(col, row)) {
            positionX = col;
            positionY = row;
        }
    }

    public int getAdjacentMineCount(int row, int col) {
        if (isWithinBounds(col, row)) {
            return countAdjacentMines(col, row);
        }
        return 0;
    }

    // Helper function to check to see if a tile is within bounds (this should take any (x, y), not just the player's:
    public boolean isWithinBounds(int x, int y) {
        return ((x >= minCols) && (x <= maxCols) && (y >= minRows) && (y <= maxRows));
    }

    // Helper function to count the number of adjacent mines and update it to Tile.
    private int countAdjacentMines(int positionX, int positionY) {
        int mineCounter = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Skip the center mine (A.K.A. where player is currently standing).
                if (i == 0 && j == 0) {
                    continue;
                }

                int newPositionX = positionX + i;
                int newPositionY = positionY + j;

                if (isWithinBounds(newPositionX, newPositionY)) {
                    if (minefield[newPositionY][newPositionX].getMine()) {
                        mineCounter++;
                    }
                }
            }
        }

        return mineCounter;
    }

    public void move(Heading heading) throws MineExplodedException, GameisWonException {
        int newPositionX = positionX;
        int newPositionY = positionY;

        switch (heading) {
            case NORTH:
                newPositionY--;
                break;
            case SOUTH:
                newPositionY++;
                break;
            case EAST:
                newPositionX++;
                break;
            case WEST:
                newPositionX--;
                break;
            case NORTHEAST:
                newPositionX++;
                newPositionY--;
                break;
            case SOUTHEAST:
                newPositionX++;
                newPositionY++;
                break;
            case NORTHWEST:
                newPositionX--;
                newPositionY--;
                break;
            case SOUTHWEST:
                newPositionX--;
                newPositionY++;
                break;
        }

        // Check if the new position is within bounds
        if (!isWithinBounds(newPositionX, newPositionY)) {
            throw new IndexOutOfBoundsException("Out of bounds!");
        }

        // Update the position
        positionX = newPositionX;
        positionY = newPositionY;

        // Update the tile statuses
        minefield[positionY][positionX].setVisited();

        // Check for mine or goal
        if (minefield[positionY][positionX].getMine()) {
            isGameOver = true;
            throw new MineExplodedException("The mine beneath your feet exploded!");
        } else if (minefield[positionY][positionX].getGoal()) {
            isGameOver = true;
            isGameWon = true;
            throw new GameisWonException("You reached the goal!");
        }

        // Update adjacent mines count for the current position
        minefield[positionY][positionX].updateAdjacentMines(countAdjacentMines(positionX, positionY));

        // Notify the system that the model has changed.
        changed();
    }
}