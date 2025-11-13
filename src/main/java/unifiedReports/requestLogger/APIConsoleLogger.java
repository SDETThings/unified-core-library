package unifiedReports.requestLogger;

import io.restassured.http.Method;
import io.restassured.response.Response;

import java.util.Map;
import java.util.logging.Logger;

public final class APIConsoleLogger {
    // Thread-safe singleton instance
    private static final APIConsoleLogger INSTANCE = new APIConsoleLogger();
    private static final Logger logger = Logger.getLogger(APIConsoleLogger.class.getName());
    private APIConsoleLogger(){
    }
    public static APIConsoleLogger getInstance(){
        return INSTANCE;
    }
    /**
     * Logs all details of an outgoing API request.
     */
    public void logRequest(Method method, String url, Map<String, String> headers, String body, Map<String, String> queryParams, Map<String, String> pathParams) {
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

        sb.append("=========================================================\n");
        synchronized (logger) {
            logger.info(sb.toString());
        }
    }
    /**
     * Logs all details of the received API response.
     */
    public void logResponse(Response response, long durationMs) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n====================== API RESPONSE =====================\n");
        sb.append("Status Code   : ").append(response.getStatusCode()).append("\n");
        sb.append("Response Time : ").append(durationMs).append(" ms\n");

        if (response.getBody() != null) {
            sb.append("Response Body : ").append(response.getBody().asPrettyString()).append("\n");
        } else {
            sb.append("Response Body : <EMPTY>\n");
        }

        sb.append("=========================================================\n");

        // Thread-safe logging
        synchronized (logger) {
            logger.info(sb.toString());
        }
    }



}
