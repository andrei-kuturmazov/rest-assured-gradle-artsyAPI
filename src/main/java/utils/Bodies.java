package utils;

import com.google.gson.JsonObject;

public class Bodies {

    public static String getRequestTokenBody() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("client_id", Property.getProperty("client_id"));
        jsonObject.addProperty("client_secret", Property.getProperty("client_secret"));
        return jsonObject.toString();
    }
}

