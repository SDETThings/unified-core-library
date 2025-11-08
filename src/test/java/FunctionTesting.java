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
        PayloadBuilder payloadBuilder = new PayloadBuilder();
        // Mock sample responses
        Response userResponse = mockResponse("""
                {
                  "users": [
                    { "id": "U101", "role": "Admin" },
                    { "id": "U102", "role": "Viewer" }
                  ],
                  "meta": {
                    "count": 2
                  }
                }
                """);

        // Base payload
        JsonObject basePayload = JsonParser.parseString("""
                {
                  "team": {
                    "members": [
                        { 
                        "id": "", 
                        "role": "" },                    
                        { 
                            "id": "", 
                            "role": ""
                        }
                    ]
                  }
                }
                """).getAsJsonObject();

        // Modification rules (with array references)
        JsonObject modifications = JsonParser.parseString("""
                {
                  "team.members[0].id": "$[userResponse].users[0].id",
                  "team.members[0].role": "$[userResponse].users[0].role",
                  "team.members[1].id": "$[userResponse].users[1].id",
                  "team.members[1].role": "$[userResponse].users[1].role",
                  "team.total": "$[userResponse].meta.count"
                }
                """).getAsJsonObject();

        Map<String, Response> responses = Map.of("userResponse", userResponse);

        JsonObject finalPayload = PayloadBuilder.buildRequestPayload(basePayload, modifications, responses);
        //System.out.println("✅ Final Payload:\n" + gson.toJson(finalPayload));
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
