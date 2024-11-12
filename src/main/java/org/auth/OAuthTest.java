package org.auth;

import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;

public class OAuthTest {

    public static void main(String[] args) throws InterruptedException {
        // The solution to get the code was to do it manually
        String url = "https://rahulshettyacademy.com/getCourse.php?state=detroit&code=4%2F0AVG7fiS8gXPS697UhiLgz-KzRSEep2A5HeWJnszSlnQskZh7UbAICn-rltYcTs20Zx5diA&scope=email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&authuser=0&prompt=consent";
        String partialCode = url.split("code=")[1];
        String code = partialCode.split("&scope")[0];

        String accessTokenResponse = given().urlEncodingEnabled(false) // To avoid issues with special characters in code
                .queryParams("code", code,
                        "client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com",
                        "client_secret", "erZOWM9g3UtwNRj340YYaK_W",
                        "redirect_uri", "https://rahulshettyacademy.com/getCourse.php",
                        "grant_type", "authorization_code").log().all()
                .when().post("https://www.googleapis.com/oauth2/v4/token").asString();

        JsonPath accessTokenResponseJS = new JsonPath(accessTokenResponse);
        String accessToken = accessTokenResponseJS.getString("access_token");

        String getCourseResponse = given().queryParam("access_token", accessToken).log().all()
                .when().log().all().get("https://rahulshettyacademy.com/getCourse.php").asString();

        System.out.println(getCourseResponse);

    }
}