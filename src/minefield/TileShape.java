package minefield;

import java.awt.*;
import java.io.Serializable;

public class TileShape implements Serializable {
    private static final long serialVersionUID = 1L;

    // Constants for tile appearance
    private static final Color DEFAULT_COLOR = Color.LIGHT_GRAY;
    private static final Color VISITED_COLOR = Color.WHITE;
    private static final Color GOAL_COLOR = Color.GREEN;
    private static final Color CURRENT_COLOR = Color.BLUE;

    // Tile properties
    private Rectangle bounds;
    private Tile tile;
    private boolean isCurrent;

    public TileShape(Rectangle bounds, Tile tile) {
        this.bounds = bounds;
        this.tile = tile;
        this.isCurrent = false;
    }

    public void draw(Graphics g) {
        // Set the color based on tile state
        if (isCurrent) {
            g.setColor(CURRENT_COLOR);
        } else if (tile.getGoal()) {
            g.setColor(GOAL_COLOR);
        } else if (tile.getVisited()) {
            g.setColor(VISITED_COLOR);
        } else {
            g.setColor(DEFAULT_COLOR);
        }

        // Fill the tile
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

        // Draw border
        g.setColor(Color.BLACK);
        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void setCurrent(boolean current) {
        this.isCurrent = current;
    }

//    public boolean contains(Point p) {
//        return bounds.contains(p);
//    }
//
//    public Rectangle getBounds() {
//        return bounds;
//    }

    public Tile getTile() {
        return tile;
    }
}