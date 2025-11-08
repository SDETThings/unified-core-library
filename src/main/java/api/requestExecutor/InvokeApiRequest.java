package api.requestExecutor;

import api.requestLogger.ApiLoggerFactory;
import api.requestLogger.LoggingContext;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public class InvokeApiRequest {
    /**
     * Executes an API request with full flexibility and logs request/response details.
     *
     * @param method          HTTP method (GET, POST, etc.)
     * @param contentType     Content-Type header
     * @param completeUrl     Full endpoint URL
     * @param defaultHeaders  Map of headers
     * @param formParams      Map of form parameters
     * @param requestPayload  Raw request body (String/JSON/XML)
     * @param queryParams     Map of query parameters
     * @param pathParams      Map of path parameters
     * @param sslConfig       Optional SSL configuration
     * @return                Rest Assured Response
     */
    public Response requestAsync(Method method , String contentType , String completeUrl , Map<String,String> defaultHeaders, Map<String ,String> formParams, String requestPayload, Map<String ,String> queryParams, Map<String ,String> pathParams, SSLConfig sslConfig) {
        long start = System.currentTimeMillis();
        long duration = System.currentTimeMillis() - start;
        ExtentTest test = LoggingContext.getExtentTest().createNode("<span style='font-weight: bold; color: #00FFFF;'>" + completeUrl + "</span>");;

        RequestSpecification requestSpecification = RestAssured.given();
        //requestSpecification.filter(new TestListener());
        if(contentType!=null)
        {
            requestSpecification.contentType(contentType);
        }
        if(requestPayload!=null)
        {
            requestSpecification.body(requestPayload);
        }
        if(defaultHeaders!=null)
        {
            requestSpecification.headers(defaultHeaders);
        }
        if(formParams!=null)
        {
            requestSpecification.formParams(formParams);
        }
        if(queryParams!=null)
        {
            requestSpecification.queryParams(queryParams);
        }
        if(pathParams!=null)
        {
            requestSpecification.pathParams(pathParams);
        }
        if(sslConfig!=null)
        {
            requestSpecification.config(RestAssured.config().sslConfig(sslConfig));
        }
        // Log request details before sending
        ApiLoggerFactory.getReportLogger().logStartOfEndpointExecutionIntoExtentReport(test,completeUrl);
        ApiLoggerFactory.getConsoleLogger().logRequest(method, completeUrl, defaultHeaders, requestPayload, queryParams, pathParams);
        // Making the API call
        Response response =requestSpecification.request(method,completeUrl);
        // Log request details after sending
        ApiLoggerFactory.getConsoleLogger().logResponse(response,duration);
        if(response.getStatusLine().equalsIgnoreCase("OK") || response.getStatusLine().equalsIgnoreCase("Success")) {
            ApiLoggerFactory.getReportLogger().logApiCallDetailsInExtentReport(test,Status.PASS, method, JsonParser.parseString(requestPayload).getAsJsonObject(), completeUrl, defaultHeaders, response);
            ApiLoggerFactory.getReportLogger().logApiCallPassOrFailMessageIntoExtentReport(test,Status.PASS);
        }else{
            ApiLoggerFactory.getReportLogger().logApiCallDetailsInExtentReport(test,Status.FAIL, method, JsonParser.parseString(requestPayload).getAsJsonObject(), completeUrl, defaultHeaders, response);
            ApiLoggerFactory.getReportLogger().logApiCallPassOrFailMessageIntoExtentReport(test,Status.FAIL);
        }
        return response;
    }
}
