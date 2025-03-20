package minefield;

import mvc.*;
import java.awt.*;

public class MinefieldView extends View {
    private static final long serialVersionUID = 1L;

    // Constants for the grid size and appearance
    private static final int GRID_SIZE = 20; // Match the Minefield WORLD_SIZE
    private static final int CELL_SIZE = 25;
    private static final int PADDING = 10;

    // Grid representation
    private TileShape[][] tileShapes;
    private Point currentPosition;

    // Constructor
    public MinefieldView(Minefield model) {
        super(model);
        this.setPreferredSize(new Dimension(GRID_SIZE * CELL_SIZE + 2 * PADDING,
                GRID_SIZE * CELL_SIZE + 2 * PADDING));
        this.setBackground(Color.GRAY);
        initView();

    }

    @Override
    public void setModel(Model model) {
        super.setModel(model);
        initView();
        repaint();
    }

    // Initialize or reinitialize the view
    public void initView() {
        if (model instanceof Minefield) {
            Minefield minefield = (Minefield) model;

            // Reset the view
            tileShapes = new TileShape[GRID_SIZE][GRID_SIZE];
            currentPosition = new Point(minefield.getPositionX(), minefield.getPositionY());

            // Initialize the tile shapes
            for (int row = 0; row < GRID_SIZE; row++) {
                for (int col = 0; col < GRID_SIZE; col++) {
                    Rectangle bounds = new Rectangle(
                            PADDING + col * CELL_SIZE,
                            PADDING + row * CELL_SIZE,
                            CELL_SIZE,
                            CELL_SIZE
                    );

                    // Get the tile from the model
                    Tile tile = minefield.getTileAt(row, col);
                    if (tile == null) {
                        tile = new Tile();
                        minefield.setTileAt(row, col, tile);
                    }

                    tileShapes[row][col] = new TileShape(bounds, tile);

                    // Mark the current position
                    if (row == currentPosition.y && col == currentPosition.x) {
                        tileShapes[row][col].setCurrent(true);
                    }
                }
            }
        }
    }

    // Handle mouse clicks
    private void handleMouseClick(Point point) {
        // Implementation for mouse interactions if needed
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (tileShapes != null) {
            // Draw all tiles
            for (int row = 0; row < GRID_SIZE; row++) {
                for (int col = 0; col < GRID_SIZE; col++) {
                    if (tileShapes[row][col] != null) {
                        tileShapes[row][col].draw(g);
                    }
                }
            }

            // Draw mine count indicators for the current position
            drawMineIndicators(g);
        }
    }

    // Draw indicators showing the number of mines adjacent to the current position
    private void drawMineIndicators(Graphics g) {
        if (currentPosition != null && model instanceof Minefield) {
            Minefield minefield = (Minefield) model;
            int mineCount = minefield.getAdjacentMineCount(currentPosition.y, currentPosition.x);

            // Draw the mine count on all visited tiles
            for (int row = 0; row < GRID_SIZE; row++) {
                for (int col = 0; col < GRID_SIZE; col++) {
                    if (tileShapes[row][col].getTile().getVisited()) {
                        int count = minefield.getAdjacentMineCount(row, col);
                        int x = PADDING + col * CELL_SIZE;
                        int y = PADDING + row * CELL_SIZE;

                        g.setColor(Color.BLACK);
                        g.setFont(new Font("Arial", Font.BOLD, 14));
                        g.drawString(String.valueOf(count), x + CELL_SIZE/2 - 5, y + CELL_SIZE/2 + 5);
                    }
                    else
                    {
                        int x = PADDING + col * CELL_SIZE;
                        int y = PADDING + row * CELL_SIZE;

                        g.setColor(Color.BLACK);
                        g.setFont(new Font("Arial", Font.BOLD, 14));
                        g.drawString("?", x + CELL_SIZE/2 - 5, y + CELL_SIZE/2 + 5);
                    }
                }
            }
        }
    }

    @Override
    public void update() {
        if (model instanceof Minefield) {
            Minefield minefield = (Minefield) model;
            Point newPos = new Point(minefield.getPositionX(), minefield.getPositionY());

            // Reset the current flag on the old position
            if (currentPosition != null && isValidPosition(currentPosition.x, currentPosition.y)) {
                tileShapes[currentPosition.y][currentPosition.x].setCurrent(false);
            }

            // Update the current position
            currentPosition = newPos;

            // Mark the new position as current
            if (isValidPosition(currentPosition.x, currentPosition.y)) {
                tileShapes[currentPosition.y][currentPosition.x].setCurrent(true);
            }
        }
        repaint();
    }

    // Check if a position is valid on the grid
    private boolean isValidPosition(int x, int y) {
        return y >= 0 && y < GRID_SIZE && x >= 0 && x < GRID_SIZE;
    }
}