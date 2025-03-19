package minefield;
import java.awt.Point;

import java.io.Serializable;
import mvc.*;

public class Minefield extends Model
{

    // Add these methods to your Minefield class

    private Tile[][] grid = new Tile[10][10];
    private Point currentPosition = new Point(0, 0);

    public Tile getTileAt(int row, int col) {
        if (row >= 0 && row < grid.length && col >= 0 && col < grid[0].length) {
            return grid[row][col];
        }
        return null;
    }

    public void setTileAt(int row, int col, Tile tile) {
        if (row >= 0 && row < grid.length && col >= 0 && col < grid[0].length) {
            grid[row][col] = tile;
        }
    }

    public Point getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int row, int col) {
        this.currentPosition = new Point(col, row);
    }

    public int getAdjacentMineCount(int row, int col) {
        int count = 0;

        // Check all adjacent cells
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                // Skip the current cell
                if (r == row && c == col) continue;

                // Check if the adjacent cell is valid and contains a mine
                if (r >= 0 && r < grid.length && c >= 0 && c < grid[0].length) {
                    if (grid[r][c] != null && grid[r][c].getMine()) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    // Update the move method
    public void move(Heading heading) {
        int newRow = currentPosition.y;
        int newCol = currentPosition.x;

        // Calculate new position based on heading
        switch (heading) {
            case NORTH:
                newRow--;
                break;
            case SOUTH:
                newRow++;
                break;
            case EAST:
                newCol++;
                break;
            case WEST:
                newCol--;
                break;
            case NORTH_WEST:
                newRow--;
                newCol--;
                break;
            case NORTH_EAST:
                newRow--;
                newCol++;
                break;
            case SOUTH_WEST:
                newRow++;
                newCol--;
                break;
            case SOUTH_EAST:
                newRow++;
                newCol++;
                break;
        }

        // Check if the new position is valid
        if (newRow >= 0 && newRow < grid.length && newCol >= 0 && newCol < grid[0].length) {
            // Check if there's a mine
            if (grid[newRow][newCol].getMine()) {
                // Game over - stepped on a mine
                isGameOver = true;
            } else {
                // Update position
                currentPosition = new Point(newCol, newRow);
                grid[newRow][newCol].setVisited();

                // Check if reached the goal
                if (grid[newRow][newCol].getGoal()) {
                    isGameOver = true; // Game is over because you won
                }
            }

            // Notify subscribers of the change
            changed();
        } else {
            // Invalid move - off the grid
            // You might want to throw an exception here
        }
    }

    // Initialize the grid in constructor or in a separate method
    public void initGrid() {
        grid = new Tile[10][10];
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                grid[row][col] = new Tile();
            }
        }

        // Set the goal at the bottom-right corner
        grid[grid.length-1][grid[0].length-1].setGoal();

        // Set initial position and mark as visited
        currentPosition = new Point(0, 0);
        grid[0][0].setVisited();

        isGameOver = false;
    }
}
