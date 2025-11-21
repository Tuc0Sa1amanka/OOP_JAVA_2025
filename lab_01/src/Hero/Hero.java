package Hero;

public class Hero
{
    public void setMoveStrategy(MoveStrategy moveStrategy)
    {
        this.moveStrategy = moveStrategy;
    }
    public void move()
    {
        if (moveStrategy == null)
        {
            System.out.println("Стратегия передвижения не установлена");
            return;
        }
        moveStrategy.move();
    }
    MoveStrategy moveStrategy;
}
