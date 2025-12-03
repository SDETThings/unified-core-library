package unifiedReports.webLogger;

import com.aventstack.chaintest.plugins.ChainTestListener;
import com.microsoft.playwright.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import unifiedReports.requestLogger.APIConsoleLogger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class WebConsoleLogger {
    private static final WebConsoleLogger INSTANCE = new WebConsoleLogger();
    private static final Logger logger = LogManager.getLogger(WebConsoleLogger.class);

    private WebConsoleLogger() {
    }

    public static WebConsoleLogger getInstance() {
        return INSTANCE;
    }

    /**
     * Logs an error message with stack trace.
     */
    public static void logError(String message, Throwable error) {
        String log = message + "\n\nError: " + error.getMessage();
        logger.error(log, error);
    }

    /**
     * Logs a step message in console
     * Works for both Selenium WebDriver and Playwright Page objects.        *
     * @param message - Description of the action performed.
     */
    public void logToConsole(boolean isStepPassed, String message,Exception... e) {
        StringBuilder log = new StringBuilder();
        if(isStepPassed) {
            log.append("✅ ").append(message);
            logInfo(log.toString());
        } else {
            log.append("❌ ").append(message);
            logError(log.toString(),e[0]);
        }
    }
    /**
     * Logs only a text step without screenshot.
     */
    public void logInfo(String message) {
        logger.info(message);
    }
}
