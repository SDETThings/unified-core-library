package web.selenium.browserManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class BrowserManager {
    private static volatile BrowserManager webDriverManagerInstance;
    private static ThreadLocal<WebDriver> tlDriver= new ThreadLocal<>();
    private BrowserManager(){
        if(webDriverManagerInstance!=null){
            throw new IllegalArgumentException("Cannot create the object of this private constructor");
        }
    }
    private void setTlDriver(String browserName,boolean isHeadless){
        switch (browserName) {
            case "Chrome" -> {
                ChromeOptions options = new ChromeOptions();
                  configureChromeOptions(options,isHeadless);  // Call to configure Chrome options
                tlDriver.set(new ChromeDriver(options));
            }
            case "Firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                configureFirefoxOptions(options,isHeadless);  // Call to configure Firefox options
                tlDriver.set(new FirefoxDriver(options));
            }
            case "Edge" -> {
                EdgeOptions options = new EdgeOptions();
                configureEdgeOptions(options,isHeadless);  // Call to configure Edge options
                tlDriver.set(new EdgeDriver(options));
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }
    }
    public static BrowserManager getBrowserManagerInstance(String browserName,boolean isHeadless) {
        if(webDriverManagerInstance==null){
            synchronized (BrowserManager.class){
                if(webDriverManagerInstance==null){
                    webDriverManagerInstance = new BrowserManager();
                }
            }
        }
        if(tlDriver.get()==null){
            webDriverManagerInstance.setTlDriver(browserName,isHeadless);
        }
        return webDriverManagerInstance;
    }
    public WebDriver getDriver(){
        return tlDriver.get();
    }
    public static void quitBrowser() {
        if(tlDriver.get()!=null){
            tlDriver.get().quit();
            tlDriver.remove();
        }
    }

    private void configureChromeOptions(ChromeOptions options,boolean isHeadless) {
        if (isHeadless) {
            options.addArguments("--headless"); // Run in headless mode
        }
        options.addArguments("--no-sandbox"); // Bypass OS security model
        options.addArguments("--disable-dev-shm-usage"); // Overcome limited resource problems
        options.addArguments("--disable-gpu"); // Disable GPU (optional)
        options.addArguments("--window-size=1920,1080"); // Set window size
    }

    private void configureFirefoxOptions(FirefoxOptions options,boolean isHeadless) {
        if (isHeadless){
            options.addArguments("--headless"); // Correctly setting headless mode for Firefox
        }
        // Additional Firefox options can be added here
        options.addArguments("--no-sandbox");           // Bypass OS security model (common in CI environments)
        options.addArguments("--disable-gpu");          // Disable GPU (useful for running in headless environments)
        options.addArguments("--disable-dev-shm-usage"); // Overcome limited resource problems
        options.addArguments("--window-size=1920,1080"); // Set window size for headless mode to avoid issues with certain web elements
        options.setCapability("acceptInsecureCerts", true);
    }

    private void configureEdgeOptions(EdgeOptions options,boolean isHeadless) {
        if (isHeadless) {
            options.addArguments("--headless");
            // Run in headless mode
        }
        // Additional Edge options can be added here
        options.addArguments("--no-sandbox");           // Bypass OS security model (similar to Chrome/Firefox)
        options.addArguments("--disable-gpu");          // Disable GPU
        options.addArguments("--disable-dev-shm-usage"); // Overcome limited resource problems
        options.addArguments("--window-size=1920,1080"); // Set window size for headless mode
        options.setCapability("acceptInsecureCerts", true); // Accept insecure certificates if required

    }
}
