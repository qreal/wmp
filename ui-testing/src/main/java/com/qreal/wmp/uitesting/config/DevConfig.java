package com.qreal.wmp.uitesting.config;

import com.codeborne.selenide.WebDriverRunner;
import com.qreal.wmp.uitesting.Auther;
import com.qreal.wmp.uitesting.Opener;
import com.qreal.wmp.uitesting.dia.services.Pallete;
import com.qreal.wmp.uitesting.dia.services.PropertyEditor;
import com.qreal.wmp.uitesting.dia.services.Scene;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

/** Creates beans for Spring needs. **/
@Configuration
@PropertySource("classpath:pages.properties")
public class DevConfig {

    @Autowired
    Environment environment;
    
    /** Processor for Environment linked to property files.*/
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    
    @Bean
    public WebDriver webDriver() {
        ChromeDriverManager.getInstance().setup();
        WebDriver driver = new ChromeDriver();
        WebDriverRunner.setWebDriver(driver);
        return driver;
    }

    @Bean
    public Scene scene() {
        return new Scene(webDriver());
    }
    
    @Bean
    public Auther auther() {
        return new Auther(environment);
    }
    
    @Bean
    public Opener opener() {
        return new Opener(environment, auther(), scene());
    }
    
    @Bean
    public PropertyEditor propertyEditor() {
        return new PropertyEditor();
    }
    
    @Bean
    public Pallete pallete() {
        return new Pallete();
    }
}
