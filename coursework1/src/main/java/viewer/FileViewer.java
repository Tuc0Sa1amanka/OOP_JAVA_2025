package viewer;

import java.io.File;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class FileViewer {
    private static final String CSV_PATH = "data.csv";
    private static final String JSON_PATH = "data.json";
    private static final ObjectMapper mapper = new ObjectMapper();
    public void showFull(String format) throws IOException {
        String path = format.equals("csv") ? CSV_PATH : JSON_PATH;
        String content = Files.readString(Paths.get(path));
        System.out.println(content);
    }
    public void showByApi(String apiName, String format) throws IOException {
        if (format.equalsIgnoreCase("json")) {
            showByApiFromJson(apiName);
        } else {
            showByApiFromCsv(apiName);
        }
    }
    private void showByApiFromJson(String apiName) throws IOException {
        File file = new File(JSON_PATH);
        List<Map<String, Object>> records = mapper.readValue(
                file,
                new TypeReference<>() {}
        );
        List<Map<String, Object>> filtered = records.stream()
                .filter(r -> r.get("source").equals(apiName))
                .toList();
        if (filtered.isEmpty()) {
            System.out.println("Нет записей от API: " + apiName);
            return;
        }
        String output = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(filtered);
        System.out.println(output);
    }
    private void showByApiFromCsv(String apiName) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(CSV_PATH));
        System.out.println(lines.getFirst());
        boolean found = false;
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] columns = line.split(",", -1);
            if (columns.length > 1 && columns[1].equals(apiName)) {
                System.out.println(line);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Нет записей от API: " + apiName);
        }
    }
}
