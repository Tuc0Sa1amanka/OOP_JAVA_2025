package api;

import java.io.IOException;

public final class PoliceApiClient extends ApiClient{
    @Override
    public String extract() throws IOException, InterruptedException {
        String query = "https://data.police.uk/api/forces/leicestershire";
        return connector.getBody(query);
    }
}
