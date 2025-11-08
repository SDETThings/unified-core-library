package web.selenium.synchronisation;

import org.openqa.selenium.support.ui.ExpectedConditions;
import web.selenium.pageActions.DriverActions;

public class DriverActionsFactory {
    private static ThreadLocal<DriverActions> instance = ThreadLocal.withInitial(() -> new DriverActions(
            (driver, element, timeoutSeconds, pollingIntervalSeconds) -> {
                // Used to set an object of selenium WebDriverWait class ( selenium inbuilt)
                DriverWaits.initializeWait(driver, timeoutSeconds, pollingIntervalSeconds);
                // Used to get the object of selenium WebDriverWait class ( selenium inbuilt)
                return DriverWaits.getWait().until(ExpectedConditions.visibilityOf(element)) != null;
            }
    ));

    // Get the singleton instance for each thread
    public static DriverActions getInstance() {
        return instance.get();
    }
}
