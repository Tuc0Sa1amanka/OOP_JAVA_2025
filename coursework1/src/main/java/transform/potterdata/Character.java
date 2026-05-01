package transform.potterdata;

import java.util.Map;
import java.util.List;
import java.time.Instant;
import java.util.ArrayList;
import save.CsvConvertible;
import java.util.LinkedHashMap;

public record Character(
        String fullName,
        String nickname,
        String hogwartsHouse,
        String interpretedBy,
        List<String> children,
        String image,
        String birthdate,
        String index
) implements CsvConvertible {
    @Override
    public List<Map<String, String>> toRows(int id, String apiName) {
        List<Map<String, String>> rows = new ArrayList<>();
        if (children() != null && !children().isEmpty()) {
            for (String child : children()) {
                Map<String, String> row = new LinkedHashMap<>();
                row.put("id", String.valueOf(id));
                row.put("source", apiName);
                row.put("timestamp", Instant.now().toString());
                row.put("data.fullName", cleanValue(fullName()));
                row.put("data.nickname", cleanValue(nickname()));
                row.put("data.hogwartsHouse", cleanValue(hogwartsHouse()));
                row.put("data.interpretedBy", cleanValue(interpretedBy()));
                row.put("data.children", cleanValue(child));
                row.put("data.image", cleanValue(image()));
                row.put("data.birthdate", cleanValue(birthdate()));
                row.put("data.index", cleanValue(index()));
                rows.add(row);
            }
        } else {
            Map<String, String> row = new LinkedHashMap<>();
            row.put("id", String.valueOf(id));
            row.put("source", apiName);
            row.put("timestamp", Instant.now().toString());
            row.put("data.fullName", cleanValue(fullName()));
            row.put("data.nickname", cleanValue(nickname()));
            row.put("data.hogwartsHouse", cleanValue(hogwartsHouse()));
            row.put("data.interpretedBy", cleanValue(interpretedBy()));
            row.put("data.children", "");
            row.put("data.image", cleanValue(image()));
            row.put("data.birthdate", cleanValue(birthdate()));
            row.put("data.index", cleanValue(index()));
            rows.add(row);
        }
        return rows;
    }
    private String cleanValue(String value) {
        return value == null ? "" : value.replace(",", " ");
    }
}
