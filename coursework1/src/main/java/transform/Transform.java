package transform;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface Transform {
    Record transform(String json) throws JsonProcessingException;
}
