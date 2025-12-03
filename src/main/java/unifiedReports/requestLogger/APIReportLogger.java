package unifiedReports.requestLogger;

import com.aventstack.chaintest.plugins.ChainTestListener;
import com.google.gson.*;
import java.util.Map;

public class APIReportLogger {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final APIReportLogger INSTANCE = new APIReportLogger();

    private APIReportLogger() {
    }
    public static APIReportLogger getInstance() {
        return INSTANCE;
    }

    /**
     * Logs full API request and response cycle.
     */
    public void logApiDetails(String method, String url, String requestBody, int statusCode, String responseBody,
                              boolean isPassed, long duration) {
        ChainTestListener.log("********************************************************");
        String stepTitle = "{"+method +"} - " + url + "(" +duration+"ms)";
        ChainTestListener.log(stepTitle);
        if (requestBody != null && !requestBody.isEmpty()) {
            String prettyReq = prettyJson(requestBody);
            ChainTestListener.log("Request Body:\n" + prettyReq);
        }else{
            ChainTestListener.log("Request Body: <EMPTY>");
        }
        ChainTestListener.log("Response Status Code:" + statusCode);
        if (responseBody != null && !responseBody.isEmpty()) {
            String prettyRes = prettyJson(responseBody);
            ChainTestListener.log("Response Body: \n" + prettyRes);
        }
    }

    /**
     * Logs API validation results (mismatched JSON fields and values).
     * Pass a map of field → "expected vs actual" description.
     */
    public void logValidationResults(Map<String, JsonElement> mismatches) {
        if (mismatches.isEmpty()) {
            ChainTestListener.log("✅ API Validation Passed: All fields match expected values");
        }else{
            StringBuilder sb = new StringBuilder();
            mismatches.forEach((field, detail) ->
                    sb.append("• ").append(field).append(" → ").append(detail).append("\n"));
            //ChainTestListener.embed(sb.toString().getBytes(StandardCharsets.UTF_8),  "❌ API Validation Failed:\n\n");
            ChainTestListener.log("❌ API Validation Failed:\n\n"+sb);
        }
    }

    /**
     * Pretty-print JSON safely using Gson.
     */
    private String prettyJson(String rawJson) {
        try {
            JsonElement je = JsonParser.parseString(rawJson);
            return GSON.toJson(je);
        } catch (Exception e) {
            return rawJson; // fallback if invalid JSON
        }
    }

    /**
     * Simple text/info log.
     */
    public void logInfo(String message) {
        ChainTestListener.log(message);
    }
}
