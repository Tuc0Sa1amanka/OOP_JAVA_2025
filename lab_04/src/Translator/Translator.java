package Translator;

import java.io.File;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import Exceptions.FileReadException;
import java.io.FileNotFoundException;
import Exceptions.InvalidFileFormatException;

public final class Translator {
    private final TreeMap<String, String> dictionary;
    public Translator(String path) throws FileReadException, InvalidFileFormatException {
        dictionary = new TreeMap<>();
        try (Scanner scanner = new Scanner(new File(path))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] translation = line.split(" \\| ");
                if (translation.length != 2 || translation[0].isBlank() || translation[1].isBlank()) {
                    throw new InvalidFileFormatException("[EXPECTED FORMAT IS - SOURCE | TRANSLATION]");
                }
                dictionary.put(translation[0].toLowerCase(), translation[1].toLowerCase());
            }
        } catch (FileNotFoundException e) {
            throw new FileReadException(e);
        }
    }
    private boolean isPartOfWord(char c) {
        return Character.isLetter(c) || c == '\'' || c == '-';
    }
    public String translateLine(String line) {
        if (line == null || line.isBlank()) {
            return line;
        }
        String lowerLine = line.toLowerCase();
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < line.length()) {
            if (!isPartOfWord(lowerLine.charAt(i))) {
                result.append(line.charAt(i));
                i++;
                continue;
            }
            String longestTranslation = null;
            int maxLength = 0;
            for (Map.Entry<String, String> entry : dictionary.entrySet()) {
                String source = entry.getKey();
                if (lowerLine.startsWith(source, i)) {
                    int end = i + source.length();
                    if (end == line.length() || !Character.isLetter(lowerLine.charAt(end)) && source.length() > maxLength) {
                        longestTranslation = entry.getValue();
                        maxLength = source.length();
                    }
                }
            }
            if (longestTranslation != null) {
                if (Character.isUpperCase(line.charAt(i))) {
                    longestTranslation = Character.toUpperCase(longestTranslation.charAt(0)) + longestTranslation.substring(1);
                }
                result.append(longestTranslation);
                i += maxLength;
            } else {
                result.append(line.charAt(i));
                i++;
            }
        }
        return result.toString();
    }
    public void translateFile(String path) throws FileReadException {
        try (Scanner scanner = new Scanner(new File(path))) {
            while (scanner.hasNextLine()) {
                System.out.println(translateLine(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            throw new FileReadException(e);
        }
    }
}