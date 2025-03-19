package minefield;

public class GameisWonException extends Exception
{
    public GameisWonException(String s)
    {
        super(s);
        System.out.println("GameisWonException: " + s);
    }
}
