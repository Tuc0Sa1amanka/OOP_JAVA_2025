package utils;

import java.util.*;
import java.util.stream.Collectors;

public final class StreamOperations
{
    public static OptionalDouble getAverage(List<Integer> list)
    {
        return list.stream().mapToInt(Integer::intValue).average();
    }
    public static List<String> toUpperCaseWithPrefix(List<String> list)
    {
        return list.stream().map(x -> "new" + x.toUpperCase()).toList();
    }
    public static List<Integer> uniqueSquares(List<Integer> list)
    {
        return list.stream()
                .filter(element -> Collections.frequency(list, element) == 1)
                .map(element -> element * element)
                .toList();
    }
    public static Collection<String> filterByFirstLetterSorted(Collection<String> strings, char letter)
    {
        return strings.stream()
                .filter(s -> s != null && !s.isEmpty() && s.charAt(0) == letter)
                .sorted()
                .toList();
    }
    public static <T> T getLastElement(Collection<T> data)
    {
        return data.stream()
                .reduce((_, second) -> second)
                .orElseThrow(() -> new NoSuchElementException("Empty collection"));
    }
    public static int sumEvenNumbers(int[] numbers)
    {
        return Arrays.stream(numbers)
                .filter(n -> n % 2 == 0)
                .sum();
    }
    public static Map<Character, String> convertStringsToMap(List<String> strings)
    {
        return strings.stream()
                .filter(s -> s != null && !s.isEmpty())
                .collect(Collectors.toMap(
                        s -> s.charAt(0),
                        s -> s.substring(1),
                        (_, second) -> second
                ));
    }
}
