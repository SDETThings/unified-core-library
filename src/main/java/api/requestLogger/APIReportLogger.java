package api.requestLogger;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.google.gson.*;
import io.restassured.http.Method;
import io.restassured.response.Response;

import java.util.Map;

public class APIReportLogger {
    private static final APIReportLogger INSTANCE = new APIReportLogger();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private APIReportLogger() {}

    public static APIReportLogger getInstance() {
        return INSTANCE;
    }

    public void logStartOfEndpointExecutionIntoExtentReport(ExtentTest test,String endpointName) {
        /* test.log(Status.INFO, "<mark style='background-color: #CCCC00; color: black;'><strong>Execution started for endpoint  : "+ endpointName +"</strong></mark>");*/
        test.log(Status.INFO, "<div style='margin: 5px 0;'>" + "<button style='background-color: #4CAF50; color: white; border: none; cursor: pointer; " + "padding: 5px; border-radius: 3px;' " + "onclick='toggleVisibility(this);'>+ Execution started for endpoint: " + endpointName + "</button>" + "<div style='display: none; padding: 10px; border: 1px solid #ccc; " + "border-radius: 3px; background-color: #2E2E2E; margin-top: 5px;'>" + "<mark style='background-color: #CCCC00; color: black;'><strong>Execution started for endpoint:</strong> " + endpointName + "</mark>" + "</div></div>" + getJavaScriptFunction());
    }

    public void logApiCallPassOrFailMessageIntoExtentReport(ExtentTest test,Status status) {
        if(status.equals(Status.PASS)) {
            String successMessage = "<div style=\"background-color:#DFF2BF; color:#4F8A10; padding: 8px; " + "border-left: 3px solid #4F8A10; border-radius: 3px; font-weight: bold;\">" + "<span style=\"font-size: 14px;\">✔ Success:</span> Api request completed successfully! All validations passed." + "</div>";
            test.log(Status.PASS, successMessage);
        }else{
            String failureMessage = "<div style=\"background-color:#FFBABA; color:#D8000C; padding: 8px; " + "border-left: 3px solid #D8000C; border-radius: 3px; font-weight: bold;\"><span style=\"font-size: 14px;\">✖ Failure:</span> Api request failed! Validations did not pass.</div>";
            test.log(Status.FAIL, failureMessage);
        }
    }

    public void logApiCallDetailsInExtentReport(ExtentTest test,Status status, Method methodType, JsonObject payloadBody, String completeUrl, Map<String, String> headers, Response response) {
        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
        if (payloadBody != null) {
            test.log(status, "<div style='margin: 5px 0;'>" + "<button style='background-color: #4CAF50; color: white; border: none; cursor: pointer; " + "padding: 5px; border-radius: 3px;' " + "onclick='toggleVisibility(this);'>+ " + "Request details" + "</button>" + "<div style='display: none; padding: 10px; border: 1px solid #ccc; " + "border-radius: 3px; background-color: #2E2E2E; margin-top: 5px;'>" + "<b style='color: #FFD700;'>URI (" + methodType + "):</b> <span style='color:#00FF00;'>" + completeUrl + "</span><br>" // Bright green for URI
                    + "<b style='color: #FFD700;'>Headers:</b><br>" + "<pre style='margin: 0; color: #FFFFFF;'>" + prettyGson.toJson(gson.toJsonTree(headers).getAsJsonObject()) + "</pre><br>" // White for headers
                    + "<b style='color: #FFD700;'>StatusCode:</b> <span style='color:#FFD700;'>" + response.getStatusCode() + "</span><br>" // Gold for StatusCode
                    + "<b style='color: #FFD700;'>Request payload:</b><br>" + "<pre style='margin: 0; color: #FFFFFF;'>" + gson.toJson(payloadBody) + "</pre>" + "<b style='color: #FFD700;'>Response payload:</b><br>" + "<pre style='margin: 0; color: #FFFFFF;'>" + gson.toJson(JsonParser.parseString(response.getBody().prettyPrint())) + "</pre><br>" + "</div></div>" + getJavaScriptFunction());

        } else {
            test.log(status, "<div style='margin: 5px 0;'>" + "<button style='background-color: #4CAF50; color: white; border: none; cursor: pointer; " + "padding: 5px; border-radius: 3px;' " + "onclick='toggleVisibility(this);'>+ " + "Request details" + "</button>" + "<div style='display: none; padding: 10px; border: 1px solid #ccc; " + "border-radius: 3px; background-color: #2E2E2E; margin-top: 5px;'>" + "<b style='color: #FFD700;'>URI (" + methodType + "):</b> <span style='color:#00FF00;'>" + completeUrl + "</span><br>" // Bright green for URI
                    + "<b style='color: #FFD700;'>Headers:</b><br>" + "<pre style='margin: 0; color: #FFFFFF;'>" + prettyGson.toJson(gson.toJsonTree(headers).getAsJsonObject()) + "</pre><br>" // White for headers
                    + "<b style='color: #FFD700;'>StatusCode:</b> <span style='color:#FFD700;'>" + response.getStatusCode() + "</span><br>" // Gold for StatusCode
                    + "<b style='color: #FFD700;'>Request payload:</b><br>" + "<pre style='margin: 0; color: #FFFFFF;'>" + "{<br>" + "Payload is not present for this request <br>}<br>" + "</pre>" + "<b style='color: #FFD700;'>Response payload:</b><br>" + "<pre style='margin: 0; color: #FFFFFF;'>" + gson.toJson(JsonParser.parseString(response.getBody().prettyPrint())) + "</pre><br>" + "</div></div>" + getJavaScriptFunction());
        }
    }

