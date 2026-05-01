package transform.policedata;

import java.util.Map;
import java.util.List;
import java.time.Instant;
import java.util.ArrayList;
import save.CsvConvertible;
import java.util.LinkedHashMap;

public record PoliceForce(
        String id,
        String name,
        String description,
        String url,
        String telephone,
        List<EngagementMethod> engagement_methods
) implements CsvConvertible {
    @Override
    public List<Map<String, String>> toRows(int id, String apiName) {
        List<Map<String, String>> rows = new ArrayList<>();
        if (engagement_methods() != null && !engagement_methods().isEmpty()) {
            for (EngagementMethod method : engagement_methods()) {
                Map<String, String> row = new LinkedHashMap<>();
                row.put("id", String.valueOf(id));
                row.put("source", apiName);
                row.put("timestamp", Instant.now().toString());
                row.put("data.id", cleanValue(this.id()));
                row.put("data.name", cleanValue(name()));
                row.put("data.description", cleanValue(description()));
                row.put("data.url", cleanValue(url()));
                row.put("data.telephone", cleanValue(telephone()));
                row.put("data.engagement_methods.type", cleanValue(method.type()));
                row.put("data.engagement_methods.title", cleanValue(method.title()));
                row.put("data.engagement_methods.url", cleanValue(method.url()));
                row.put("data.engagement_methods.description", cleanValue(method.description()));
                rows.add(row);
            }
        } else {
            Map<String, String> row = new LinkedHashMap<>();
            row.put("id", String.valueOf(id));
            row.put("source", apiName);
            row.put("timestamp", Instant.now().toString());
            row.put("data.id", cleanValue(this.id()));
            row.put("data.name", cleanValue(name()));
            row.put("data.description", cleanValue(description()));
            row.put("data.url", cleanValue(url()));
            row.put("data.telephone", cleanValue(telephone()));
            rows.add(row);
        }
        return rows;
    }
    private String cleanValue(String value) {
        return value == null ? "" : value.replace(",", " ");
    }
}
