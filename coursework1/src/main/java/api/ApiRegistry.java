package api;

import java.util.Map;
import java.util.HashMap;
import transform.JiraTransform;
import transform.PoliceTransform;
import transform.PotterTransform;


public final class ApiRegistry {
    private final Map<String, ApiPair> apiPairMap;
    public ApiRegistry() {
        apiPairMap = new HashMap<>();
        apiPairMap.put("jira", new ApiPair(new JiraApiClient(), new JiraTransform()));
        apiPairMap.put("potter", new ApiPair(new PotterApiClient(), new PotterTransform()));
        apiPairMap.put("police", new ApiPair(new PoliceApiClient(), new PoliceTransform()));
    }
    public ApiPair getPair(String apiName) {
        return apiPairMap.get(apiName);
    }
}
