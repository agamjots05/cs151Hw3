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
    public void execute() throws Exception
    {
        if (!(model instanceof Minefield))
        {
            throw new Exception("Model must instantiate Minefield");
        }

        Minefield minefield = (Minefield) model;

        // OPTIONAL: What happens when the user clicks on a directional command after they have either lost or
        // won the game?

        if (minefield.isGameOver)
        {
            throw new Exception("The game is over, please start a new game to continue playing.");
        }

        minefield.move(heading);
    }
}
