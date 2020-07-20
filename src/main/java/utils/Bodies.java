package utils;

import com.google.gson.JsonObject;

import java.io.IOException;

public class Bodies {

    public static String getRequestTokenBody() {
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty("client_id", Property.getPropertyValue("client_id"));
            jsonObject.addProperty("client_secret", Property.getPropertyValue("client_secret"));
        } catch (IOException e) {
            Log.error(e);
        }
        return jsonObject.toString();
    }
}

