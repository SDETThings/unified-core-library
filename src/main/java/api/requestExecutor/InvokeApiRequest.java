package api.requestExecutor;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import unifiedReports.LoggerFactory;

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
    @Step("Execute API Request: {method} - {completeUrl}")
    public Response requestAsync(Method method , String contentType , String completeUrl , Map<String,String> defaultHeaders, Map<String ,String> formParams, String requestPayload, Map<String ,String> queryParams, Map<String ,String> pathParams, SSLConfig sslConfig) {
        long start = System.currentTimeMillis();
        long duration = System.currentTimeMillis() - start;

        RequestSpecification requestSpecification = RestAssured.given();
        if (contentType != null) requestSpecification.contentType(contentType);
        if (requestPayload != null) requestSpecification.body(requestPayload);
        if (defaultHeaders != null) requestSpecification.headers(defaultHeaders);
        if (formParams != null) requestSpecification.formParams(formParams);
        if (queryParams != null) requestSpecification.queryParams(queryParams);
        if (pathParams != null) requestSpecification.pathParams(pathParams);
        if (sslConfig != null) requestSpecification.config(RestAssured.config().sslConfig(sslConfig));

        Response response =requestSpecification.request(method,completeUrl);
        // log to console
        LoggerFactory.getApiConsoleLogger().logRequest(method, completeUrl, defaultHeaders, requestPayload, queryParams, pathParams);

        // log to Allure report
        if(response.getStatusLine().equalsIgnoreCase("OK") || response.getStatusLine().equalsIgnoreCase("Success")) {
            if(requestPayload==null){
                LoggerFactory.getApiReportLogger().logApiDetails(method.name(), completeUrl, null, response.getStatusCode(), response.getBody().asString(),true, duration);
            }else{
                LoggerFactory.getApiReportLogger().logApiDetails(method.name(), completeUrl, requestPayload, response.getStatusCode(), response.getBody().asString(),true, duration);
            }
        }else{
            if(requestPayload==null){
                LoggerFactory.getApiReportLogger().logApiDetails(method.name(), completeUrl, null, response.getStatusCode(), response.getBody().asString(),false, duration);
            }else{
                LoggerFactory.getApiReportLogger().logApiDetails(method.name(), completeUrl, requestPayload, response.getStatusCode(), response.getBody().asString(),false, duration);
            }
        }
        return response;
    }
}
