package api;

import connector.Connector;

import java.io.IOException;

public abstract class ApiClient {
    Connector connector = new Connector();
    public abstract String extract() throws IOException, InterruptedException;
}
