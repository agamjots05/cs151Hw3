package minefield;

import mvc.*;

public class Minefield extends Model 
{
    private static final long serialVersionUID = 1L;

    private Tile[][] minefield;
    public boolean isGameOver;
    public boolean isGameWon;
    private int positionX;
    private int positionY;

    public static Integer WORLD_SIZE = 20;
    private final Integer minRows = 0;
    private final Integer minCols = 0;
    private final Integer maxRows = WORLD_SIZE - 1;
    private final Integer maxCols = WORLD_SIZE - 1;

    // Minefield constructor:
    public Minefield() {

        // Initialize minefield:
        initializeMinefield();
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

    public Point getCurrentPosition()
    {
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



    // Helper function to initialize the minefield:
    private void initializeMinefield()
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
        this.minefield[maxRows][maxCols].setMine();
        this.minefield[maxRows][maxCols].setGoal();

        // OPTIONAL: Use DFS to ensure that player has valid path to goal tile.
        if (!hasValidPath(minCols, minRows, maxCols, maxRows))
        {
            regenerateMinefield();
        }
        else
        {
            // We need to reset the isVisited flags set during the search process.
            resetVisited();

            // Set starting tile's isVisited flag.
            this.minefield[minRows][minCols].setVisited(true);

            // Call method to count adjacent mines and send to tile.
            this.minefield[minRows][minCols].updateAdjacentMines(countAdjacentMines(positionX, positionY));
        }
    }

    // Helper function to determine if a valid path exists through minefield:
    private boolean hasValidPath(int startX, int startY, int goalX, int goalY)
    {
        return DFS(startX, startY, goalX, goalY);
    }

    // Helper DFS function that searches through minefield:
    private boolean DFS(int x, int y, int goalX, int goalY)
    {
        // Check if out of bounds:
        if (!isWithinBounds(x, y))
        {
            return false;
        }

        // Check to see if the tile contains a mine of has already been visited:
        if (minefield[x][y].getMine() || minefield[x][y].getVisited())
        {
            return false;
        }

        // Otherwise, mark the tile given as parameter as visited:
        minefield[x][y].setVisited(true);

        // Method ends when we reach the goal:
        if (x == goalX && y == goalY)
        {
            return true;
        }

        // Explore neighbors (8 directions):
        int[] dx = {-1, 1, 0, 0, -1, -1, 1, 1}; // Directions for x (N, S, W, E, NW, SW, NE, SE).
        int[] dy = {0, 0, -1, 1, -1, 1, -1, 1}; // Directions for y (in the same order).

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

        // If there is no valid path, return false.
        return false;
    }

    // Helper function to check to see if a tile is within bounds (this should take any (x, y), not just the player's:
    private boolean isWithinBounds(int x, int y)
    {
        return ((x >= minCols) && (x <= maxCols) && (y >= minRows) && (y <= maxRows));
    }

    // Helper function to regenerate the minefield:
    private void regenerateMinefield()
    {
        initializeMinefield();
    }

    // Helper function to reset tile status "isVisited" after checking:
    private void resetVisited()
    {
        for (int i = minRows; i <= maxRows; i++)
        {
            for (int j = minCols; j <= maxCols; j++)
            {
                minefield[i][j].setVisited(false);
            }
        }
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

    public void move(Heading heading) throws OutofBoundsException, MineExplodedException, GameisWonException, GameisOverException
    {
        // OPTIONAL: What happens when the user clicks on a directional command after they have either lost or
        // won the game?

        if (isGameOver)
        {
            throw new GameisOverException("The game is over, please start a new game to continue playing.");
        }

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
            throw new OutofBoundsException("Out of bounds!");
        }

        // Update the position
        positionX = newPositionX;
        positionY = newPositionY;

        // Update the tile statuses
        minefield[positionY][positionX].setVisited(true);

        // Check for mine or goal
        if (minefield[positionY][positionX].getMine()) {
            isGameOver = true;
            throw new MineExplodedException("You stepped on a mine! Game over.");
        } else if (minefield[positionY][positionX].getGoal()) {
            isGameOver = true;
            isGameWon = true;
            throw new GameisWonException("Congratulations, you've reached the end!");
        }



        // Update adjacent mines count for the current position
        minefield[positionY][positionX].updateAdjacentMines(countAdjacentMines(positionX, positionY));

        // Notify the system that the model has changed.
        changed();
    }
}