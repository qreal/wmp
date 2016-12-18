package com.qreal.wmp.uitesting.headerpanel;

import org.openqa.selenium.By;
import org.springframework.stereotype.Service;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

@Service
public class HeaderPanel {

    public final static By selector = By.id("main-toolbar-area");

    private FileItem fileItem;

    public HeaderPanel() {
        fileItem = new FileItem();
    }

    public void clickDashboard() {
        $(selector).find(withText("Dashboard")).click();
    }

    public FileItem getFileItem() {
        $(FileItem.selector).click();
        return fileItem;
    }
}
