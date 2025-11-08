package api.requestBuilder.requestMethodTypes;

public enum APIMethodType {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE");

    private final String method;

    APIMethodType(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
