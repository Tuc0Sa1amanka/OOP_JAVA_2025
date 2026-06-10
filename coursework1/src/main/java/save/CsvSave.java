package save;

import java.io.*;
import java.util.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

public final class CsvSave {
    private final String FILE_PATH;
    private boolean append;
    public CsvSave(boolean append) {
        FILE_PATH = "data.csv";
        this.append = append;
    }
    public synchronized void save(CsvConvertible data, String apiName) throws IOException, CsvValidationException {
        List<Map<String, String>> allRows = new ArrayList<>();
        Set<String> allHeaders = new LinkedHashSet<>(List.of("id", "source", "timestamp"));
        File file = new File(FILE_PATH);
        if (append && file.exists()) {
            try (CSVReader reader = new CSVReader(new FileReader(file))) {
                String[] headers = reader.readNext();
                allHeaders.addAll(Arrays.asList(headers));
                String[] row;
                while ((row = reader.readNext()) != null) {
                    Map<String, String> rowMap = new LinkedHashMap<>();
                    for (int i = 0; i < row.length; i++) {
                        rowMap.put(headers[i], row[i]);
                    }
                    allRows.add(rowMap);
                }
            }
        }
        int nextId = allRows.isEmpty() ? 1 : allRows.stream()
                .mapToInt(r -> Integer.parseInt(r.getOrDefault("id", "0"))
                ).max().getAsInt() + 1;
        List<Map<String, String>> newRows = data.toRows(nextId, apiName);
        allHeaders.addAll(newRows.getFirst().keySet());
        allRows.addAll(newRows);
        try (CSVWriter writer = new CSVWriter(
                new FileWriter(FILE_PATH, false),
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {
            writer.writeNext(allHeaders.toArray(new String[0]));
            for (Map<String, String> row : allRows) {
                String[] values = new String[allHeaders.size()];
                int i = 0;
                for (String header : allHeaders) {
                    values[i++] = row.get(header);
                }
                writer.writeNext(values);
            }
        }
        append = true;
    }
}