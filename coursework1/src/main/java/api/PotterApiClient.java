package api;

import java.io.IOException;

public final class PotterApiClient extends ApiClient{
    @Override
    public String extract() throws IOException, InterruptedException {
        String query = "https://potterapi-fedeperin.vercel.app/en/characters/random";
        return connector.getBody(query);
    }
}
