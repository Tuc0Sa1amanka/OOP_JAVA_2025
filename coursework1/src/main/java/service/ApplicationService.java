package service;

import save.CsvSave;
import save.JsonSave;
import api.ApiClient;
import transform.Transform;
import save.CsvConvertible;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;

public class ApplicationService {
    private static final JsonSave JSON_SAVE = new JsonSave();
    private static final CsvSave CSV_SAVE = new CsvSave();
    public static void processApi(ApiClient c, Transform t, String apiName, String format, boolean append) throws IOException, InterruptedException, CsvValidationException {
        String json = c.extract();
        Record data = t.transform(json);
        if (format.equalsIgnoreCase("json")) {
            JSON_SAVE.save(data, apiName, append);
        } else if (format.equalsIgnoreCase("csv")) {
            CSV_SAVE.save((CsvConvertible) data, apiName, append);
        }
    }
}
