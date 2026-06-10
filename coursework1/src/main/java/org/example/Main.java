package org.example;

import java.util.*;
import api.ApiRegistry;
import viewer.FileViewer;
import java.io.IOException;
import service.ApiProcessor;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    static FileViewer FILE_VIEWER = new FileViewer();
    public static void main(String[] args) {
        AtomicReference<ApiProcessor> processorRef = new AtomicReference<>(null);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ApiProcessor p = processorRef.get();
            if (p != null) {
                System.out.println("\nЗавершение работы...");
                p.stop();
            }
        }));
        if (args.length == 0) {
            runInteractiveMode(processorRef);
        } else if (args.length >= 4) {
            runAutoMode(args, processorRef);
        } else {
            System.out.println("Использование:");
            System.out.println("  Интерактивный: без параметров командной строки");
            System.out.println("  Автоматический: <n> <t> <format> <api1> <api2> ... <apin>");
            System.out.println("  Пример: 8 6 json jira police potter");
        }
    }
    private static void runAutoMode(String[] args, AtomicReference<ApiProcessor> processorRef) {
        int n;
        long t;
        try {
            n = Integer.parseInt(args[0]);
            t = Long.parseLong(args[1]);
            if (n < 1) {
                System.out.println("Количество потоков n должно быть >= 1");
                return;
            }
            if (t < 0) {
                System.out.println("Интервал t не может быть отрицательным");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("n и t должны быть целыми числами (n > 0, t >= 0)");
            return;
        }
        String format = args[2].toLowerCase();
        if (!format.equals("json") && !format.equals("csv")) {
            System.out.println("Неверный формат. Допустимо: json или csv");
            return;
        }
        String[] apiNames = Arrays.copyOfRange(args, 3, args.length);
        ApiRegistry registry = new ApiRegistry();
        ApiProcessor processor = new ApiProcessor(registry, n, t);
        processorRef.set(processor);
        processor.start(apiNames, format, false);
        System.out.println("Опрос запущен. API: " + Arrays.toString(apiNames)
                + ", n=" + n + ", t=" + t + "с, формат=" + format);
        System.out.println("Введите что угодно для остановки (или Ctrl+C).");
        try (Scanner scanner = new Scanner(System.in)) {
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
        }
        processor.stop();
        processorRef.set(null);
    }
    private static void runInteractiveMode(AtomicReference<ApiProcessor> processorRef) {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = readInt(scanner, "Количество потоков n (>= 1): ", 1, Integer.MAX_VALUE);
            int t = readInt(scanner, "Интервал опроса t, секунд (>= 0): ", 0, Integer.MAX_VALUE);
            List<String> apiNames = selectAPIs(scanner);
            String format = selectFormat(scanner);
            boolean appendMode = selectWriteMode(scanner);
            ApiRegistry registry = new ApiRegistry();
            System.out.println("\nКоманды: run, stop, view, exit");
            while (scanner.hasNextLine()) {
                System.out.print("> ");
                String cmd = scanner.nextLine().trim().toLowerCase();
                switch (cmd) {
                    case "run":
                        if (processorRef.get() != null) {
                            System.out.println("Опрос уже запущен. Сначала выполните stop.");
                        } else {
                            ApiProcessor processor = new ApiProcessor(registry, n, t);
                            processorRef.set(processor);
                            processor.start(apiNames.toArray(new String[0]), format, appendMode);
                            System.out.println("Опрос запущен (режим: " + (appendMode ? "дозапись" : "новый") + ").");
                        }
                        break;
                    case "stop":
                        ApiProcessor toStop = processorRef.getAndSet(null);
                        if (toStop == null) {
                            System.out.println("Опрос не запущен.");
                        } else {
                            toStop.stop();
                            System.out.println("Опрос остановлен.");
                        }
                        break;
                    case "view":
                        if (processorRef.get() != null) {
                            System.out.println("Сначала остановите опрос (stop).");
                        } else {
                            showFileContent(scanner, format);
                        }
                        break;
                    case "exit":
                        ApiProcessor toExit = processorRef.getAndSet(null);
                        if (toExit != null) toExit.stop();
                        System.out.println("Выход.");
                        return;
                    default:
                        if (!cmd.isEmpty()) {
                            System.out.println("Неизвестная команда. Доступно: run, stop, view, exit");
                        }
                }
            }
        }
    }
    private static boolean selectWriteMode(Scanner scanner) {
        while (true) {
            System.out.println("\nРежим записи:");
            System.out.println("  1. Новый файл (перезаписать)");
            System.out.println("  2. Добавить в существующий");
            System.out.print("Выбор (1-2): ");
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1": return false;
                case "2": return true;
                default: System.out.println("Ошибка: выберите 1 или 2.");
            }
        }
    }
    private static int readInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value < min || value > max) {
                    System.out.println("Ошибка: введите число от " + min + " до " + max + ".");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: требуется целое число.");
            }
        }
    }
    private static List<String> selectAPIs(Scanner scanner) {
        String[] available = {"jira", "police", "potter"};
        List<String> selected = new ArrayList<>();
        while (true) {
            System.out.println("\nДоступные API:");
            for (int i = 0; i < available.length; i++) {
                System.out.println("  " + (i + 1) + ". " + available[i]);
            }
            if (!selected.isEmpty()) {
                System.out.println("Уже выбрано: " + selected);
            }
            System.out.println("  0. Завершить выбор");
            System.out.print("Выбор: ");
            String input = scanner.nextLine().trim();
            if (input.equals("0")) {
                if (selected.isEmpty()) {
                    System.out.println("Ошибка: выберите хотя бы один API.");
                    continue;
                }
                return selected;
            }
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число.");
                continue;
            }
            if (choice < 1 || choice > available.length) {
                System.out.println("Ошибка: неверный номер API.");
                continue;
            }
            String apiName = available[choice - 1];
            int count = readInt(scanner, "Сколько раз добавить " + apiName + "? ", 1, 100);
            for (int i = 0; i < count; i++) selected.add(apiName);
            System.out.println("Добавлено: " + apiName + " x" + count);
        }
    }
    private static String selectFormat(Scanner scanner) {
        while (true) {
            System.out.println("\nФормат сохранения:");
            System.out.println("  1. json");
            System.out.println("  2. csv");
            System.out.print("Выбор (1-2): ");
            switch (scanner.nextLine().trim()) {
                case "1": return "json";
                case "2": return "csv";
                default: System.out.println("Ошибка: выберите 1 или 2.");
            }
        }
    }
    private static void showFileContent(Scanner scanner, String format) {
        System.out.println("\nПросмотр файла:");
        System.out.println("  1. Весь файл");
        System.out.println("  2. Записи по конкретному API");
        System.out.print("Выбор: ");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                try {
                    FILE_VIEWER.showFull(format);
                } catch (IOException e) {
                    System.out.println("Ошибка чтения файла: " + e.getMessage());
                }
                break;
            case "2":
                System.out.println("\nВыберите API:");
                System.out.println("  1. jira");
                System.out.println("  2. police");
                System.out.println("  3. potter");
                System.out.print("Выбор: ");
                String apiChoice = scanner.nextLine().trim();
                String source;
                switch (apiChoice) {
                    case "1": source = "jira"; break;
                    case "2": source = "police"; break;
                    case "3": source = "potter"; break;
                    default:
                        System.out.println("Ошибка: неверный выбор API.");
                        return;
                }
                try {
                    FILE_VIEWER.showByApi(source, format);
                } catch (Exception e) {
                    System.out.println("Ошибка чтения файла: " + e.getMessage());
                }
                break;
            default:
                System.out.println("Ошибка: выберите 1 или 2.");
        }
    }
}
