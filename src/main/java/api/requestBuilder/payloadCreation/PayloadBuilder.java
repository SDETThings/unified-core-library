package api.requestBuilder.payloadCreation;

import com.google.gson.*;
import io.restassured.builder.ResponseBuilder;
import io.restassured.response.Response;
import unifiedUtils.JsonOperations;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PayloadBuilder {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    JsonOperations jsonOperations;
    private static final Pattern REF_PATTERN = Pattern.compile("^\\$\\[([a-zA-Z0-9_]+)]\\.(.+)$");
    private PayloadBuilder(){

    }
    public static PayloadBuilder getPayloadBuilderInstance(){
        return new PayloadBuilder();
    }
    // Matches: $[responseName].json.path

    /**
     * Payload builder with chained response value support.
     * @param basePayload - the original payload
     * @param modifications - the fields to modify
     * @param namedResponses - map of named responses to extract chained values from
     * @return - the modified payload
     */
    /*public static JsonObject buildRequestPayload(JsonObject basePayload, JsonObject modifications, Map<String, Response> namedResponses) {

        JsonObject alteredPayload = gson.fromJson(basePayload, JsonObject.class);

        for (String fieldToModify : modifications.keySet()) {
            JsonElement valueToApply = modifications.get(fieldToModify);

            if (valueToApply.isJsonPrimitive()) {
                String valStr = valueToApply.getAsString().trim();
                Matcher matcher = REF_PATTERN.matcher(valStr);

                if (matcher.matches()) {
                    String responseName = matcher.group(1);
                    String jsonPath = matcher.group(2);

                    if (namedResponses == null || !namedResponses.containsKey(responseName)) {
                        System.out.println("⚠️ Skipping substitution for " + valStr + " (no response found)");
                        updateJsonField(alteredPayload, fieldToModify, JsonNull.INSTANCE);
                        continue;
                    }

                    Object extracted = namedResponses.get(responseName).jsonPath().get(jsonPath);
                    updateJsonField(alteredPayload, fieldToModify, gson.toJsonTree(extracted));
                    continue;
                }
            }

            updateJsonField(alteredPayload, fieldToModify, valueToApply);
        }

        return alteredPayload;
    }*/
    public JsonObject buildRequestPayload(JsonObject basePayload, JsonObject modifications, List<Response> namedResponses) {
        JsonObject alteredPayload = gson.fromJson(basePayload, JsonObject.class);
        for (String key : modifications.keySet()) {
            JsonElement expectedValue = modifications.get(key);
            if (expectedValue.getAsString().startsWith("%")) {
                if (!namedResponses.isEmpty()) {
                    for (Response response : namedResponses) {
                        if (response.jsonPath().get(expectedValue.getAsString().substring(1)) != null) {
                            JsonElement chainedValue = new Gson().toJsonTree(response.jsonPath().get(expectedValue.getAsString().substring(1)));
                            alteredPayload.add(key, chainedValue);
                            break;
                        }
                    }
                }
            }else {
                alteredPayload.add(key, expectedValue);
            }
        }
        return alteredPayload;
    }

    /**
     * Simple payload builder without chained values.
     * @param basePayload - the original payload
     * @param modifications - the fields to modify
     * @return - the modified payload
     */
    public JsonObject buildRequestPayload(JsonObject basePayload, JsonObject modifications) {
        JsonObject alteredPayload = gson.fromJson(basePayload, JsonObject.class);
        for (String key : modifications.keySet()) {
            JsonElement expectedValue = modifications.get(key);
            alteredPayload.add(key, expectedValue);
        }
        return alteredPayload;
    }

    /**
     * Payload builder with chained response value support and previous payloads.
     * @param basePayload - the original payload
     * @param modifications - the fields to modify
     * @param namedResponses - list of named responses to extract chained values from
     * @param previousPayloads - list of previous payloads to extract chained values from
     * @return - the modified payload
     */
    public JsonObject buildRequestPayload(JsonObject basePayload, JsonObject modifications,List<Response> namedResponses, List<JsonObject> previousPayloads) {
        JsonObject alteredPayload = gson.fromJson(basePayload, JsonObject.class);
        jsonOperations = new JsonOperations();
        for (String key : modifications.keySet()) {
            boolean isFound=false;
            JsonElement expectedValue = modifications.get(key);
            if (expectedValue.getAsString().startsWith("%")) {
               responseCheck:{
                   if (namedResponses != null && !namedResponses.isEmpty()) {
                       for (Response response : namedResponses) {
                           if (response.jsonPath().get(expectedValue.getAsString().substring(1)) != null) {
                               JsonElement chainedValue = new Gson().toJsonTree(response.jsonPath().get(expectedValue.getAsString().substring(1)));
                               alteredPayload.add(key, chainedValue);
                               isFound = true;
                               break;
                           }
                       }
                   }
               }
               PayloadsCheck:{
                   if(!isFound) {
                       for (JsonObject payload : previousPayloads) {
                           if (!previousPayloads.isEmpty()) {
                               if (payload.has(expectedValue.getAsString().substring(1))) {
                                   Response convertedResponse = jsonOperations.convertJsonObjectToResponse(payload);
                                   JsonElement chainedValue = JsonParser.parseString(convertedResponse.jsonPath().get(expectedValue.getAsString().substring(1)));
                                   alteredPayload.add(key, chainedValue);
                                   break;
                               }
                           }
                       }
                   }
               }
            } else {
                alteredPayload.add(key, expectedValue);
            }
        }
        return alteredPayload;
    }
    /**
     * Handles nested JSON creation with array support.
     * Supports paths like team.members[0].id
     */
    private void updateJsonField(JsonObject root, String path, JsonElement value) {
        String[] tokens = path.split("\\.");

        JsonElement current = root;
        for (int i = 0; i < tokens.length - 1; i++) {
            String token = tokens[i];
            Matcher arrMatcher = Pattern.compile("(\\w+)\\[(\\d+)]").matcher(token);

            if (arrMatcher.matches()) {
                // Handle array step
                String arrayName = arrMatcher.group(1);
                int index = Integer.parseInt(arrMatcher.group(2));

                JsonArray array = current.getAsJsonObject().has(arrayName) ? current.getAsJsonObject().getAsJsonArray(arrayName) : new JsonArray();

                // Expand array if index doesn't exist yet
                while (array.size() <= index) {
                    array.add(new JsonObject());
                }

                current.getAsJsonObject().add(arrayName, array);
                current = array.get(index);
            } else {
                // Normal object field
                JsonObject obj = current.getAsJsonObject();
                if (!obj.has(token) || !obj.get(token).isJsonObject()) {
                    obj.add(token, new JsonObject());
                }
                current = obj.get(token);
            }
        }

        // Final token (could also be array)
        String last = tokens[tokens.length - 1];
        Matcher arrMatcher = Pattern.compile("(\\w+)\\[(\\d+)]").matcher(last);
        if (arrMatcher.matches()) {
            String arrayName = arrMatcher.group(1);
            int index = Integer.parseInt(arrMatcher.group(2));

            JsonArray array = current.getAsJsonObject().has(arrayName) ? current.getAsJsonObject().getAsJsonArray(arrayName) : new JsonArray();

            while (array.size() <= index) {
                array.add(JsonNull.INSTANCE);
            }

            array.set(index, value);
            current.getAsJsonObject().add(arrayName, array);
        } else {
            current.getAsJsonObject().add(last, value);
        }
    }

    private Response mockResponse(String jsonBody) {
        return new ResponseBuilder().setStatusCode(200).setBody(jsonBody).setContentType("application/json").build();
    }
}
