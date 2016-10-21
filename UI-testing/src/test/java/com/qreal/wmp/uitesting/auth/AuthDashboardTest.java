package com.qreal.wmp.uitesting.auth;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.close;
import static com.codeborne.selenide.Selenide.open;

public class AuthDashboardTest {

    @Before
    public void openAuthPage() {
        open("http://localhost:8080/dashboard/");
        $(byText("Sign in to continue to Auth")).shouldBe(exist);
        $(byText("Dashboard")).shouldNotBe(exist);
    }

    @Test
    public void userCanLoginByUsername() {
        $(By.name("username")).setValue("123");
        $(By.name("password")).setValue("123");
        $("[type=\"submit\"]").click();
        $(byText("Dashboard")).shouldBe(exist);
    }

    @Test
    public void  userWrongAuth() {
        $(byText("Password or login wrong")).shouldNotBe(exist);
        String wrongLogin = RandomStringUtils.random(20);
        String wrongPassword = RandomStringUtils.random(20);
        $(By.name("username")).setValue(wrongLogin);
        $(By.name("password")).setValue(wrongPassword);
    }

    @After
    public void logout() {

        // Should logout, but now simply close the browser
        close();
    }
}
