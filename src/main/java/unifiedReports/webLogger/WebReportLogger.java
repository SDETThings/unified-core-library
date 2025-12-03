package unifiedReports.webLogger;

import com.aventstack.chaintest.plugins.ChainTestListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.playwright.Page;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class WebReportLogger {
    private WebReportLogger() {
    }
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final WebReportLogger INSTANCE = new WebReportLogger();
    public static WebReportLogger getInstance() {
        return INSTANCE;
    }
    /**
     * Logs a step message with optional screenshot.
     * Works for both Selenium WebDriver and Playwright Page objects.
     * @param stepDescription   Description of the action performed.
     * @param driverOrPage      WebDriver (Selenium) or Page (Playwright) object.
     * @param captureScreenshot Whether to attach a screenshot to the report.
     */
    public void logStep(String stepDescription, Object driverOrPage,boolean isPassed, boolean... captureScreenshot) {
        byte[] screenshotBytes = null;
        if (captureScreenshot[0] && driverOrPage != null) {
            try {
                screenshotBytes = captureScreenshot(driverOrPage);
                if (screenshotBytes != null) {
                    // log the textual step
                    logInfo(stepDescription);
                    // capture screenshot if required
                    ChainTestListener.embed(screenshotBytes, "image/png");
                    //Allure.addAttachment("Screenshot - " + stepDescription, "image/png", new java.io.ByteArrayInputStream(screenshotBytes), ".png");
                }
            } catch (Exception e) {
                // capture screenshot
                if(captureScreenshot[0] && screenshotBytes!=null)
                {
                    ChainTestListener.embed(screenshotBytes, "image/png");
                }
                logError("Failed to log step: " , e);
            }
        }
    }
    /**
     * Capture screenshot for both Selenium and Playwright
     */
    private static byte[] captureScreenshot(Object driverOrPage) {
        try {
            if (driverOrPage instanceof WebDriver driver) {
                // Full-page screenshot using AShot
                Screenshot screenshot = new AShot()
                        .shootingStrategy(ShootingStrategies.viewportPasting(100))
                        .takeScreenshot(driver);
                BufferedImage image = screenshot.getImage();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "PNG", baos);
                return baos.toByteArray();

            } else if (driverOrPage instanceof Page page) {
                // Playwright screenshot (full page)
                return page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Logs only a text step without screenshot.
     */
    public static void logInfo(String message) {
        ChainTestListener.log(message);
    }

    /**
     * Logs an error message with stack trace.
     */
    public static void logError(String message, Throwable error) {
        String log = message + "\n\nError: " + error.getMessage();
        ChainTestListener.log(log);
    }
}
