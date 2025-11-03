package api.RequestBuilder.headerConstruction;

import api.RequestBuilder.authentication.SecretsAndCertificates;
import io.restassured.http.Header;
import io.restassured.http.Headers;

import java.util.*;
import java.util.stream.Collectors;

public class HeaderBuilder {
    private final Map<String, String> headers;

    // Private constructor to force use of builder pattern
    private HeaderBuilder() {
        headers = new HashMap<>();
    }

    // Factory method
    public static HeaderBuilder newBuilder() {
        return new HeaderBuilder();
    }

    // Default headers
    public HeaderBuilder withDefaultHeaders() {
        headers.putIfAbsent("Content-Type", "application/json");
        headers.putIfAbsent("Accept", "application/json");
        return this;
    }

    // Add Bearer Token
    public HeaderBuilder withBearerToken() {
        headers.put("Authorization", "Bearer " + SecretsAndCertificates.newBuilder().getBearerTokenWithAzureDefaultCredentials("",""));
        return this;
    }

    // Add API key
    public HeaderBuilder withApiKey(String keyName, String keyValue) {
        if (keyName != null && keyValue != null) {
            headers.put(keyName, keyValue);
        }
        return this;
    }

    // Add custom headers
    public HeaderBuilder addHeader(String name, String value) {
        if (name != null && value != null) {
            headers.put(name, value);
        }
        return this;
    }

    // Add multiple headers
    public HeaderBuilder addHeaders(Map<String, String> customHeaders) {
        if (customHeaders != null) {
            headers.putAll(customHeaders);
        }
        return this;
    }

    // Return as Map
    public Map<String, String> buildAsMap() {
        return new HashMap<>(headers);
    }

    // Return as Rest Assured Headers object
    public Headers buildAsRestAssuredHeaders() {
        List<Header> headerList = headers.entrySet()
                .stream()
                .map(e -> new Header(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        return new Headers(headerList);
    }
}
