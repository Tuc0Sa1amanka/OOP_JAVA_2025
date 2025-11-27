package services;

import annotation.Repeat;


public final class ReflectionService
{
    @Repeat(3)
    private void first(String a, String b, Integer c)
    {
        System.out.println("Вызван приватный метод first");
        System.out.println("Первое поле имеет тип String " + a);
        System.out.println("Второе поле имеет тип String " + b);
        System.out.println("Третье поле имеет тип Integer " + c);
    }

    @Repeat(5)
    private void second(int a, String b)
    {
        System.out.println("Вызван приватный метод second");
        System.out.println("Первое поле имеет тип int " + a);
        System.out.println("Второе поле имеет тип String " + b);
    }

    @Repeat(2)
    protected void third(String a, Example b)
    {
        System.out.println("Вызван защищенный метод third");
        System.out.println("Вызван конструктор с параметром Example");
        System.out.println("Первое поле имеет тип String " + a);
        System.out.println("Второе поле имеет тип " + b);
    }

    @Repeat(4)
    protected void first(double a, double b)
    {
        System.out.println("Вызван защищенный метод first");
        System.out.println("Первое поле имеет тип double " + a);
        System.out.println("Второе поле имеет тип double " + b);
    }

    @Repeat(1)
    public void second(int a)
    {
        System.out.println("Вызван публичный метод second");
        System.out.println("Первое поле имеет тип int");
    }

    public void third(String a, String b)
    {
        System.out.println("Вызван публичный метод third");
        System.out.println("Первое поле имеет тип String");
        System.out.println("Второе поле имеет тип String");
    }

    @Repeat(3)
    protected void first(String a, int b)
    {
        System.out.println("Вызван защищенный метод first");
        System.out.println("Первое поле имеет тип String" + a);
        System.out.println("Второе поле имеет тип int " + b);
    }

    @Repeat(2)
    private void second(String a, boolean b)
    {
        System.out.println("Вызван приватный метод second");
        System.out.println("Первое поле имеет тип String " + a);
        System.out.println("Второе поле имеет тип boolean " + b);
    }
}