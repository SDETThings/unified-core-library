import api.requestBuilder.payloadCreation.PayloadBuilder;
import api.requestBuilder.uriConstruction.UrlBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.builder.ResponseBuilder;
import io.restassured.response.Response;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class FunctionTesting {
    public static void main(String[] args) throws IOException {

    }

    /**
     * Helper: mock Response with body
     */
    private static Response mockResponse(String jsonBody) {
        return new ResponseBuilder().setStatusCode(200).setBody(jsonBody).setContentType("application/json").build();
    }

    public void urlContructionTest() {
        Map<String, String> pathParam = new LinkedHashMap<>();
        pathParam.put("user", "kunal");
        pathParam.put("id", "123456");
        Map<String, String> queryParam = new LinkedHashMap<>();
        queryParam.put("status", "true");
        queryParam.put("limit", "100");
        UrlBuilder urlBuilder = new UrlBuilder();
        System.out.println(urlBuilder.buildRequestUrl("https://api.example.com/", "v2/", "users/{user}/{id}/orders?status={}&limit={}", queryParam, pathParam));

    }
}
