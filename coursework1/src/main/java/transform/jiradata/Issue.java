package transform.jiradata;

public record Issue(
        String id,
        String key,
        Fields fields
) {}
