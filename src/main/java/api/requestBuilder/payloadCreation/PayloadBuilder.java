package api.requestBuilder.payloadCreation;

import com.google.gson.*;
import io.restassured.builder.ResponseBuilder;
import io.restassured.response.Response;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PayloadBuilder {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Pattern REF_PATTERN = Pattern.compile("^\\$\\[([a-zA-Z0-9_]+)]\\.(.+)$");
    // Matches: $[responseName].json.path

    /**
     * Builds dynamic payload with static + chained (named) values.
     * Example syntax in modifications JSON:
     * {
     *   "user.id": "$[createUserResponse].data.id",
     *   "user.token": "$[authResponse].data.token",
     *   "user.status": "ACTIVE"
     * }
     */
    public static JsonObject buildRequestPayload(JsonObject basePayload, JsonObject modifications, Map<String, Response> namedResponses) {

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
    }

    /**
     * Handles nested JSON creation with array support.
     * Supports paths like team.members[0].id
     */
    private static void updateJsonField(JsonObject root, String path, JsonElement value) {
        String[] tokens = path.split("\\.");

        JsonElement current = root;
        for (int i = 0; i < tokens.length - 1; i++) {
            String token = tokens[i];
            Matcher arrMatcher = Pattern.compile("(\\w+)\\[(\\d+)]").matcher(token);

            if (arrMatcher.matches()) {
                // Handle array step
                String arrayName = arrMatcher.group(1);
                int index = Integer.parseInt(arrMatcher.group(2));

                JsonArray array = current.getAsJsonObject().has(arrayName)
                        ? current.getAsJsonObject().getAsJsonArray(arrayName)
                        : new JsonArray();

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

            JsonArray array = current.getAsJsonObject().has(arrayName)
                    ? current.getAsJsonObject().getAsJsonArray(arrayName)
                    : new JsonArray();

            while (array.size() <= index) {
                array.add(JsonNull.INSTANCE);
            }

            array.set(index, value);
            current.getAsJsonObject().add(arrayName, array);
        } else {
            current.getAsJsonObject().add(last, value);
        }
    }

    private static Response mockResponse(String jsonBody) {
        return new ResponseBuilder()
                .setStatusCode(200)
                .setBody(jsonBody)
                .setContentType("application/json")
                .build();
    }
}
