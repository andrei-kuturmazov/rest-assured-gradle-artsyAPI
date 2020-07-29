import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import pages.ParseMethods;
import utils.EndPoints;
import utils.Property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("API tests for artsy.net")
public class Requests extends BeforeRequest {
    private String DaVinchiID = "";
    private Response response = null;

    @Test
    @Order(1)
    @DisplayName("Check Da Vinci ID")
    public void checkArtistId() {
        response = given()
                .spec(requestSpec)
                .when()
                .get(EndPoints.SEARCH + "Leonardo Da Vinci");
        DaVinchiID = ParseMethods.getArtistIdFromSearch(response);
        Assertions.assertEquals("4d8b92684eb68a1b2c00009e", DaVinchiID);
    }

    @Test
    @Order(2)
    @DisplayName("Check Da Vinci hometown")
    public void checkArtistHomeTown() {
        response = given()
                .spec(requestSpec)
                .when()
                .get(EndPoints.ARTISTS + "/" + DaVinchiID);
        String hometown = ParseMethods.getArtistHometown(response);
        Assertions.assertEquals("Anchiano, Italy", hometown);
    }

    @Test
    @Order(3)
    @DisplayName("Check Da Vinci nationality")
    public void checkArtistNationality() {
        response = given()
                .spec(requestSpec)
                .when()
                .get(EndPoints.ARTISTS + "/" + DaVinchiID);
        String nationality = ParseMethods.getArtistNationality(response);
        Assertions.assertEquals("Italian", nationality);
    }

    @Test
    @Order(4)
    @DisplayName("Check if Leonardo has artwork 'Mona Lisa'")
    public void checkArtistHasArtwork() {
        response = given()
                .spec(requestSpec)
                .when()
                .get(EndPoints.ARTWORKS + "/?artist_id=" + DaVinchiID);
        Assertions.assertTrue(response.getBody().asString().contains("Mona Lisa"));
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
    @DisplayName("Check first sales ID")
    public void checkFirstSalesID() {
        String salesID = "5177449d082cd050f3000221";
        response = given(requestSpec)
                .when()
                .get(EndPoints.SALES);
        String actualSalesID = ParseMethods.getFirstSalesID(response);
        Assertions.assertEquals(salesID, actualSalesID);
    }

    @Test
    @Order(7)
    @DisplayName("Check existing fair status")
    public void checkFairStatus() {
        String fairID = "4dbb0c01771d8967970010a8";
        response = given()
                .spec(requestSpec)
                .when()
                .get(EndPoints.FAIRS + "/" + fairID);
        String actualFairStatus = ParseMethods.getFairStatus(response);
        Assertions.assertEquals("closed", actualFairStatus);
    }

    @Test
    @Order(8)
    @DisplayName("Check the correct interaction of shows link")
    public void checkShowLinkInteraction() {
        String fairID = "4dbb0c01771d8967970010a8";
        response = given()
                .spec(requestSpec)
                .when()
                .get(EndPoints.FAIRS + "/" + fairID);
        Assertions.assertEquals(fairID, ParseMethods.getShowIdFromFairsResponse(response));
    }

    @Test
    @Order(9)
    @DisplayName("Check username")
    public void checkUserName() {
        response = given()
                .spec(requestSpec)
                .when()
                .header("X-Access-Token", Property.getProperty("access_token"))
                .get(EndPoints.USERS + "/" + Property.getProperty("user_id"));
        String username = ParseMethods.getJsonObjectFromResponse(response).get("name").getAsString();
        Assertions.assertEquals("andrei_kuturmazov", username);
    }
}

