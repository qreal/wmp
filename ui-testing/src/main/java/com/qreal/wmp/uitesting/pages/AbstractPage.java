package com.qreal.wmp.uitesting.pages;

import static com.codeborne.selenide.WebDriverRunner.url;

/** Any page must by identified by its url. */
public abstract class AbstractPage {
    
    private final String url;
    
    protected AbstractPage() {
        this.url = url();
    }
    
    public boolean onPage() {
        String currentUrl = url();
        return url.equals(currentUrl.contains("?") ? currentUrl.substring(0, currentUrl.indexOf("?")) : currentUrl);
    }
}
