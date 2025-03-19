package minefield;

import mvc.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MinefieldView extends View implements Serializable {
    private static final long serialVersionUID = 1L;

    // Constants for the grid size and appearance
    private static final int GRID_SIZE = 10; // 10x10 grid
    private static final int CELL_SIZE = 40;
    private static final int PADDING = 20;

    // Grid representation
    private TileShape[][] tileShapes;
    private Point currentPosition;
    private boolean gridInitialized = false;

    // Constructor
    public MinefieldView(Minefield model) {
        super(model);
        this.setPreferredSize(new Dimension(GRID_SIZE * CELL_SIZE + 2 * PADDING,
                GRID_SIZE * CELL_SIZE + 2 * PADDING));
        this.setBackground(Color.GRAY);
        initView();

        // Add mouse listener for potential click interactions
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getPoint());
            }
        });
    }

    @Override
    public void setModel(Model model) {
        super.model = model;
        initView();
        repaint();
    }

    // Initialize or reinitialize the view
    public void initView() {
        if (model instanceof Minefield) {
            Minefield minefield = (Minefield) model;

            // Reset the view if this is a new model
            if (!gridInitialized) {
                tileShapes = new TileShape[GRID_SIZE][GRID_SIZE];
                currentPosition = new Point(0, 0); // Start position (top-left)
                gridInitialized = true;

                // Initialize the tile shapes
                for (int row = 0; row < GRID_SIZE; row++) {
                    for (int col = 0; col < GRID_SIZE; col++) {
                        Rectangle bounds = new Rectangle(
                                PADDING + col * CELL_SIZE,
                                PADDING + row * CELL_SIZE,
                                CELL_SIZE,
                                CELL_SIZE
                        );

                        // Get the actual Tile from the model, or create a new one if it doesn't exist
                        Tile tile = minefield.getTileAt(row, col);
                        if (tile == null) {
                            tile = new Tile(); // Default initialization
                            if (row == GRID_SIZE - 1 && col == GRID_SIZE - 1) {
                                tile.setGoal(); // Bottom-right is the goal
                            }
                            minefield.setTileAt(row, col, tile);
                        }

                        tileShapes[row][col] = new TileShape(bounds, tile);
                    }
                }

                // Set the current position tile as visited
                updateCurrentPosition(0, 0);
            }
        }
    }

    // Update current position and mark as visited
    public void updateCurrentPosition(int row, int col) {
        // Reset the current flag on the old position
        if (currentPosition != null && isValidPosition(currentPosition.x, currentPosition.y)) {
            tileShapes[currentPosition.y][currentPosition.x].setCurrent(false);
        }

        // Update the current position
        currentPosition = new Point(col, row);

        // Mark the new position as current and visited
        if (isValidPosition(col, row)) {
            Tile tile = tileShapes[row][col].getTile();
            tile.setVisited();
            tileShapes[row][col].setCurrent(true);

            // Update the model's current position
            if (model instanceof Minefield) {
                ((Minefield) model).setCurrentPosition(row, col);
            }
        }
    }

    // Check if a position is valid on the grid
    private boolean isValidPosition(int col, int row) {
        return row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE;
    }

    // Handle mouse clicks (optional)
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

            // Draw the path (if needed)
            drawPath(g);

            // Draw mine count indicators for the current position
            drawMineIndicators(g);
        }
    }

    // Draw the path taken by the player
    private void drawPath(Graphics g) {
        // Implementation for drawing the path if needed
    }

    // Draw indicators showing the number of mines adjacent to the current position
    private void drawMineIndicators(Graphics g) {
        if (currentPosition != null && model instanceof Minefield) {
            Minefield minefield = (Minefield) model;
            int mineCount = minefield.getAdjacentMineCount(currentPosition.y, currentPosition.x);

            if (mineCount >= 0) {
                int x = PADDING + currentPosition.x * CELL_SIZE;
                int y = PADDING + currentPosition.y * CELL_SIZE;

                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 16));
                g.drawString(String.valueOf(mineCount), x + CELL_SIZE/2 - 5, y + CELL_SIZE/2 + 5);
            }
        }
    }

    @Override
    public void update() {
        if (model instanceof Minefield) {
            Minefield minefield = (Minefield) model;
            Point newPos = minefield.getCurrentPosition();

            if (newPos != null && (currentPosition == null ||
                    newPos.x != currentPosition.x || newPos.y != currentPosition.y)) {
                updateCurrentPosition(newPos.y, newPos.x);
            }
        }
        repaint();
    }
}