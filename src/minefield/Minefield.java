package minefield;

import java.awt.dnd.InvalidDnDOperationException;
import java.io.Serializable;
import mvc.*;

/*
    The minefield model:
        (1) Contains the logic of movement commands (the model can only move one unit at a time in any of 8 directions).
        (2) Keeps track of the game status (whether the game is over).
        (3) Keeps track of the player's position + the # of adjacent mines to the current player position.
 */
public class Minefield extends Model
{
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
    public Minefield()
    {
        // The class example has a 20 x 20 board.
        this.minefield = new Tile[WORLD_SIZE][WORLD_SIZE];

        for (int i = minRows; i <= maxRows; i++)
        {
            for (int j = minCols; j <= maxCols; j++)
            {
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

        // TO-DO / OPTIONAL: Use DFS to ensure that player has valid path to goal.

        // Call method to count adjacent mines and send to tile.
        this.minefield[minRows][minCols].updateAdjacentMines(countAdjacentMines(positionX, positionY));
    }

    // Setters and getters:
    public int getPositionX()
    {
        return positionX;
    }

    public int getPositionY()
    {
        return positionY;
    }

    // Helper function to check to see if a tile is within bounds (this should take any (x, y), not just the player's:
    public boolean isWithinBounds(int x, int y)
    {
        return ((x >= minCols) && (x <= maxCols) && (y >= minRows) && (y <= maxCols));
    }

    // Helper function to count the number of adjacent mines and update it to Tile.
    private int countAdjacentMines(int positionX, int positionY)
    {
        int mineCounter = 0;

        for (int i = -1; i <= 1; i++)
        {
            for (int j = -1; j <= 1; j++)
            {
                // Skip the center mine (A.K.A. where player is currently standing).
                if (i == 0 && j == 0)
                {
                    continue;
                }

                int newPositionX = positionX + i;
                int newPositionY = positionY + j;

                if (isWithinBounds(newPositionX, newPositionY))
                {
                    if (minefield[newPositionX][newPositionY].getMine())
                    {
                        mineCounter++;
                    }
                }
            }
        }

        return mineCounter;
    }

    public void move(Heading heading) throws MineExplodedException, GameisWonException
    {
        switch (heading)
        {
            case NORTH:
            {
                if (--positionY < minRows)
                {
                    positionY++;
                    throw new IndexOutOfBoundsException("Out of bounds!");
                }
            }
            case SOUTH:
            {
                if (++positionY > maxRows)
                {
                    positionY--;
                    throw new IndexOutOfBoundsException("Out of bounds!");
                }

            }
            case EAST:
            {
                if (++positionX > maxCols)
                {
                    positionX--;
                    throw new IndexOutOfBoundsException("Out of bounds!");
                }
            }
            case WEST:
            {
                if (--positionX < minCols)
                {
                    positionX++;
                    throw new IndexOutOfBoundsException("Out of bounds!");
                }
            }
            case NORTHEAST:
            {
                if ((positionX == maxCols) || (positionY == minRows))
                {
                    throw new IndexOutOfBoundsException("Out of bounds!");
                }
                else
                {
                    positionX++;
                    positionY--;
                }
            }
            case SOUTHEAST:
            {
                if ((positionX == maxCols) || (positionY == maxCols))
                {
                    throw new IndexOutOfBoundsException("Out of bounds!");
                }
                else
                {
                    positionX++;
                    positionY++;
                }
            }
            case NORTHWEST:
            {
                if ((positionX == minCols) || (positionY == minRows))
                {
                    throw new IndexOutOfBoundsException("Out of bounds!");
                }
                else
                {
                    positionX--;
                    positionY--;
                }
            }
            case SOUTHWEST:
            {
                if ((positionX == minCols) || (positionY == maxRows))
                {
                    throw new IndexOutOfBoundsException("Out of bounds!");
                }
                else
                {
                    positionX--;
                    positionY++;
                }
            }

            // After updating player position, we need to update the tile statuses.
            minefield[positionX][positionY].setVisited();

            if (minefield[positionX][positionY].getMine())
            {
                isGameOver = true;
                throw new MineExplodedException("The mine beneath your feet exploded!");
            }
            else if (minefield[positionX][positionY].getGoal())
            {
                isGameOver = true;
                isGameWon = true;
                throw new GameisWonException("You reached the goal!");
            }

            // We also need to notify the system that the model has changed.
            changed();
        }
    }
}
