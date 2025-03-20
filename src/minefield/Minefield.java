package minefield;

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
    private final Integer minRows = 0;
    private final Integer minCols = 0;
    private final Integer maxRows = WORLD_SIZE - 1;
    private final Integer maxCols = WORLD_SIZE - 1;

    // Minefield constructor:
    public Minefield()
    {
        // Initialize minefield:
        initializeMinefield();
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

        System.out.println("Adjacent mines: " + mineCounter);
        return mineCounter;
    }

    // Helper function to reset tile status "isVisited" after checking:
    private void resetVisited()
    {
        for (int i = minRows; i <= maxRows; i++)
        {
            for (int j = minCols; j <= maxCols; j++)
            {
                minefield[i][j].setVisited(false);  // Reset visited status
            }
        }
    }

    // Helper DFS function:
    private boolean DFS(int x, int y, int goalX, int goalY)
    {
        // Check if out of bounds:
        if (!isWithinBounds(x, y))
        {
            return false;
        }

        // Check to see if the tile contains a mine or has already been visited:
        if (minefield[x][y].getMine() || minefield [x][y].getVisited())
        {
            return false;
        }

        // Otherwise, mark the tile given as a parameter as visited:
        minefield[x][y].setVisited(true);

        // Method ends when we reach the goal:
        if (x == goalX && y == goalY)
        {
            return true;
        }

        // Explore neighbors (8 directions):
        int[] dx = {-1, 1, 0, 0, -1, -1, 1, 1}; // Directions for x (N, S, W, E, NW, SW, NE, SE)
        int[] dy = {0, 0, -1, 1, -1, 1, -1, 1}; // Directions for y (N, S, W, E, NW, SW, NE, SE)

        // Explore each direction:
        for (int i = 0; i < 8; i++)
        {
            int newX = x + dx[i];
            int newY = y + dy[i];

            // Recursively explore the neighboring tiles:
            if (DFS(newX, newY, goalX, goalY))
            {
                return true;
            }
        }

        // If there is no valid path, return false:
        return false;
    }

    private boolean hasValidPath(int startX, int startY, int goalX, int goalY)
    {
        return DFS(startX, startY, goalX, goalY);
    }

    // Helper function to initialize the minefield:
    private void initializeMinefield()
    {
        // The class example has a 20 x 20 board.
        this.minefield = new Tile[WORLD_SIZE][WORLD_SIZE];

        for (int i = minRows; i <= maxRows; i++)
        {
            for (int j = minCols; j <= maxCols; j++)
            {
                this.minefield[i][j] = new Tile();
            }
        }

        this.isGameOver = false;

        this.isGameWon = false;

        // Set the starting position & end goal.
        this.positionX = minCols;
        this.positionY = minRows;
        this.minefield[minRows][minCols].setMine();
        this.minefield[maxRows][maxCols].setMine();
        this.minefield[maxRows][maxCols].setGoal();

        // TO-DO / OPTIONAL: Use DFS to ensure that player has valid path to goal.
        if (!hasValidPath(minCols, minRows, maxCols, maxRows))
        {
            regenerateMinefield();
            System.out.println("No valid path, regenerating... ");
        }
        else
        {
            resetVisited();
            System.out.println("Valid path found, continue playing... ");

            // Set starting tile to visited.
            this.minefield[minRows][minCols].setVisited(true);

            // Call method to count adjacent mines and send to tile.
            this.minefield[minRows][minCols].updateAdjacentMines(countAdjacentMines(positionX, positionY));
        }
    }

    // Helper function to regenerate the minefield:
    private void regenerateMinefield()
    {
        initializeMinefield();
        System.out.println("Regenerating minefield... ");
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
            minefield[positionX][positionY].setVisited(true);

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
            // changed();
        }
    }

//    public static void main(String[] args)
//    {
//        // Initialize the Minefield
//        Minefield minefield = new Minefield();
//
//        // Print the initial position
//        System.out.println("Initial Position: (" + minefield.getPositionX() + ", " + minefield.getPositionY() + ")");
//        System.out.println("Game Over: " + minefield.isGameOver);
//        System.out.println("Game Won: " + minefield.isGameWon);
//
//        // Try moving North and check the status
//        try
//        {
//            System.out.println("Moving North...");
//            minefield.move(Heading.NORTH);
//            System.out.println("New Position: (" + minefield.getPositionX() + ", " + minefield.getPositionY() + ")");
//        }
//        catch (Exception e)
//        {
//            System.out.println(e.getMessage());
//        }
//
//        // Try moving East and check the status
//        try
//        {
//            System.out.println("Moving East...");
//            minefield.move(Heading.EAST);
//            System.out.println("New Position: (" + minefield.getPositionX() + ", " + minefield.getPositionY() + ")");
//        }
//        catch (Exception e)
//        {
//            System.out.println(e.getMessage());
//        }
//
//        // Try moving South and check the status
//        try
//        {
//            System.out.println("Moving South...");
//            minefield.move(Heading.SOUTH);
//            System.out.println("New Position: (" + minefield.getPositionX() + ", " + minefield.getPositionY() + ")");
//        }
//        catch (Exception e)
//        {
//            System.out.println(e.getMessage());
//        }
//
//        // Try moving West and check the status
//        try
//        {
//            System.out.println("Moving West...");
//            minefield.move(Heading.WEST);
//            System.out.println("New Position: (" + minefield.getPositionX() + ", " + minefield.getPositionY() + ")");
//        }
//        catch (Exception e)
//        {
//            System.out.println(e.getMessage());
//        }
//
//        // Check if the game is won or lost
//        if (minefield.isGameWon)
//        {
//            System.out.println("Congratulations! You've won the game!");
//        }
//        else if (minefield.isGameOver)
//        {
//            System.out.println("Game Over! You stepped on a mine.");
//        }
//        else
//        {
//            System.out.println("The game is still ongoing.");
//        }
//    }
}
