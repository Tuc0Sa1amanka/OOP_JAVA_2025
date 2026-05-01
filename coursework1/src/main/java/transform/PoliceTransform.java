package transform;

import transform.policedata.PoliceForce;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public final class PoliceTransform implements Transform {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Override
    public PoliceForce transform(String json) throws JsonProcessingException {
        return MAPPER.readValue(json, PoliceForce.class);
    }
}
