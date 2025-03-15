package minefield;

import mvc.*;

/*
    Tiles are the individual components of a minefield. They keep track of their own status (i.e. if they are planted
    with a bomb or if they have been visited by the player). A special status is whether the tile is the goal or not.
 */
public class Tile
{
    // There is a 5% chance that a patch contains a mine.
    private static int percentMined = 5;

    // The tile either contains a mine or doesn't contain a mine.
    private boolean isMined;

    // The tile has either been visited or hasn't been visited.
    private boolean isVisited;

    private boolean isGoal;

    // Default constructor:
    public Tile ()
    {
        // Random generator between 0 and 100:
        int chance = Utilities.rng.nextInt(100);

        // Randomly determine whether a mine exists.
        isMined = chance < percentMined;

        isVisited = false;

        isGoal = false;
    }

    // We only need a getter for "isMine".
    public boolean getMine()
    {
        return isMined;
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
}
