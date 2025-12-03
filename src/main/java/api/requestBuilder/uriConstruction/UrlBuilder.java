package api.requestBuilder.uriConstruction;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlBuilder {

    /**
     * Creates the request URL
     * @param baseUrl - base URL
     * @param apiVersion - api version
     * @param endpoint - endpoint ( includes everything apart from the base url and api version )
     * @param queryParam - query parameters in form of a map
     *
     * @param pathParam - path parameters in form of a map
     * @return complete modified request url
     */
    public String buildRequestUrl(String baseUrl, String apiVersion, String endpoint, Map<String, String> queryParam, Map<String, String> pathParam) {
        // Immutable copies for thread safety
        Map<String, String> safePathParam = pathParam != null ? Map.copyOf(pathParam) : Map.of();
        Map<String, String> safeQueryParam = queryParam != null ? Map.copyOf(queryParam) : Map.of();
        String apiVersionString = (apiVersion != null && !apiVersion.isEmpty()) ? apiVersion : "";
        String url = baseUrl + apiVersionString + endpoint;
        if (safePathParam != null) {
            for (Map.Entry<String, String> entry : safePathParam.entrySet()) {
                String key = entry.getKey();
                String replacementValue = entry.getValue();
                url = url.replace("{" + key + "}", replacementValue);
            }
        }
        if (safeQueryParam != null ) {
            // Regex to match ?key={} or &key={}
            Pattern pattern = Pattern.compile("([?&])(\\w+)=\\{}");
            Matcher matcher = pattern.matcher(url);
            StringBuffer result = new StringBuffer();

            while (matcher.find()) {
                String key = matcher.group(2);
                String replacementValue = safeQueryParam.getOrDefault(key, "");
                matcher.appendReplacement(result, matcher.group(1) + key + "=" + replacementValue);
            }
            matcher.appendTail(result);
            url = result.toString();
        }
        return url;
    }
}

