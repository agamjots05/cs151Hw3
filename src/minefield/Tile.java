package minefield;

import mvc.*;

/*
    Tiles are the individual components of a minefield. They keep track of:
    (1) their own status
        - if they are planted with a bomb
        - if they have been visited by the player
        - if they are the goal or not
    (2) # of adjacent mines.
 */
public class Tile
{
    // There is a 5% chance that a patch contains a mine.
    private static int percentMined = 5;

    // The tile either contains a mine or doesn't contain a mine.
    private boolean isMined;

    // The tile has either been visited or hasn't been visited.
    private boolean isVisited;

    // The tile is either the end goal or isn't the end goal.
    private boolean isGoal;

    private int adjacentMines;

    // Default constructor:
    public Tile ()
    {
        // Random generator between 0 and 100:
        int chance = Utilities.rng.nextInt(100);

        // Randomly determine whether a mine exists.
        this.isMined = chance < percentMined;

        this.isVisited = false;

        this.isGoal = false;

        this.adjacentMines = 0;
    }

    public boolean getMine()
    {
        return isMined;
    }

    // We need to set the starting tile and the end tile to not be mines.
    public void setMine()
    {
        isMined = false;
    }

    public void setVisited()
    {
        isVisited = true;
    }

    public boolean getVisited()
    {
        return isVisited;
    }

    public void setGoal()
    {
        isGoal = true;
    }

    public boolean getGoal()
    {
        return isGoal;
    }

    public void updateAdjacentMines(int count)
    {
        adjacentMines = count;
    }

    public int getAdjacentMines()
    {
        return adjacentMines;
    }
}