    public void logResponseValidationResultsToExtentReport(ExtentTest test,Status status, Response actualResponse, JsonObject expectedResponse) {
   /*     ReportListeners.extentTest.get().log(status,
                "<b>Comparison of actual vs expected response:</b><br>" +
                        "<pre style='background-color:#2E2E2E; color:#FFD700; border-left: 3px solid #FFD700; padding: 10px;'>"   // Dark background, gold text for comparison
                        +"----------------------------------------------------------------------------<br>"
                        +"\t\t\t\t ACTUAL<br>"
                        +"----------------------------------------------------------------------------<br>"
                        +gson.toJson(JsonParser.parseString(actualResponse.getBody().prettyPrint())) + "<br><br>"
                        +"----------------------------------------------------------------------------<br>"
                        +"\t\t\t\t EXPECTED<br>"
                        +"----------------------------------------------------------------------------<br>"
                        +gson.toJson(expectedResponse) + "</pre>");*/
        test.log(status, "<div style='margin: 5px 0;'>" + "<button style='background-color: #4CAF50; color: white; border: none; cursor: pointer; " + "padding: 5px; border-radius: 3px;' " + "onclick='toggleVisibility(this);'>+ Response validations</button>" + "<div style='display: none; padding: 10px; border: 1px solid #ccc; " + "border-radius: 3px; background-color: #2E2E2E; margin-top: 5px;'>" + "<b style='color: #FFD700;'>ACTUAL:</b><br>" + "<pre style='margin: 0; color: #FFFFFF;'>" + gson.toJson(JsonParser.parseString(actualResponse.getBody().prettyPrint())) + "</pre><br>" + "<hr style='border: 1px solid #444;'>" + "<b style='color: #FFD700;'>EXPECTED:</b><br>" + "<pre style='margin: 0; color: #FFFFFF;'>" + gson.toJson(expectedResponse) + "</pre>" + "</div></div>" + getJavaScriptFunction());

    }

    public void logMismatchedValidationResultsToExtentReport(ExtentTest test,Status status, Map<String, JsonElement> mismatchedValues) {
        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
        /*ReportListeners.extentTest.get().log(status,
                "<b>Found :</b>" + mismatchedValues.size()+"<b> mistches in comparison of actual and expected values</b><br>"
                        +"<pre style='background-color:#2E2E2E; color:#FF0000; border-left: 3px solid #FF0000; padding: 10px;'>"   // Dark background, gold text for comparison
                        +prettyGson.toJson(gson.toJsonTree(mismatchedValues).getAsJsonObject()) + "</pre>");*/
        test.log(status, "<div style='margin: 5px 0;'>" + "<button style='background-color: #FF5733; color: white; border: none; cursor: pointer; " + "padding: 5px; border-radius: 3px;' " + "onclick='toggleVisibility(this);'>+ Found: " + mismatchedValues.size() + " mismatches in comparison of actual and expected values</button>" + "<div style='display: none; padding: 10px; border: 1px solid #ccc; " + "border-radius: 3px; background-color: #2E2E2E; margin-top: 5px;'>" + "<b style='color: #FFD700;'>Found:</b> " + mismatchedValues.size() + " <b style='color: #FFD700;'>mismatches in comparison of actual and expected values:</b><br>" + "<pre style='background-color:#2E2E2E; color:#FF0000; border-left: 3px solid #FF0000; padding: 10px;'>" // Dark background, red text for mismatched values
                + prettyGson.toJson(gson.toJsonTree(mismatchedValues).getAsJsonObject()) + "</pre>" + "</div></div>" + getJavaScriptFunction());

    }
    public static String getJavaScriptFunction() {
        return "<script>" + "function toggleVisibility(button) {" + "  var detailsDiv = button.nextElementSibling;" + "  if (detailsDiv.style.display === 'none') {" + "    detailsDiv.style.display = 'block';" + "    button.innerHTML = '- ' + button.innerHTML.substring(2);" + "  } else {" + "    detailsDiv.style.display = 'none';" + "    button.innerHTML = '+ ' + button.innerHTML.substring(2);" + "  }" + "}" + "</script>";
    }
}
