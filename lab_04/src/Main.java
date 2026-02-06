import Translator.Translator;
import Exceptions.FileReadException;
import Exceptions.InvalidFileFormatException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("[EXPECTED FORMAT IS - <DICTIONARY> <TEXT>]");
            return;
        }
        Translator translator;
        try {
            translator = new Translator(args[0]);
            translator.translateFile(args[1]);
        } catch (FileReadException | InvalidFileFormatException e) {
            System.out.println(e.getMessage());
        }
    }
}