package minefield;

public class MineExplodedException extends Exception
{
    public MineExplodedException(String s)
    {
        super(s);
        System.out.println("MineExplodedException: " + s);
    }
}
