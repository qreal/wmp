package com.qreal.wmp.uitesting.config;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.qreal.wmp.uitesting")
public class AppInit {

    /** Main function creates context. */
    public static void main(final String... args) {
        ChromeDriverManager.getInstance().setup();
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.qreal.wmp.uitesting");
        context.register(AppInit.class);
        context.refresh();
    }
}
