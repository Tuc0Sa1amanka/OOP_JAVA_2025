import Hero.Hero;
import Hero.WalkStrategy;
import Hero.HorseRidingStrategy;
import Hero.AirplaneStrategy;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        System.out.println("Добро пожаловать в игру!");
        System.out.println("Выбирайте стратегию передвижения 1 - 3");
        System.out.println("1. Идти пешком");
        System.out.println("2. Ехать на лошади");
        System.out.println("3. Лететь на самолете");
        System.out.println("Введите 'exit' для выхода");
        Hero hero = new Hero();
        Scanner scanner = new Scanner(System.in);
        String strategy;
        while (!(strategy = scanner.nextLine()).equals("exit"))
        {
            switch (strategy)
            {
                case "1":
                    hero.setMoveStrategy(new WalkStrategy());
                    break;
                case "2":
                    hero.setMoveStrategy(new HorseRidingStrategy());
                    break;
                case "3":
                    hero.setMoveStrategy(new AirplaneStrategy());
                    break;
                default:
                    System.out.println("Неверный выбор! Попробуйте снова.");
                    continue;
            }
            hero.move();
        }
        System.out.println("Игра завершена");
    }
}