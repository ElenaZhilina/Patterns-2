package ru.netology;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.http.ContentType;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static final RequestSpecification request = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    private static RegistrationDto sendRequest(RegistrationDto user) {
        given()
                .spec(request)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
        return user;
    }

    public static String randomLogin() {
        return faker.name().username();
    }

    public static String randomPwd() {
        return faker.internet().password();
    }

    public static class Registration {
        private Registration() {
        }

        public static RegistrationDto getUser(String status) {
            return new RegistrationDto(randomLogin(), randomPwd(), status);
        }

        public static RegistrationDto getValidUser(String status) {
            return sendRequest(getUser(status));
        }
    }

    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }

}