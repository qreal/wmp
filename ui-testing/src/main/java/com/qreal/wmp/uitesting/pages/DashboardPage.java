package com.qreal.wmp.uitesting.pages;

import static com.codeborne.selenide.Selenide.title;

/** Describes Dashboard page of the WMP project. */
public class DashboardPage {
    
    private final String title;
    
    public DashboardPage(String title) {
        this.title = title;
    }
    
    public boolean onPage() {
        return title().equals(title);
    }
}
