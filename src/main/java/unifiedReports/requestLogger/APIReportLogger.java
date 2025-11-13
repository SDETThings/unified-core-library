package unifiedReports.requestLogger;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.google.gson.*;
import io.qameta.allure.Allure;
import io.restassured.http.Method;
import io.restassured.response.Response;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
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

        String statusLabel = isPassed ? "✅ PASSED" : "❌ FAILED";
        String stepTitle = String.format("%s | %s %s (%d ms)", statusLabel, method, url, duration);

        Allure.step(stepTitle, () -> {
            if (requestBody != null && !requestBody.isEmpty()) {
                String prettyReq = prettyJson(requestBody);
                Allure.addAttachment("Request Body", "application/json", prettyReq);
            }

            Allure.addAttachment("Response Status Code", "text/plain", String.valueOf(statusCode));

            if (responseBody != null && !responseBody.isEmpty()) {
                String prettyRes = prettyJson(responseBody);
                Allure.addAttachment("Response Body", "application/json", prettyRes);
            }
        });
    }

    /**
     * Logs API validation results (mismatched JSON fields and values).
     * Pass a map of field → "expected vs actual" description.
     */
    public void logValidationResults(Map<String, String> mismatches) {
        if (mismatches == null || mismatches.isEmpty()) {
            Allure.step("✅ API Validation Passed: All fields match expected values");
            return;
        }

        StringBuilder sb = new StringBuilder("❌ API Validation Failed:\n\n");
        mismatches.forEach((field, detail) ->
                sb.append("• ").append(field).append(" → ").append(detail).append("\n"));

        Allure.addAttachment("Validation Results", "text/plain",
                new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8)), ".txt");
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
        Allure.step(message);
    }
}
