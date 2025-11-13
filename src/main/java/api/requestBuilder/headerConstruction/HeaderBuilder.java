/**
 * The HeaderBuilder class is a utility for constructing headers in a flexible and reusable way.
 * It supports adding default headers, bearer tokens, API keys, and custom headers, and can return
 * the headers as either a Map or a Rest Assured Headers object.
 */
package api.requestBuilder.headerConstruction;

import api.requestBuilder.authentication.SecretsAndCertificates;
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

    /**
     * Factory method to create a new instance of HeaderBuilder.
     * @return a new HeaderBuilder instance.
     */
    public static HeaderBuilder newBuilder() {
        return new HeaderBuilder();
    }

    /**
     * Adds default headers to the builder. Default headers include:
     * - Content-Type: application/json
     * - Accept: application/json
     *
     * @return the current HeaderBuilder instance for method chaining.
     */
    public HeaderBuilder withDefaultHeaders() {
        headers.putIfAbsent("Content-Type", "application/json");
        headers.putIfAbsent("Accept", "application/json");
        return this;
    }
    /**
     * Adds an Authorization header with a Bearer token. The token is retrieved
     * using the SecretsAndCertificates utility.
     *
     * @return the current HeaderBuilder instance for method chaining.
     */
    public HeaderBuilder withBearerToken() {
        headers.put("Authorization", "Bearer " + SecretsAndCertificates.newBuilder().getBearerTokenWithAzureDefaultCredentials("",""));
        return this;
    }
    /**
     * Adds an API key to the headers.
     *
     * @param keyName  the name of the API key header.
     * @param keyValue the value of the API key.
     * @return the current HeaderBuilder instance for method chaining.
     */
    public HeaderBuilder withApiKey(String keyName, String keyValue) {
        if (keyName != null && keyValue != null) {
            headers.put(keyName, keyValue);
        }
        return this;
    }
    /**
     * Adds a custom header to the builder.
     *
     * @param name  the name of the header.
     * @param value the value of the header.
     * @return the current HeaderBuilder instance for method chaining.
     */
    public HeaderBuilder addHeader(String name, String value) {
        if (name != null && value != null) {
            headers.put(name, value);
        }
        return this;
    }
    /**
     * Adds multiple custom headers to the builder.
     *
     * @param customHeaders a map of header names and values.
     * @return the current HeaderBuilder instance for method chaining.
     */
    public HeaderBuilder addHeaders(Map<String, String> customHeaders) {
        if (customHeaders != null) {
            headers.putAll(customHeaders);
        }
        return this;
    }
    /**
     * Builds the headers as a Map.
     *
     * @return a new Map containing the headers.
     */
    public Map<String, String> buildAsMap() {
        return new HashMap<>(headers);
    }

    /**
     * Builds the headers as a Rest Assured Headers object.
     *
     * @return a new Headers object containing the headers.
     */
    public Headers buildAsRestAssuredHeaders() {
        List<Header> headerList = headers.entrySet()
                .stream()
                .map(e -> new Header(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        return new Headers(headerList);
    }
}
