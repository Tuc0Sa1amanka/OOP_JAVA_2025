package save;

import java.util.Map;
import java.util.List;

public interface CsvConvertible {
    List<Map<String, String>> toRows(int id, String apiName);
}