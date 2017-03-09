package com.qreal.wmp.uitesting.innertests;

import com.qreal.wmp.uitesting.config.AppInit;
import com.qreal.wmp.uitesting.exceptions.WrongAuthException;
import com.qreal.wmp.uitesting.services.Auther;
import com.qreal.wmp.uitesting.services.Opener;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

/** Tests for opener and auther services. */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppInit.class, loader = AnnotationConfigContextLoader.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthTest {

    @Autowired
    private Auther auther;

    @Autowired
    private Opener opener;

    /**
     * Try to login with correct username and password.
     * Should redirect to OAuth page.
     */
    @Test
    public void authTest() {
        try {
            opener.cleanOpen("auth");
            assert inAuthPage();
            auther.auth();
            assert $(byText("OAuth Server")).waitUntil(appear, 5000).exists();
        } catch (WrongAuthException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Try to login with random username and password.
     * An error must be shown.
     */
    @Test
    public void authWrongTest() {
        opener.cleanOpen("auth");
        assert inAuthPage();
        final char[] alphabet = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        final String wrongLogin = RandomStringUtils.random(20, alphabet);
        final String wrongPassword = RandomStringUtils.random(20, alphabet);
        try {
            auther.auth(wrongLogin, wrongPassword);
        } catch (WrongAuthException e) {
            System.err.println(e.getMessage());
        }
        $(byText("Password or login wrong")).waitUntil(appear, 5000);
    }

    /**
     * Try to open dashboard page without authentication.
     * Should be redirected to auth page.
     * Try to open dashboard page with correct login and password.
     */
    @Test
    public void dashboardTest() {
        opener.cleanOpen("dashboard");
        assert inAuthPage();
        opener.open("dashboard");
        $(byText("Dashboard")).waitUntil(appear, 5000);
    }

    /**
     * Try to open robots-editor page without authentication.
     * Should be redirected to auth page.
     * Try to open robots-editor page with correct login and password.
     */
    @Test
    public void robotsEditorTest() {
        opener.cleanOpen("robotsEditor");
        assert inAuthPage();
        opener.open("robotsEditor");
        $(byText("Property Editor")).waitUntil(appear, 5000);
    }
    
    /**
     * Try to open bpmn-editor page without authentication.
     * Should be redirected to auth page.
     * Try to open bpmn-editor page with correct login and password.
     */
    @Test
    public void bpmnEditorTest() {
        opener.cleanOpen("bpmnEditor");
        assert inAuthPage();
        opener.open("bpmnEditor");
        $(byText("Property Editor")).waitUntil(appear, 5000);
    }
    
    /**
     * Check that current page is Auth page.
     *
     * @return true if it is
     */
    private boolean inAuthPage() {
        return  $(byText("Sign in to continue to Auth")).exists();
    }
}
