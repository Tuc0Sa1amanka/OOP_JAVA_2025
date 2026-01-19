import java.util.List;
import java.util.ArrayList;
import utils.StreamOperations;

public class Main
{
    public static void main(String[] args)
    {
        List<Integer> list1 = new ArrayList<>(List.of(1,2,3,4,5));
        System.out.println(StreamOperations.getAverage(list1));

        List<String> list2 = new ArrayList<>(List.of("string1", "string2", "string3"));
        System.out.println(StreamOperations.toUpperCaseWithPrefix(list2));

        List<Integer> list3 = new ArrayList<>(List.of(1, 2, 3, 3, 3, 4, 5, 6, 7, 4));
        System.out.println(StreamOperations.uniqueSquares(list3));

        List<String> list4 = new ArrayList<>(List.of("", " ", "13", "lalala", "1gksk", "ffjjf", "laba"));
        list4.add(null);
        System.out.println(StreamOperations.filterByFirstLetterSorted(list4, 'l'));

        List<Integer> list5 = new ArrayList<>(List.of(1,2,3,4,5,6));
        System.out.println(StreamOperations.getLastElement(list5));

        int[] arr = {1,2,3,4,5,6};
        System.out.println(StreamOperations.sumEvenNumbers(arr));

        List<String> testData = new ArrayList<>(List.of("apple", "", "banana", "a", "cherry"));
        System.out.println(StreamOperations.convertStringsToMap(testData));
    }
}