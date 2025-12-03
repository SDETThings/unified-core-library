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
        this(page, 60, 500, false);
    }

    public ElementActionHandler(Page page, int timeoutSeconds, int pollingMillis, boolean highlight) {
        this.page = page;
        this.defaultTimeoutSeconds = timeoutSeconds;
        this.pollingMillis = pollingMillis;
        this.highlight = highlight;
    }

    /**
     * Generic retryable action which returns a value.
     * @param locator target Playwright Locator
     * @param action  lambda performing the actual work
     * @param actionName descriptive name for logs
     * @param <T> return type (e.g. String, Boolean, Integer)
     * @return result from action, or null if failed after retries
     */
    public <T> T performActionWithRetry(Locator locator, Supplier<T> action, String actionName,boolean... skipElementReadinessCheck) {
        int maxAttempts = 1;
        int attempt = 0;
        T result = null;
        String locatorDescription = null;
        if(locator !=null)
        {
            locatorDescription = locator.toString().split("//")[1];
        }
        while (attempt < maxAttempts) {
            try {
                attempt++;
                if(locatorDescription!=null)
                {
                    log.info("=========================================================================");
                    log.info("Attempt {} to perform action: {} on {}", attempt, actionName,locatorDescription);
                }else{
                    log.info("Attempt {} to perform action: {} on locator", attempt, actionName);
                }
                if(skipElementReadinessCheck.length==0) {
                    // 1️⃣ Smart Wait
                    waitForElementToBeReady(locator);
                }

                // 2️⃣ Scroll & highlight
                if(highlight)
                {
                    //scrollIntoViewAndHighlight(locator);
                }

                // 3️⃣ Perform the action and capture return value
                result = action.get();
                if(locatorDescription!=null)
                {
                    log.info("{} succeeded on locator {} attempt {}", actionName, locatorDescription, attempt);
                    log.info("=========================================================================");
                }else{
                    log.info("{} succeeded on locator attempt {}", actionName, attempt);
                    log.info("=========================================================================");
                }
                return result; // ✅ success — return immediately

            } catch (PlaywrightException e) {
                if(locatorDescription!=null)
                {
                    log.warn("{} failed on locator {} for attempt {}: {}", actionName,locatorDescription, attempt, e.getMessage());
                }else{
                    log.warn("{} failed on locator for attempt {}: {}", actionName, attempt, e.getMessage());
                }
                if (attempt >= maxAttempts) {
                    if(locatorDescription!=null)
                    {
                        log.error("Action '{}' on locator {} failed after {} attempts", actionName,locatorDescription, maxAttempts);                    log.info("=========================================================================");
                        log.info("=========================================================================");
                    }else{
                        log.error("Action '{}' on locator failed after {} attempts", actionName, maxAttempts);
                        log.info("=========================================================================");
                    }
                    return null;
                }
                sleep(1000);
            } catch (Exception e) {
                if(locatorDescription!=null)
                {
                    log.warn("{} on locator {} failed unexpectedly on attempt {}: {}", actionName,locatorDescription, attempt, e.getMessage());
                }else{
                    log.warn("{} on locator failed unexpectedly on attempt {}: {}", actionName, attempt, e.getMessage());
                }
                if (attempt >= maxAttempts) {
                    if(locatorDescription!=null)
                    {
                        log.error("Action '{}' failed on locator : {} after {} attempts", actionName,locatorDescription, maxAttempts);
                    }else {
                        log.error("Action '{}' failed on locator after {} attempts", actionName, maxAttempts);
                    }
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
    private void waitForElementToBeReady(Locator locator) {
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
    }

    /**
     * Scroll element into view and highlight if enabled.
     */
    /*private void scrollIntoViewAndHighlight(Locator locator) {
        try {
            //locator.scrollIntoViewIfNeeded();
            // Scroll using JS (more stable than scrollIntoViewIfNeeded)
            locator.evaluate("el => el.scrollIntoView({ block: 'center', inline: 'center' })");
            if (highlight) {
                page.evaluate("""
            (el, duration) => {
                try {
                    const r = el.getBoundingClientRect();
                    const overlay = document.createElement('div');
                    overlay.className = 'pw-overlay-highlight';
                    Object.assign(overlay.style, {
                        position: 'absolute',
                        left: (window.scrollX + r.left) + 'px',
                        top:  (window.scrollY + r.top) + 'px',
                        width: r.width + 'px',
                        height: r.height + 'px',
                        borderRadius: '4px',
                        boxSizing: 'border-box',
                        boxShadow: '0 0 0 3px rgba(0,255,0,0.6)',
                        pointerEvents: 'none',
                        zIndex: 2147483647,
                        transition: 'opacity 120ms ease-in-out',
                        opacity: '1'
                    });
                    document.body.appendChild(overlay);
                    setTimeout(() => {
                        overlay.style.opacity = '0';
                        setTimeout(() => overlay.remove(), 150);
                    }, duration || 300);
                } catch (e) {
                    // swallow errors inside page context
                }
            }
        """, locator);
            }
        } catch (Exception e) {
            log.warn("Scroll/Highlight failed: {}", e.getMessage());
        }
    }*/

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
