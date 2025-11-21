package Hero;

public class WalkStrategy implements MoveStrategy
{
    public void move()
    {
        System.out.println("Герой идет пешком");
    }
}
