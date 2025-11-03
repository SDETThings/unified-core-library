import api.RequestBuilder.payloadCreation.PayloadBuilder;
import api.RequestBuilder.requestMethodTypes.APIMethodType;
import api.RequestBuilder.uriConstruction.UrlBuilder;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class FunctionTesting {
    public static void main(String[] args) throws IOException {
        String jsonStr1 = """
        {
            "category": 
                { 
                    "id": "{{static}}", 
                    "name": "{{static}}" 
                },
            "tags": [ 
                { "name": "Fiction", "count": 5 },
                { "name": "Bestseller", "count": 10 }
            ],
            "active": true
        }
        """;
        String jsonStr2 = """
        {
            "category.id": 102, 
            "category.name": "Books"
        }
        """;
        PayloadBuilder payloadBuilder = new PayloadBuilder();
        payloadBuilder.buildRequestPayload(JsonParser.parseString(jsonStr1).getAsJsonObject(),JsonParser.parseString(jsonStr2).getAsJsonObject());
    }
    public void urlContructionTest(){
        Map<String ,String> pathParam = new LinkedHashMap<>();
        pathParam.put("user","kunal");
        pathParam.put("id","123456");
        Map<String ,String> queryParam = new LinkedHashMap<>();
        queryParam.put("status","true");
        queryParam.put("limit","100");
        UrlBuilder urlBuilder = new UrlBuilder();
        System.out.println(urlBuilder.buildRequestUrl("https://api.example.com/","v2/","users/{user}/{id}/orders?status={}&limit={}",queryParam,pathParam));

    }
}
