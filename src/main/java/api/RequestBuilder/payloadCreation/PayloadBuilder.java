package api.RequestBuilder.payloadCreation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.io.IOException;

public class PayloadBuilder {
    public void buildRequestPayload(JsonObject unalteredPayloadBody, JsonObject requestPayloadFieldsToModify, Response... referenceResponse) throws IOException {

        for(String fieldToModify: requestPayloadFieldsToModify.keySet()){
            JsonPath jsonPath = new JsonPath(String.valueOf(unalteredPayloadBody));
            Object value = jsonPath.get(fieldToModify);
        }
        //return alteredPayload;

    }
}
