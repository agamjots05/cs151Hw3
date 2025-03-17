package minefield;

import java.io.Serializable;
import mvc.*;

/*
    The minefield model:
        (1) Contains the logic of movement commands (the model can only move one unit at a time in any of 8 directions).
        (2) Keeps track of the game status (whether the game is over).
        (3) Keeps track of the player's position + the # of adjacent mines to the current player position.
 */
public class Minefield extends Model implements Serializable
{
    private Tile[][] minefield;
    public boolean isGameOver;
    private Heading heading;
    private int positionX;
    private int positionY;
    private int adjacentMines;

    // Minefield constructor:
    public Minefield()
    {
        // The class example has a 20 x 20 board.
        this.minefield = new Tile[20][20];

        this.isGameOver = false;

        // Set the starting position & end goal.
        this.positionX = 0;
        this.positionY = 0;
        this.minefield[0][0].setVisited();
        this.minefield[20][20].setGoal();

        // Call method to count adjacent mines:
        this.adjacentMines = countAdjacentMines(positionX, positionY);
    }

    private int countAdjacentMines(int positionX, int positionY)
    {
    }

    public void move(Heading heading)
    {
        switch (heading)
        {
            case NORTH:
            {

            }
            case SOUTH:
            {

            }
            case EAST:
            {

            }
            case WEST:
            {

            }
            case NORTHEAST:
            {

            }
            case SOUTHEAST:
            {

            }
            case NORTHWEST:
            {

            }
            case SOUTHWEST:
            {

            }
        }
    }
}
