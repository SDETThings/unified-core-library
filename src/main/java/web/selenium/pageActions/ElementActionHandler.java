package web.selenium.pageActions;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;

public class ElementActionHandler {

    private static final Logger log = LogManager.getLogger(ElementActionHandler.class);
    private final WebDriver driver;
    private final JavascriptExecutor js;
    private final int defaultTimeoutSeconds;
    private final int pollingMillis;

    public ElementActionHandler(WebDriver driver) {
        this(driver, 10, 500);
    }

    public ElementActionHandler(WebDriver driver, int timeoutSeconds, int pollingMillis) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        this.defaultTimeoutSeconds = timeoutSeconds;
        this.pollingMillis = pollingMillis;
    }

    /**
     * Generic retryable action which returns a value.
     * @param element target WebElement
     * @param action  lambda performing the actual work
     * @param actionName descriptive name for logs
     * @param <T> return type (e.g. String, Boolean, Integer)
     * @return result from action, or null if failed after retries
     */
    public <T> T performActionWithRetry(WebElement element, Supplier<T> action, String actionName) {
        int maxAttempts = 2;
        int attempt = 0;
        T result = null;

        while (attempt < maxAttempts) {
            try {
                attempt++;
                log.info("Attempt {} to perform action: {}", attempt, actionName);

                // 1️⃣ Smart Wait
                WebElement readyElement = waitForElement(element, defaultTimeoutSeconds);
                if (readyElement == null)
                    throw new TimeoutException("Element not ready after wait: " + describe(element));

                // 2️⃣ Interactability
                if (!isElementInteractable(readyElement))
                    throw new ElementNotInteractableException("Element not interactable: " + describe(element));

                // 3️⃣ Scroll & highlight
                scrollIntoViewAndHighlight(readyElement);

                // 4️⃣ Perform the action and capture return value
                result = action.get();

                log.info("{} succeeded on attempt {}", actionName, attempt);
                return result; // ✅ success — return immediately

            } catch (Exception e) {
                log.warn("{} failed on attempt {}: {}", actionName, attempt, e.getMessage());
                if (attempt >= maxAttempts) {
                    log.error("Action '{}' failed after {} attempts", actionName, maxAttempts);
                    return null;
                }
                sleep(1000);
            }
        }
        return result;
    }

    private WebElement waitForElement(WebElement element, int timeoutSeconds) {
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(timeoutSeconds))
                    .pollingEvery(Duration.ofMillis(pollingMillis))
                    .ignoring(NoSuchElementException.class)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(ElementNotInteractableException.class);

            return wait.until(driver -> {
                try {
                    if (element.isDisplayed() && element.isEnabled()) {
                        return element;
                    }
                    return null;
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            });
        } catch (TimeoutException te) {
            log.warn("Timeout waiting for element: {}", te.getMessage());
            return null;
        }
    }

    private boolean isElementInteractable(WebElement ele) {
        try {
            return ele.isEnabled() && ele.getSize().getHeight() > 0 && ele.getSize().getWidth() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private void scrollIntoViewAndHighlight(WebElement ele) {
        try {
            js.executeScript("arguments[0].scrollIntoView({ behavior: 'smooth', block: 'center' });", ele);
            js.executeScript("""
                arguments[0].style.outline='2px solid #00ff00';
                arguments[0].style.backgroundColor='rgba(255,255,0,0.3)';
            """, ele);
        } catch (Exception e) {
            log.warn("Scroll/Highlight failed: {}", e.getMessage());
        }
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }

    private String describe(WebElement ele) {
        try {
            return ele.toString();
        } catch (Exception e) {
            return "Unknown Element";
        }
    }
}
