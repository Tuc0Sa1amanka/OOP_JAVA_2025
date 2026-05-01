package org.example;

import api.ApiClient;
import api.JiraApiClient;
import api.PoliceApiClient;
import api.PotterApiClient;
import service.ApplicationService;
import com.opencsv.exceptions.CsvValidationException;
import transform.JiraTransform;
import transform.PoliceTransform;
import transform.PotterTransform;
import transform.Transform;
import viewer.FileViewer;

import java.io.IOException;
import java.net.ConnectException;
import java.net.http.HttpTimeoutException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final List<String> API_NAMES = List.of("jira", "police", "potter");
    private static final List<ApiClient> CLIENTS = List.of(new JiraApiClient(), new PoliceApiClient(), new PotterApiClient());
    private static final List<Transform> TRANSFORMS = List.of(new JiraTransform(), new PoliceTransform(), new PotterTransform());
    private static final FileViewer FILE_VIEWER = new FileViewer();

    public static void main(String[] args) {
        if (args.length == 0) {
            runInteractiveMode();
        } else if (args.length >= 4) {
            runAutoMode(args);
        } else {
            System.out.println("Использование");
            System.out.println("Интерактивный: без параметров командной строки");
            System.out.println("Автоматический: <api1> <api2> ... <format>");
            System.out.println("Пример: jira police potter json");
        }
    }
    private static void runAutoMode(String[] args) {
        String format = args[args.length - 1];
        if (!format.equals("json") && !format.equals("csv")) {
            System.out.println("Неверный формат: " + format);
            return;
        }
        for (int i = 0; i < args.length - 1; i++) {
            String apiName = args[i];
            int index = API_NAMES.indexOf(apiName);
            if (index == -1) {
                System.out.println("Неизвестный API: " + apiName);
                continue;
            }
            try {
                processAndSave(apiName, format, i > 0);
            } catch (ConnectException e) {
                System.out.println("Ошибка подключения");
            } catch (HttpTimeoutException e) {
                System.out.println("Сервер не отвечает: превышено время ожидания");
            } catch (InterruptedException e) {
                System.out.println("Операция была прервана");
            } catch (IOException e) {
                System.out.println("Ошибка ввода-вывода");
            } catch (CsvValidationException e) {
                System.out.println("Неккоретный csv");
            }
        }
    }
    private static void runInteractiveMode() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nВыберите API:");
        System.out.println("1. jira");
        System.out.println("2. police");
        System.out.println("3. potter");
        System.out.print("Ваш выбор (1-3): ");
        String apiChoice = scanner.nextLine();
        String apiName;
        switch (apiChoice) {
            case "1" -> apiName = "jira";
            case "2" -> apiName = "police";
            case "3" -> apiName = "potter";
            default -> {
                System.out.println("Неверный выбор");
                scanner.close();
                return;
            }
        }
        System.out.println("\nВыберите формат:");
        System.out.println("1. json");
        System.out.println("2. csv");
        System.out.print("Ваш выбор (1-2): ");
        String formatChoice = scanner.nextLine();
        String format;
        switch (formatChoice) {
            case "1":
                format = "json";
                break;
            case "2":
                format = "csv";
                break;
            default:
                System.out.println("Неверный выбор");
                scanner.close();
                return;
        }
        System.out.println("\nВыберите режим:");
        System.out.println("1. new");
        System.out.println("2. append");
        System.out.print("Ваш выбор (1-2): ");
        String appendChoice = scanner.nextLine();
        boolean append;
        switch (appendChoice) {
            case "1":
                append = false;
                break;
            case "2":
                append = true;
                break;
            default:
                System.out.println("Неверный выбор режима");
                scanner.close();
                return;
        }
        try {
            processAndSave(apiName, format, append);
        } catch (Exception e) {
            System.out.println("Ошибка");
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Сохранено в data." + format);
        System.out.print("\nПоказать содержимое файла? (y/n): ");
        String showChoice = scanner.nextLine();
        if (showChoice.equalsIgnoreCase("y")) {
            System.out.println("\n1. Весь файл");
            System.out.println("2. Только записи из конкретного API");
            System.out.print("Ваш выбор: ");
            String viewChoice = scanner.nextLine();
            switch (viewChoice) {
                case "1":
                    try {
                        FILE_VIEWER.showFull(format);
                    } catch (IOException e) {
                        System.out.println("ошибка чтения файла");
                        System.out.println(e.getMessage());
                        return;
                    }
                    break;
                case "2":
                    System.out.println("\nВыберите API:");
                    System.out.println("1. jira");
                    System.out.println("2. police");
                    System.out.println("3. potter");
                    System.out.print("Ваш выбор (1-3): ");
                    String sourceChoice = scanner.nextLine();
                    String source;
                    switch (sourceChoice) {
                        case "1" -> source = "jira";
                        case "2" -> source = "police";
                        case "3" -> source = "potter";
                        default -> {
                            System.out.println("Неверный выбор");
                            scanner.close();
                            return;
                        }
                    }
                    try {
                        FILE_VIEWER.showByApi(source, format);
                    } catch (Exception e) {
                        System.out.println("Ошибка чтения файла");
                        System.out.println(e.getMessage());
                        return;
                    }
                    break;
                default:
                    System.out.println("Неверный выбор");
            }
        }
        scanner.close();
    }
    private static void processAndSave(String apiName, String format, boolean append) throws IOException, InterruptedException, CsvValidationException {
        int index = API_NAMES.indexOf(apiName);
        ApiClient client = CLIENTS.get(index);
        Transform transform = TRANSFORMS.get(index);
        ApplicationService.processApi(client, transform, apiName, format, append);
    }
}
