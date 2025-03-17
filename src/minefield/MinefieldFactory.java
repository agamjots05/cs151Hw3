package minefield;

import mvc.*;

public class MinefieldFactory implements AppFactory {

    public Model makeModel() { return new Minefield(); }

    public View makeView(Model m) {
        return new MinefieldView((Minefield)m);
    }

    public String[] getEditCommands() { return new String[] {"NW", "N", "NE", "W", "E", "SW", "S", "SE"}; }

    // source added 3/15 to support text fields
    public Command makeEditCommand(Model model, String type, Object source) {
        if (type == "NW") return new MoveCommand(Heading.NORTH_WEST, model);
        if (type == "N") return new MoveCommand(Heading.NORTH, model);
        if (type == "NE") return new MoveCommand(Heading.NORTH_EAST, model);
        if (type == "W") return new MoveCommand(Heading.WEST, model);
        if (type == "E") return new MoveCommand(Heading.EAST, model);
        if (type == "SW") return new MoveCommand(Heading.SOUTH_WEST, model);
        if (type == "S") return new MoveCommand(Heading.SOUTH, model);
        if (type == "SE") return new MoveCommand(Heading.SOUTH_EAST, model);
        return null;
    }

    public String getTitle() { return "Mine Field"; }

    public String[] getHelp() {
        return new String[] {
            "NW: Move a step in the direction of Northwest",
            "N: Move a step in the direction of North",
            "NE: Move a step in the direction of Northeast",
            "W: Move a step in the direction of West",
            "E: Move a step in the direction of East",
            "SW: Move a step in the direction of Southwest",
            "S: Move a step in the direction of South",
            "SE: Move a step in the direction of Southeast"
        };
    }

    public String about() {
        return "Mine Field, by Team 2: Agamjot Singh, Canhui Huang, and Gloria Duo";
    }

}
