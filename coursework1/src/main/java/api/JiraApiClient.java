package api;

import java.io.IOException;

public final class JiraApiClient extends ApiClient{
    @Override
    public String extract() throws IOException, InterruptedException  {
        String query = "https://issues.apache.org/jira/rest/api/2/search?jql=project=Accumulo&expand=changelog&startAt=0&maxResults=2";
        return connector.getBody(query);
    }
}
