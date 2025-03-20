package minefield;

import mvc.*;

/*
    ChangeCommand is responsible for updating the direction of the model as well as units that need to be moved.
 */
public class MoveCommand extends Command
{
    private Heading heading;

    // ChangeCommand will take a direction.
    public MoveCommand (Heading heading, Model model)
    {
        super(model);
        this.heading = heading;
    }

    // Execute implementation.
    @Override
    public void execute() throws Exception {
        if (!(model instanceof Minefield))
        {
            throw new Exception("Model must instantiate Minefield");
        }

        Minefield minefield = (Minefield) model;

        try
        {
            minefield.move(heading);
        }
        catch (GameisOverException | MineExplodedException | OutofBoundsException e)
        {
            Utilities.error(e.getMessage());
        }
        catch (GameisWonException e)
        {
            Utilities.inform(e.getMessage());
        }
    }
}
