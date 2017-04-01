package com.qreal.wmp.uitesting.mousegestures;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.awt.*;
import java.awt.event.InputEvent;
import java.net.URISyntaxException;

public class RobotCalibration {
    
    private static int lastKnownPointX;
    
    private static int lastKnownPointY;
    
    /** Time for which to wait for the page response. */
    private static final long TIMEOUT = 1000;
    
    private final WebDriver driver;
    
    private final Robot robot;
    
    private final Point browserCenter;
    
    private CalibratedPoint xPoint;
    
    private CalibratedPoint yPoint;
    
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
            com.codeborne.selenide.Selenide.open(
                    this.getClass().getClassLoader().getResource("RobotCalibration.html").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        
        this.driver = driver;
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
    
    /** Clicks on the specified location */
    private void click(int x, int y) {
        robot.mouseMove(x, y);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        
        // for some reason, my IE8 can't properly register clicks that are close
        // to each other faster than click every half a second
        if (driver instanceof InternetExplorerDriver) {
            sleep(500);
        }
    }
    
    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
            // nothing to do
        }
    }
    
    /** @return whether the click on a page was successful */
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
    
    /** @return whether the top left corner has already been clicked at */
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