package pages;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.response.Response;
import utils.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseMethods {

    public static JsonObject getJsonObjectFromResponse(Response response) {
        return new JsonParser().parse(response.getBody().asString()).getAsJsonObject();
    }

    public static String getAXAPPToken(Response response) {
        return getJsonObjectFromResponse(response).get("token").getAsString();
    }

    public static String getArtistLinkFromSearch(Response response) {
        return getJsonObjectFromResponse(response)
                .get("_embedded").getAsJsonObject()
                .get("results").getAsJsonArray()
                .get(0).getAsJsonObject()
                .get("_links").getAsJsonObject()
                .get("self").getAsJsonObject()
                .get("href").getAsString();
    }

    public static String getArtistIdFromSearch(Response response) {
        String artistId = "";
        String artistLink = getArtistLinkFromSearch(response);
        Matcher m = Pattern.compile("[\\w+|\\d+]{24}").matcher(artistLink);
        while (m.find()) {
            artistId = m.group(0);
        }
        Log.info("Artist id from search: " + artistId);
        return artistId;
    }

    public static String getArtistHometown(Response response) {
        return getJsonObjectFromResponse(response).get("hometown").getAsString();
    }

    public static String getArtistNationality(Response response) {
        return getJsonObjectFromResponse(response).get("nationality").getAsString();
    }

    public static String getFirstSalesID(Response response) {
        return getJsonObjectFromResponse(response)
                .get("_embedded").getAsJsonObject()
                .get("sales").getAsJsonArray()
                .get(0).getAsJsonObject()
                .get("id").getAsString();
    }

    public static String getFairStatus(Response response) {
        return getJsonObjectFromResponse(response)
                .get("status").getAsString();
    }

    public static String getShowLinkFromFairsResponse(Response response) {
        return getJsonObjectFromResponse(response)
                .get("_links").getAsJsonObject()
                .get("shows").getAsJsonObject()
                .get("href").getAsString();
    }

    public static String getShowIdFromFairsResponse(Response response) {
        String fairIdInsideLink = "";
        String showLink = getShowLinkFromFairsResponse(response);
        Matcher m = Pattern.compile("[\\w+|\\d+]{24}").matcher(showLink);
        while (m.find()) {
            fairIdInsideLink = m.group(0);
        }
        Log.info("ID from shows link inside fairs response" + fairIdInsideLink);
        return fairIdInsideLink;
    }
}

