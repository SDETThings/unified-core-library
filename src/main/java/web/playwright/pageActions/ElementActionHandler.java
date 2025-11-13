package web.playwright.pageActions;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.function.Supplier;

/**
 * PlaywrightActionHandler — modular utility to manage safe, retryable element actions.
 * Mirrors the Selenium ElementActionHandler but adapted for Playwright.
 */
public class ElementActionHandler {

    private static final Logger log = LogManager.getLogger(ElementActionHandler.class);

    private final Page page;
    private final int defaultTimeoutSeconds;
    private final int pollingMillis;
    private final boolean highlight;

    public ElementActionHandler(Page page) {
        this(page, 10, 500, false);
    }

    public ElementActionHandler(Page page, int timeoutSeconds, int pollingMillis, boolean highlight) {
        this.page = page;
        this.defaultTimeoutSeconds = timeoutSeconds;
        this.pollingMillis = pollingMillis;
        this.highlight = highlight;
    }

    /**
     * Generic retryable action which returns a value.
     *
     * @param locator target Playwright Locator
     * @param action  lambda performing the actual work
     * @param actionName descriptive name for logs
     * @param <T> return type (e.g. String, Boolean, Integer)
     * @return result from action, or null if failed after retries
     */
    public <T> T performActionWithRetry(Locator locator, Supplier<T> action, String actionName,boolean... skipElementReadinessCheck) {
        int maxAttempts = 2;
        int attempt = 0;
        T result = null;

        while (attempt < maxAttempts) {
            try {
                attempt++;
                log.info("Attempt {} to perform action: {}", attempt, actionName);

                if(skipElementReadinessCheck.length==0) {
                    // 1️⃣ Smart Wait
                    waitForElementToBeReady(locator);
                }

                // 2️⃣ Scroll & highlight
                scrollIntoViewAndHighlight(locator);

                // 3️⃣ Perform the action and capture return value
                result = action.get();

                log.info("{} succeeded on attempt {}", actionName, attempt);
                return result; // ✅ success — return immediately

            } catch (PlaywrightException e) {
                log.warn("{} failed on attempt {}: {}", actionName, attempt, e.getMessage());
                if (attempt >= maxAttempts) {
                    log.error("Action '{}' failed after {} attempts", actionName, maxAttempts);
                    return null;
                }
                sleep(1000);
            } catch (Exception e) {
                log.warn("{} failed unexpectedly on attempt {}: {}", actionName, attempt, e.getMessage());
                if (attempt >= maxAttempts) {
                    log.error("Action '{}' failed after {} attempts", actionName, maxAttempts);
                    return null;
                }
                sleep(1000);
            }
        }
        return result;
    }

    /**
     * Wait until the locator is visible and enabled.
     */
    private void waitForElementToBeReady(Locator locator,boolean... skipElementReadinessCheck) {
        if (locator == null) {
            throw new PlaywrightException("Locator is null");
        }
        // 1️⃣ Wait for element to be attached to the DOM
        locator.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.ATTACHED)
                .setTimeout(defaultTimeoutSeconds * 1000));

        // 2️⃣ Wait for element to be visible
        locator.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(defaultTimeoutSeconds * 1000));

        // 3️⃣ Additional check for enabled state (Playwright doesn’t have direct “enabled” wait)
        if (!locator.isEnabled()) {
            long endTime = System.currentTimeMillis() + Duration.ofSeconds(defaultTimeoutSeconds).toMillis();
            while (System.currentTimeMillis() < endTime) {
                if (locator.isEnabled()) return;
                page.waitForTimeout(pollingMillis); // soft polling while visible
            }
            throw new PlaywrightException("Element visible but not enabled within timeout: " + describe(locator));
        }

        throw new PlaywrightException("Timeout waiting for element to be ready: " + describe(locator));
    }

    /**
     * Scroll element into view and highlight if enabled.
     */
    private void scrollIntoViewAndHighlight(Locator locator) {
        try {
            locator.scrollIntoViewIfNeeded();
            if (highlight) {
                locator.evaluate("""
                    el => {
                        el.style.outline='2px solid #00ff00';
                        el.style.backgroundColor='rgba(255,255,0,0.3)';
                    }
                """);
            }
        } catch (Exception e) {
            log.warn("Scroll/Highlight failed: {}", e.getMessage());
        }
    }

    private void sleep(long ms) {
        try {
            page.waitForTimeout(ms);  // ✅ Playwright’s async-friendly wait
        } catch (PlaywrightException e) {
            log.warn("Wait interrupted: {}", e.getMessage());
        }
    }

    private String describe(Locator locator) {
        try {
            return locator.toString();
        } catch (Exception e) {
            return "Unknown Locator";
        }
    }
}
