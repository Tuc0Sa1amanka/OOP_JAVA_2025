package api;

import transform.Transform;

public record ApiPair(ApiClient client, Transform transform) {}