package com.qreal.wmp.uitesting.pages;

import static com.codeborne.selenide.Selenide.title;

public class AuthPage {
    
    private final String title;
    
    public AuthPage(String title) {
        this.title = title;
    }
    
    public boolean onPage() {
        return title.equals(title());
    }
}
