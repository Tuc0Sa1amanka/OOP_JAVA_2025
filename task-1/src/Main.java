public class Main
{
    public static void main(String[] args)
    {
        String current = args[0];
        int sum = 0;
        while (!current.isEmpty())
        {
            for (int i = 0; i < current.length(); i++)
            {
                char digitChar = current.charAt(i);
                int temp = digitChar - '0';
                sum += temp;
            }
            if (sum < 10)
            {
                System.out.println(sum);
                return;
            }
            current = Integer.toString(sum);
            sum = 0;
        }
    }
}