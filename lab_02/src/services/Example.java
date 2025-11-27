package services;

public class Example
{
    int a;
    double b;
    Example(int a, double b)
    {
        this.a = a;
        this.b = b;
    }

    @Override
    public String toString()
    {
        return "Example{a=" + a + ", b=" + b + "}";
    }
}
