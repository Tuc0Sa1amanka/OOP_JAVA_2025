package transform.jiradata;

import java.util.Map;
import java.util.List;
import java.time.Instant;
import java.util.ArrayList;
import save.CsvConvertible;
import java.util.LinkedHashMap;

public record JiraResponse(
        String expand,
        int startAt,
        int maxResults,
        int total,
        List<Issue> issues
) implements CsvConvertible {
    @Override
    public List<Map<String, String>> toRows(int id, String apiName) {
        List<Map<String, String>> rows = new ArrayList<>();
        for (Issue issue : issues()) {
            Map<String, String> row = new LinkedHashMap<>();
            row.put("id", String.valueOf(id));
            row.put("source", apiName);
            row.put("timestamp", Instant.now().toString());
            row.put("data.expand", cleanValue(expand()));
            row.put("data.startAt", String.valueOf(startAt()));
            row.put("data.maxResults", String.valueOf(maxResults()));
            row.put("data.total", String.valueOf(total()));
            row.put("data.issues.id", cleanValue(issue.id()));
            row.put("data.issues.key", cleanValue(issue.key()));
            row.put("data.issues.fields.summary", cleanValue(issue.fields().summary()));
            row.put("data.issues.fields.created", cleanValue(issue.fields().created()));
            row.put("data.issues.fields.status.id", cleanValue(issue.fields().status().id()));
            row.put("data.issues.fields.status.name", cleanValue(issue.fields().status().name()));
            rows.add(row);
        }
        return rows;
    }
    private String cleanValue(String value) {
        return value == null ? "" : value.replace(",", " ");
    }
}