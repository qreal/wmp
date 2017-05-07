package com.qreal.wmp.uitesting.mousegestures;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.InputEvent;
import java.net.URISyntaxException;

import static com.codeborne.selenide.Selenide.open;

/** For finding top left corner of the body in the any opened html file. */
public class RobotCalibration {
    
    private static final Logger logger = LoggerFactory.getLogger(RobotCalibration.class);
    
    private static int lastKnownPointX;
    
    private static int lastKnownPointY;
    
    /** Time for which to wait for the page response. */
    private static final long TIMEOUT = 1000;
    
    private final WebDriver driver;
    
    private final Robot robot;
    
    private final Point browserCenter;
    
    private CalibratedPoint xPoint = new CalibratedPoint();
    
    private CalibratedPoint yPoint = new CalibratedPoint();
    
    private int counter = 0;
    
    public static Point calibrate(WebDriver driver) {
        Point point = new RobotCalibration(driver).calibrate();
        lastKnownPointX = point.x;
        lastKnownPointY = point.y;
        return point;
    }
    
    public static int getLastKnownPointX() {
        return lastKnownPointX;
    }
    
    public static int getLastKnownPointY() {
        return lastKnownPointY;
    }
    
    @SuppressWarnings("ConstantConditions")
    private RobotCalibration(WebDriver driver) {
        try {
            open(this.getClass().getClassLoader().getResource("RobotCalibration.html").toURI().toString());
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
        
        this.driver = driver;
        ((JavascriptExecutor) driver).executeScript("alert(\"Focus window\")");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        driver.switchTo().alert().accept();
        try {
            driver.manage().window().getSize();
        } catch (UnsupportedOperationException headlessBrowserException) {
            throw new IllegalArgumentException("Calibrating a headless browser makes no sense.",
                    headlessBrowserException);
        }
        
        try {
            this.robot = new Robot();
        } catch (AWTException headlessEnvironmentException) {
            throw new IllegalStateException("Robot won't work on headless environments.",
                    headlessEnvironmentException);
        }
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        org.openqa.selenium.Dimension browserSize = driver.manage().window().getSize();
        org.openqa.selenium.Point browserPos = driver.manage().window().getPosition();
        
        // a maximized browser returns negative position
        // a maximized browser returns size larger than actual screen size
        // you can't click outside the screen
        xPoint.begin = Math.max(0, browserPos.x);
        xPoint.end = Math.min(xPoint.begin + browserSize.width, screenSize.width - 1);
        xPoint.mid = (xPoint.begin + xPoint.end) / 2;
        
        yPoint.begin = Math.max(0, browserPos.y);
        yPoint.end = Math.min(yPoint.begin + browserSize.height, screenSize.height - 1);
        yPoint.mid = (yPoint.begin + yPoint.end) / 2;
        
        browserCenter = new Point(xPoint.mid, yPoint.mid);
    }
    
    private Point calibrate() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        click(xPoint.begin, yPoint.begin);
        // find left border
        while (xPoint.begin < xPoint.end) {
            click(xPoint.mid, yPoint.mid);
            xPoint = shift(xPoint);
        }
        
        // find top border
        while (yPoint.begin < yPoint.end) {
            click(xPoint.mid, yPoint.mid);
            yPoint = shift(yPoint);
        }
        
        if (!isCalibrated()) {
            throw new IllegalStateException("Couldn't calibrate the Robot.");
        }
        return new Point(xPoint.mid + 5, yPoint.mid + 5);
    }
    
    /** Clicks on the specified location. */
    private void click(int xPosition, int yPosition) {
        robot.mouseMove(xPosition, yPosition);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }
    
    /** @return whether the click on a page was successful. */
    private boolean clickWasSuccessful() {
        counter++;
        
        long targetTime = System.currentTimeMillis() + TIMEOUT;
        while (System.currentTimeMillis() < targetTime) {
            int pageCounter = Integer.parseInt(driver.findElement(By.id("counter")).getAttribute("value"));
            if (counter == pageCounter) {
                return true;
            }
        }
        return false;
    }
    
    /** Returns whether the top left corner has already been clicked at. */
    private boolean isCalibrated() {
        long targetTime = System.currentTimeMillis() + TIMEOUT;
        while (System.currentTimeMillis() < targetTime) {
            if (driver.findElement(By.id("done")).getAttribute("value").equals("yep")) {
                return true;
            }
        }
        return false;
    }
    
    private CalibratedPoint shift(CalibratedPoint point) {
        if (clickWasSuccessful()) {
            point.end = point.mid;
        } else {
            point.begin = point.mid + 1;
            click(browserCenter.x, browserCenter.y);
        }
        point.mid = (point.begin + point.end) / 2;
        return point;
    }
    
    private class CalibratedPoint {
        private int begin;
        
        private int end;
        
        private int mid;
    }
}