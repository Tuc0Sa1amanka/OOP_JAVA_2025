import animals.*;

import java.util.ArrayList;

public class Main
{
    @SuppressWarnings("NonAsciiCharacters")
    public static void main(String[] args)
    {
        ArrayList<Mammals> Млекопитающие = new ArrayList<>();
        Млекопитающие.add(new Mammals());
        Млекопитающие.add(new Insectivores());
        Млекопитающие.add(new Carnivores());
        Млекопитающие.add(new Hedgehogs());
        Млекопитающие.add(new Felidae());

        Млекопитающие.add(new EuropeanHedgehog());
        Млекопитающие.add(new EuropeanHedgehog());
        Млекопитающие.add(new EuropeanHedgehog());

        Млекопитающие.add(new Manul());

        Млекопитающие.add(new Lynx());
        Млекопитающие.add(new Lynx());

        ArrayList<Carnivores> Хищные = new ArrayList<>();
        Хищные.add(new Carnivores());
        Хищные.add(new Felidae());

        Хищные.add(new Manul());

        Хищные.add(new Lynx());

        ArrayList<Hedgehogs> Eжовые = new ArrayList<>();
        Eжовые.add(new Hedgehogs());
        Eжовые.add(new EuropeanHedgehog());
        Eжовые.add(new EuropeanHedgehog());

        ArrayList<Felidae> Кошачьи = new ArrayList<>();
        ArrayList<Manul> Манулы = new ArrayList<>();
        ArrayList<Chordates> Хордовые = new ArrayList<>();
        ArrayList<Insectivores> Насекомоядные = new ArrayList<>();

        segregate(Млекопитающие, Eжовые, Кошачьи, Хищные);
        segregate(Хищные, Хордовые, Манулы, Кошачьи);
        segregate(Eжовые, Насекомоядные, Хищные, Хищные);
    }
    public static void segregate(ArrayList<? extends Chordates> SrcCollection, ArrayList<? super EuropeanHedgehog> Collection1, ArrayList<? super Manul> Collection2, ArrayList<? super Lynx> Collection3)
    {
        ArrayList<Object> ex = new ArrayList<>();
        ex.add("string");
        ex.add(null);

        int hCount = 0;
        int mCount = 0;
        int lCount = 0;
        for (Chordates animal : SrcCollection)
        {
            if (animal instanceof EuropeanHedgehog hedgehog)
            {
                Collection1.add(hedgehog);
                hCount++;
            }
            else if (animal instanceof Manul manul)
            {
                Collection2.add(manul);
                mCount++;
            }
            else if (animal instanceof Lynx lynx)
            {
                Collection3.add(lynx);
                lCount++;
            }
        }
        System.out.println(hCount);
        System.out.println(mCount);
        System.out.println(lCount);
    }
}