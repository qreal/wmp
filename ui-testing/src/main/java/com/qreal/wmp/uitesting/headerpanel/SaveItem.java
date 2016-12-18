package com.qreal.wmp.uitesting.headerpanel;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class SaveItem {

    public static final By selector = By.cssSelector("#diagrams .modal-content");

    public void createFolder(String folderName) {
        $(selector).find(By.id("creating-menu")).click();
        $(selector).find(By.className("folder-menu")).find(By.cssSelector("[type=\"text\"]")).setValue(folderName);
        $(selector).find(By.className("folder-menu")).find(By.id("creating")).click();
    }
}
