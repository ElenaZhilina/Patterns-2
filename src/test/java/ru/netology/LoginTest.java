package ru.netology;

import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import com.codeborne.selenide.impl.SelenideElementListIterator;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import java.time.Duration;
import static ru.netology.DataGenerator.Registration.getValidUser;
import static ru.netology.DataGenerator.Registration.getUser;
import static ru.netology.DataGenerator.randomLogin;
import static ru.netology.DataGenerator.randomPwd;


public class LoginTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Successfully login with valid active user")
    void successLoginIfUserValidActive() {
        var validUser = getValidUser("active");
        $("input[name='login']").setValue(validUser.getLogin());
        $("input[name='password']").setValue(validUser.getPassword());
        $("button[data-test-id='action-login']").click();
        $("h2").shouldHave(Condition.exactText("Личный кабине")).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Unsuccessfully login with valid blocked user")
    void unsuccessLoginIfUserValidBlocked() {
        var blockedUser = getValidUser("blocked");
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Error if login with invalid user")
    void errorIfInvalidUser() {
        var invalidUser = getUser("active");
        $("[data-test-id='login'] input").setValue(invalidUser.getLogin());
        $("[data-test-id='password'] input").setValue(invalidUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Error if insert wrong login")
    void errorIfWrongLogin() {
        var validUser = getValidUser("active");
        var wrongLogin = randomLogin();
        $("[data-test-id='login'] input").setValue(wrongLogin);
        $("[data-test-id='password'] input").setValue(validUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Error if insert wrong password")
    void errorIfWrongPassword() {
        var validUser = getValidUser("active");
        var wrongPwd = randomPwd();
        $("[data-test-id='login'] input").setValue(validUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPwd);
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }
}