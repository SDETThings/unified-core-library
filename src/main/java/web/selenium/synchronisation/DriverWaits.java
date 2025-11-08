package web.selenium.synchronisation;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.Function;

public class DriverWaits {
    private WebDriver driver;
    DriverWaits waits;
    private static ThreadLocal<WebDriverWait> wait = new ThreadLocal<>();
    public static void initializeWait(WebDriver driver, int timeoutSeconds, int pollingIntervalSeconds) {
        wait.set(new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds), Duration.ofMillis(pollingIntervalSeconds)));
    }
    public static WebDriverWait getWait() {
        return wait.get();
    }

    public <T> T waitForCondition(Function<WebDriver, T> condition, int timeoutSeconds, int pollingIntervalSeconds) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofSeconds(pollingIntervalSeconds))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementNotInteractableException.class);

        return wait.until(condition);
    }

    // Wait for element to be visible
    public WebElement waitForElementToBeVisible(WebElement element, int timeoutSeconds, int pollingIntervalSeconds) {
        return waitForCondition(webDriver -> element.isDisplayed() ? element : null, timeoutSeconds, pollingIntervalSeconds);
    }
    // Wait for element to be clickable
    public WebElement waitForElementToBeClickable(WebElement element, int timeoutSeconds, int pollingIntervalSeconds) {
        return waitForCondition(webDriver -> (element.isDisplayed() && element.isEnabled()) ? element : null, timeoutSeconds, pollingIntervalSeconds);
    }
    // Wait for element to have specific text
    public WebElement waitForElementToHaveText(WebElement element, String expectedText, int timeoutSeconds, int pollingIntervalSeconds) {
        return waitForCondition(webDriver -> element.getText().equals(expectedText) ? element : null, timeoutSeconds, pollingIntervalSeconds);
    }
    // Wait for element to have a specific attribute with a given value
    public WebElement waitForElementToHaveAttribute(WebElement element, String attribute, String expectedValue, int timeoutSeconds, int pollingIntervalSeconds) {
        return waitForCondition(webDriver -> {
            String actualValue = element.getAttribute(attribute);
            return actualValue != null && actualValue.equals(expectedValue) ? element : null;
        }, timeoutSeconds, pollingIntervalSeconds);
    }
    // Wait for element to be enabled
    public WebElement waitForElementToBeEnabled(WebElement element, int timeoutSeconds, int pollingIntervalSeconds) {
        return waitForCondition(webDriver -> element.isEnabled() ? element : null, timeoutSeconds, pollingIntervalSeconds);
    }
    // Wait for element to be selected (for checkboxes,  radio buttons)
    public WebElement waitForElementToBeSelected(WebElement element, int timeoutSeconds, int pollingIntervalSeconds) {
        return waitForCondition(webDriver -> element.isSelected() ? element : null, timeoutSeconds, pollingIntervalSeconds);
    }
}
