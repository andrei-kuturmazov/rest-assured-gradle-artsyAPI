import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import pages.ParseMethods;
import utils.EndPoints;
import utils.Property;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("API tests for artsy.net")
public class Requests extends BeforeRequest {
    private String GustavKlimtID = "";
    private Response response = null;

    @Test
    @Order(1)
    @DisplayName("Check Gustav Klimt ID")
    public void checkArtistId() {
        response = given()
                .spec(requestSpec)
                .when()
                .get(EndPoints.SEARCH + "Gustav Klimt");
        GustavKlimtID = ParseMethods.getArtistIdFromSearch(response);
        Assertions.assertEquals("4d8b92b64eb68a1b2c000414", GustavKlimtID);
    }

    @Test
    @Order(2)
    @DisplayName("Check Gustav's hometown")
    public void checkArtistHomeTown() {
        response = given()
                .spec(requestSpec)
                .when()
                .get(EndPoints.ARTISTS + "/" + GustavKlimtID);
        String hometown = ParseMethods.getArtistHometown(response);
        Assertions.assertEquals("Baumgarten, Austria", hometown);
    }

    @Test
    @Order(3)
    @DisplayName("Check Gustav's nationality")
    public void checkArtistNationality() {
        response = given()
                .spec(requestSpec)
                .when()
                .get(EndPoints.ARTISTS + "/" + GustavKlimtID);
        String nationality = ParseMethods.getArtistNationality(response);
        Assertions.assertEquals("Austrian", nationality);
    }

    @Test
    @Order(4)
    @DisplayName("Check if Gustav has artwork 'Curled up Girl on Bed'")
    public void checkArtistHasArtwork() {
        response = given()
                .spec(requestSpec)
                .when()
                .get(EndPoints.ARTWORKS + "/?artist_id=" + GustavKlimtID);
        Assertions.assertTrue(response.getBody().asString().contains("Curled up Girl on Bed"));
    }

    @Test
    @Order(5)
    @DisplayName("Check time of creation and update of image")
    public void checkImageCreatedTime() {
        String imageID = "54d359787261691caca30900";
        List<String> expectedTimes = new ArrayList<>(Arrays.asList("2015-02-05T11:52:24+00:00", "2015-02-05T11:52:26+00:00"));
        List<String> actualTimes = new ArrayList<>();
        response = given()
                .spec(requestSpec)
                .when()
                .get(EndPoints.IMAGES + "/" + imageID);
        actualTimes.add(ParseMethods.getJsonObjectFromResponse(response).get("created_at").getAsString());
        actualTimes.add(ParseMethods.getJsonObjectFromResponse(response).get("updated_at").getAsString());
        Assertions.assertArrayEquals(expectedTimes.toArray(), actualTimes.toArray());
    }

    @Test
    @Order(6)
    @DisplayName("Check username")
    public void checkUserName() throws IOException {
        response = given()
                .spec(requestSpec)
                .when()
                .header("X-Access-Token", Property.getPropertyValue("access_token"))
                .get(EndPoints.USERS+ "/" + Property.getPropertyValue("user_id"));
        String username = ParseMethods.getJsonObjectFromResponse(response).get("name").getAsString();
        Assertions.assertEquals("andrei_kuturmazov", username);
    }
}

