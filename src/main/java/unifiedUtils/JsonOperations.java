package unifiedUtils;

import com.google.gson.*;
import io.restassured.builder.ResponseBuilder;
import io.restassured.response.Response;

import java.io.FileReader;
import java.io.IOException;

public class JsonOperations {
    public JsonElement readJsonFileAndReturnContentAsJsonElement(String jsonFilePath) {
        try(FileReader reader = new FileReader(jsonFilePath)){
            JsonElement jsonElement = JsonParser.parseReader(reader);
            if(jsonElement.isJsonObject()){
                return jsonElement.getAsJsonObject();
            } else {
                return jsonElement.getAsJsonArray();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file: " + jsonFilePath, e);
        }
    }
    public Response convertJsonObjectToResponse(JsonObject jsonObject) {
        ResponseBuilder builder = new ResponseBuilder();
        builder.setBody(jsonObject.toString());
        builder.setStatusCode(200);
        builder.setContentType("application/json");

        return builder.build();
    }
}
