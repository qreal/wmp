package com.qreal.wmp.uitesting.config;

import com.codeborne.selenide.WebDriverRunner;
import com.qreal.wmp.uitesting.PageFactory;
import com.qreal.wmp.uitesting.PageLoader;
import com.qreal.wmp.uitesting.services.Auther;
import com.qreal.wmp.uitesting.services.Opener;
import com.qreal.wmp.uitesting.services.impl.AutherImpl;
import com.qreal.wmp.uitesting.services.impl.OpenerImpl;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import java.util.concurrent.TimeUnit;

/** Creates beans for Spring needs. **/
@Configuration
@PropertySource("classpath:pages.properties")
public class DevConfig {

    @Autowired
    private Environment environment;
    
    /** Processor for Environment linked to property files.*/
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    
    @Bean
    public WebDriver webDriver() {
        ChromeDriverManager.getInstance().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(3, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        WebDriverRunner.setWebDriver(driver);
        return driver;
    }
    
    @Bean
    public Auther auther() {
        return new AutherImpl(environment);
    }
    
    @Bean
    public Opener opener() {
        return new OpenerImpl(environment, auther());
    }
    
    @Bean
    public PageFactory pageFactory() {
        return new PageFactory(webDriver());
    }
    
    @Bean
    public PageLoader pageLoader() {
        return new PageLoader(pageFactory(), opener(), auther());
    }
}
