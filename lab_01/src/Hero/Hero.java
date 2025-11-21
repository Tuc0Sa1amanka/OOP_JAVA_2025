package Hero;

public class Hero
{
    public void setMoveStrategy(MoveStrategy moveStrategy)
    {
        this.moveStrategy = moveStrategy;
    }
    public void move()
    {
        moveStrategy.move();
    }
    MoveStrategy moveStrategy;
}
