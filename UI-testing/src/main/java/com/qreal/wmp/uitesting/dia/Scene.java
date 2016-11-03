package com.qreal.wmp.uitesting.dia;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$$;

@Service
public class Scene {

    private final String selector = ".scene-wrapper";

    private List<SelenideElement> elements = new ArrayList<>();

    public SelenideElement dragAndDrop(SelenideElement element) {
        List<SelenideElement> all = $$(By.cssSelector(selector + " #v_7 > *"));
        element.dragAndDropTo(selector);
        SelenideElement newEl = all.stream().filter(x -> !elements.stream()
                .anyMatch(y -> x.attr("id").equals(y.attr("id")))).findFirst().get();
        elements.add(newEl);
        return newEl;
    };

    public boolean exist(SelenideElement selenideElement) {
        return elements.stream().anyMatch(x -> x.equals(selenideElement));
    }

}
