package save;

import java.io.File;
import java.util.Map;
import java.util.List;
import java.time.Instant;
import java.util.ArrayList;
import java.io.IOException;
import java.util.LinkedHashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public final class JsonSave {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String FILE_PATH = "data.json";
    public void save(Record data, String apiName, boolean append) throws IOException {
        int idCounter;
        List<Map<String, Object>> result;
        File file = new File(FILE_PATH);
        if (append && file.exists()) {
            result = mapper.readValue(file, new TypeReference<>() {});
            idCounter = result.stream()
                    .mapToInt(m -> (int) m.get("id"))
                    .max()
                    .orElse(0) + 1;
        } else {
            result = new ArrayList<>();
            idCounter = 1;
        }
        Map<String, Object> record = new LinkedHashMap<>();
        record.put("id", idCounter);
        record.put("source", apiName);
        record.put("timestamp", Instant.now().toString());
        record.put("data", data);
        result.add(record);
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, result);
    }
}
