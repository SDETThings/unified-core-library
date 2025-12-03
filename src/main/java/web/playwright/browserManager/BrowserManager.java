package web.playwright.browserManager;

import com.microsoft.playwright.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class BrowserManager {

    private static volatile BrowserManager instance;
    private static ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
    private static ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
    private static ThreadLocal<BrowserContext> tlContext = new ThreadLocal<>();
    private static ThreadLocal<Page> tlPage = new ThreadLocal<>();

    private BrowserManager() {
        if (instance != null) {
            throw new IllegalArgumentException("Cannot create another instance of BrowserManager");
        }
    }

    public static BrowserManager getInstance(String browserName, boolean isHeadless) {
        if (Objects.isNull(instance)) {
            synchronized (BrowserManager.class) {
                if (Objects.isNull(instance)) {
                    instance = new BrowserManager();
                }
            }
        }
        if (Objects.isNull(tlPlaywright.get())) {
            instance.initPlaywright(browserName, isHeadless);
        }
        return instance;
    }

    private void initPlaywright(String browserName, boolean isHeadless) {
        tlPlaywright.set(Playwright.create());
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setChannel(browserName).setHeadless(isHeadless);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        switch (browserName.toLowerCase()) {
            case "chrome", "chromium" -> tlBrowser.set(tlPlaywright.get().chromium().launch(options.setArgs(new ArrayList<>(Collections.singleton("--start-maximized")))));
            case "firefox" -> tlBrowser.set(tlPlaywright.get().firefox().launch(options.setArgs(new ArrayList<>(Collections.singleton("--start-maximized")))));
            case "webkit", "safari" -> tlBrowser.set(tlPlaywright.get().webkit().launch(options.setArgs(new ArrayList<>(Collections.singleton("--start-maximized")))));
            case "msedge", "edge" -> {
                options.setChannel("msedge");
                tlBrowser.set(tlPlaywright.get().chromium().launch(options));
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }

        tlContext.set(tlBrowser.get().newContext(
                new Browser.NewContextOptions()
                        .setViewportSize(null)
                        .setScreenSize((int) screenSize.getWidth(),(int) screenSize.getHeight())
                        .setIgnoreHTTPSErrors(true)
        ));
        tlPage.set(tlContext.get().newPage());
    }

    public Page getPage() {
        return tlPage.get();
    }

    public BrowserContext getContext() {
        return tlContext.get();
    }

    public Browser getBrowser() {
        return tlBrowser.get();
    }

    public Playwright getPlaywright() {
        return tlPlaywright.get();
    }

    public static void closeBrowser() {
        if (tlPage.get() != null) {
            tlPage.get().close();
        }
        if (tlContext.get() != null) {
            tlContext.get().close();
        }
        if (tlBrowser.get() != null) {
            tlBrowser.get().close();
        }
        if (tlPlaywright.get() != null) {
            tlPlaywright.get().close();
        }

        tlPage.remove();
        tlContext.remove();
        tlBrowser.remove();
        tlPlaywright.remove();
    }
}
