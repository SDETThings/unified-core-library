package web.selenium.synchronisation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@FunctionalInterface
public interface WebDriverWaits {
    boolean apply(WebDriver driver, WebElement element, int timeoutSeconds, int pollingIntervalSeconds) throws Exception;
}

