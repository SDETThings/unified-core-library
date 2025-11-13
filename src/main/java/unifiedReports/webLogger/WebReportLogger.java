package unifiedReports.webLogger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import unifiedReports.requestLogger.APIReportLogger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
     *
     * @param stepDescription   Description of the action performed.
     * @param driverOrPage      WebDriver (Selenium) or Page (Playwright) object.
     * @param captureScreenshot Whether to attach a screenshot to the report.
     */
    public static void logStep(String stepDescription, Object driverOrPage,Status status, boolean... captureScreenshot) {
        // log the textual step
        Allure.step(stepDescription,status);

        // capture screenshot if required
        if (captureScreenshot[0] && driverOrPage != null) {
            try {
                byte[] screenshotBytes = captureScreenshot(driverOrPage);
                if (screenshotBytes != null) {
                    Allure.addAttachment("Screenshot - " + stepDescription, "image/png", new ByteArrayInputStream(screenshotBytes), ".png");
                }
            } catch (Exception e) {
                Allure.step("Failed to log step: " + e.getMessage(), Status.BROKEN);
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
        Allure.addAttachment("Info", "text/plain", new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8)), ".txt");
    }

    /**
     * Logs an error message with stack trace.
     */
    public static void logError(String message, Throwable error) {
        String log = message + "\n\nError: " + error.getMessage();
        Allure.addAttachment("Error Log", "text/plain",
                new ByteArrayInputStream(log.getBytes(StandardCharsets.UTF_8)), ".txt");
    }
}
