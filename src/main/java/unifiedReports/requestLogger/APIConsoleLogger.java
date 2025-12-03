package unifiedReports.requestLogger;

import com.google.gson.JsonElement;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Map;
import java.util.regex.Pattern;

public final class APIConsoleLogger {
    // Thread-safe singleton instance
    private static final APIConsoleLogger INSTANCE = new APIConsoleLogger();
    private static final Logger logger = LogManager.getLogger(APIConsoleLogger.class);
    private static final Pattern ANSI_PATTERN = Pattern.compile("\u001B\\[[;\\d]*m");     // actual ESC sequences
    private static final Pattern ESCAPED_UNICODE_PATTERN = Pattern.compile("\\\\u001B\\[[;\\d]*m"); // "\u001B[31m" or "\\u001B[31m"

    private static String stripAnsi(String text) {
        if (text == null) return null;
        // remove real ANSI sequences: ESC[...m
        String cleaned = ANSI_PATTERN.matcher(text).replaceAll("");
        // remove escaped unicode literal sequences: \u001B[...m or \\u001B[...m
        cleaned = ESCAPED_UNICODE_PATTERN.matcher(cleaned).replaceAll("");
        return cleaned;
    }
    private APIConsoleLogger(){
    }
    public static APIConsoleLogger getInstance(){
        return INSTANCE;
    }
    /**
     * Logs all details of an outgoing API request.
     */
    public void logRequest(Method method, String url, Map<String, String> headers, String body, Map<String, String> queryParams, Map<String, String> pathParams, Response response, long duration) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n====================== API REQUEST ======================\n");
        sb.append("HTTP Method   : ").append(method).append("\n");
        sb.append("Request URL   : ").append(url).append("\n");

        if (headers != null && !headers.isEmpty()) {
            sb.append("Headers       : ").append(headers).append("\n");
        }

        if (queryParams != null && !queryParams.isEmpty()) {
            sb.append("Query Params  : ").append(queryParams).append("\n");
        }

        if (pathParams != null && !pathParams.isEmpty()) {
            sb.append("Path Params   : ").append(pathParams).append("\n");
        }

        if (body != null && !body.isEmpty()) {
            sb.append("Request Body  : ").append(body).append("\n");
        } else {
            sb.append("Request Body  : <EMPTY>\n");
        }
        if(response!=null){
            sb.append("Status Code  : ").append(response.getStatusCode()).append("\n");
            sb.append("Response Time : ").append(duration).append(" ms\n");
            sb.append("Response Body  : ").append(response.getBody().asPrettyString()).append("\n");
        }else {
            sb.append("Response Time : ").append(duration).append(" ms\n");
            sb.append("Response Body : <EMPTY>\n");
        }

        synchronized (logger) {
            logger.info(stripAnsi(sb.toString()));
        }
    }
    public void logMessage(String message){
        StringBuilder sb = new StringBuilder();
        sb.append(message+"\n");
        // Thread-safe logging
        synchronized (logger) {
            logger.info(sb.toString());
        }
    }
    public void logValidationResults(Map<String, JsonElement> mismatches) {
        if (!mismatches.isEmpty()) {
            logMessage("❌ Mismatches: " + mismatches.size() + " mismatches found for response validation :\n" + mismatches);
        } else {
            logMessage("✅ No mismatches found for response validation.");
        }
    }
}
