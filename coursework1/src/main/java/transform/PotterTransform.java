package transform;

import transform.potterdata.Character;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public final class PotterTransform implements Transform {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Override
    public Character transform(String json) throws JsonProcessingException {
        return MAPPER.readValue(json, Character.class);
    }
}
