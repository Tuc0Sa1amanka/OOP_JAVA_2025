package transform;

import transform.jiradata.JiraResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;

public final class JiraTransform implements Transform {
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    @Override
    public  JiraResponse transform(String json) throws JsonProcessingException {
        return MAPPER.readValue(json, JiraResponse.class);
    }
}
