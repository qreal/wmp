package com.qreal.wmp.uitesting.innertests;

import com.codeborne.selenide.Condition;
import com.qreal.wmp.uitesting.config.AppInit;
import com.qreal.wmp.uitesting.exceptions.WrongAuthException;
import com.qreal.wmp.uitesting.services.Auther;
import com.qreal.wmp.uitesting.services.Opener;
import com.qreal.wmp.uitesting.services.SelectorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
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

    private static final String WRONG_LOGIN = "lbltfn16vup5boj7o1ju";
    
    private static final String WRONG_PASSWORD = "8epo7li9uq5vs3wujpm4";
    
    @Autowired
    private Auther auther;

    @Autowired
    private Opener opener;
    
    @Autowired
    private SelectorService selectorService;
    
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
        assert !$(By.id(selectorService.get("authform.wrongAuthLabel", SelectorService.Attribute.ID)))
                .isDisplayed();
        assert inAuthPage();
        try {
            auther.auth(WRONG_LOGIN, WRONG_PASSWORD);
        } catch (WrongAuthException e) {
            System.err.println(e.getMessage());
        }
        $(By.id(selectorService.get("authform.wrongAuthLabel", SelectorService.Attribute.ID)))
                .waitUntil(Condition.visible, 5000);
    }

    /**
     * Try to open dashboard page without authentication.
     * Should be redirected to auth page.
     * Try to open dashboard page with correct login and password..
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
        return  $(By.id(selectorService.get("authform.usernameInput", SelectorService.Attribute.ID)))
                .isDisplayed();
    }
}
