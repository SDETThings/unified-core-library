package web.playwright.pageActions;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web.errorHandler.ElementMetaExtractor;
import web.errorHandler.ErrorReporter;


import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;


public class PlaywrightActions {

    private static final Logger log = LogManager.getLogger(PlaywrightActions.class);
    private final int defaultTimeoutSeconds = 10;
    private final int pollingMillis = 500;

    // =================================================================
    // BASIC ELEMENT ACTIONS
    // =================================================================
    public boolean clickElement(Page page, Locator locator) {
        try {
            ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, true);
            return Boolean.TRUE.equals(handler.performActionWithRetry(locator, () -> {
                locator.click();
                return true;
            }, "Click element"));
        } catch (PlaywrightException e) {
            String[] details = ElementMetaExtractor.getElementDetails(locator);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    public boolean doubleClick(Page page, Locator locator) {
        try {
            ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, true);
            return Boolean.TRUE.equals(handler.performActionWithRetry(locator, () -> {
                locator.dblclick();
                return true;
            }, "Double click element"));
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(locator);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    public boolean rightClick(Page page, Locator locator) {
        try {
            ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, true);
            return Boolean.TRUE.equals(handler.performActionWithRetry(locator, () -> {
                locator.click(new Locator.ClickOptions().setButton(MouseButton.RIGHT));
                return true;
            }, "Right click element"));
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(locator);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    public boolean typeText(Page page, Locator locator, String text) {
        try {
            ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, true);
            return Boolean.TRUE.equals(handler.performActionWithRetry(locator, () -> {
                locator.fill(text);
                return true;
            }, "Type text"));
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(locator);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    public String getText(Page page, Locator locator) {
        try {
            ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, true);
            String text = handler.performActionWithRetry(locator, locator::innerText, "Get element text");
            return text;
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(locator);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    public String getAttribute(Page page, Locator locator, String attrName) {
        try {
            ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, true);
            String text = handler.performActionWithRetry(locator, () -> locator.getAttribute(attrName), "Get attribute");
            return text;
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(locator);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    // =================================================================
    // MOUSE & KEYBOARD INTERACTIONS
    // =================================================================
    public boolean hoverElement(Page page, Locator locator) {
        try {
            ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, true);
            return Boolean.TRUE.equals(handler.performActionWithRetry(locator, () -> {
                locator.hover();
                return true;
            }, "Hover element"));
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(locator);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    public boolean dragAndDrop(Page page, Locator source, Locator target) {
        try {
            ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, true);
            return Boolean.TRUE.equals(handler.performActionWithRetry(source, () -> {
                source.dragTo(target);
                return true;
            }, "Drag and drop element"));
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(source);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    public boolean dragByOffset(Page page, Locator locator, int xOffset, int yOffset) {
        try {
            ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, true);
            return Boolean.TRUE.equals(handler.performActionWithRetry(locator, () -> {
                BoundingBox box = locator.boundingBox();
                if (box == null) throw new PlaywrightException("Unable to get bounding box for drag.");
                page.mouse().move(box.x + box.width / 2, box.y + box.height / 2);
                page.mouse().down();
                page.mouse().move(box.x + xOffset, box.y + yOffset);
                page.mouse().up();
                return true;
            }, "Drag by offset"));
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(locator);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    public boolean sliderMove(Page page, Locator slider, int xOffset) {
        try {
            ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, true);
            return Boolean.TRUE.equals(handler.performActionWithRetry(slider, () -> {
                BoundingBox box = slider.boundingBox();
                if (box == null) throw new PlaywrightException("Unable to get slider bounding box.");
                page.mouse().move(box.x + box.width / 2, box.y + box.height / 2);
                page.mouse().down();
                page.mouse().move(box.x + xOffset, box.y + box.height / 2);
                page.mouse().up();
                return true;
            }, "Slider move"));
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(slider);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    public boolean pressKey(Page page, String key) {
        try {
            ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, false);
            return Boolean.TRUE.equals(handler.performActionWithRetry(null, () -> {
                page.keyboard().press(key);
                return true;
            }, "Press key: " + key));
        } catch (Exception e) {
            throw e;
        }
    }

    // =================================================================
    // SCROLL OPERATIONS
    // =================================================================
    public boolean scrollIntoView(Page page, Locator locator) {
        try {
            ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, true);
            return Boolean.TRUE.equals(handler.performActionWithRetry(locator, () -> {
                locator.scrollIntoViewIfNeeded();
                return true;
            }, "Scroll into view"));
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(locator);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    public boolean scrollToTop(Page page) {
        try {
            ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, false);
            return Boolean.TRUE.equals(handler.performActionWithRetry(null, () -> {
                page.evaluate("window.scrollTo(0, 0)");

                return true;
            }, "Scroll to top"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean scrollToBottom(Page page) {
        try {
            ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, false);
            return Boolean.TRUE.equals(handler.performActionWithRetry(null, () -> {
                page.evaluate("window.scrollTo(0, document.body.scrollHeight)");
                return true;
            }, "Scroll to bottom"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // =================================================================
    // FRAME HANDLING
    // =================================================================
    public Frame getFrameByUrl(Page page, String urlSubstring) {
        ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, true);
        return handler.performActionWithRetry(null, () -> {
            for (Frame frame : page.frames()) {
                Object evalResult = frame.evaluate("() => window.location.href");
                String frameUrl = evalResult != null ? evalResult.toString() : "";
                if (frameUrl.contains(urlSubstring)) {
                    log.info("Found frame with URL containing '{}'", urlSubstring);
                    return frame;
                }
            }
            return null;
        }, "Get frame by URL");
    }

    public Frame getFrameByName(Page page, String name) {
        ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, true);
        return handler.performActionWithRetry(null, () -> {
            for (Frame frame : page.frames()) {
                Object evalResult = frame.evaluate("() => window.name");
                String frameName = evalResult != null ? evalResult.toString() : "";
                if (frameName.equalsIgnoreCase(name)) {
                    return frame;
                }
            }
            return null;
        }, "Get frame by name");
    }

    // =================================================================
    // WINDOW / TAB HANDLING
    // =================================================================
    public boolean switchToNewPage(BrowserContext context) {
        ElementActionHandler handler = new ElementActionHandler(context.pages().get(0), defaultTimeoutSeconds, pollingMillis, true);
        return Boolean.TRUE.equals(handler.performActionWithRetry(null, () -> {
            Page newPage = context.waitForPage(() -> {});
            newPage.bringToFront();
            log.info("Switched to new page: {}", newPage.url());
            return true;
        }, "Switch to new page"));
    }

    public boolean switchToParentPage(BrowserContext context) {
        ElementActionHandler handler = new ElementActionHandler(context.pages().get(0), defaultTimeoutSeconds, pollingMillis, true);
        return Boolean.TRUE.equals(handler.performActionWithRetry(null, () -> {
            List<Page> pages = context.pages();
            if (!pages.isEmpty()) {
                pages.get(0).bringToFront();
                log.info("Switched to parent page.");
                return true;
            }
            return false;
        }, "Switch to parent page"));
    }

    public boolean closeAllChildPages(BrowserContext context) {
        ElementActionHandler handler = new ElementActionHandler(context.pages().get(0), defaultTimeoutSeconds, pollingMillis, true);
        return Boolean.TRUE.equals(handler.performActionWithRetry(null, () -> {
            List<Page> pages = context.pages();
            for (int i = 1; i < pages.size(); i++) {
                pages.get(i).close();
            }
            log.info("Closed all child pages.");
            return true;
        }, "Close all child pages"));
    }

    // =================================================================
    // NAVIGATION
    // =================================================================
    public String getCurrentUrl(Page page) {
        ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, true);
        return handler.performActionWithRetry(null, page::url, "Get current URL");
    }

    public String getTitle(Page page) {
        ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, true);
        return handler.performActionWithRetry(null, page::title, "Get page title");
    }

    public boolean navigateBack(Page page) {
        ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, true);
        return Boolean.TRUE.equals(handler.performActionWithRetry(null, () -> {
            page.goBack();
            return true;
        }, "Navigate back"));
    }

    public boolean navigateForward(Page page) {
        ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, true);
        return Boolean.TRUE.equals(handler.performActionWithRetry(null, () -> {
            page.goForward();
            return true;
        }, "Navigate forward"));
    }

    public boolean refreshPage(Page page) {
        ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, true);
        return Boolean.TRUE.equals(handler.performActionWithRetry(null, () -> {
            page.reload();
            return true;
        }, "Refresh page"));
    }

    // =================================================================
    // ALERT / DIALOG HANDLING
    // =================================================================
    public boolean acceptDialog(Page page) {
        ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, true);
        return Boolean.TRUE.equals(handler.performActionWithRetry(null, () -> {
            page.onceDialog(Dialog::accept);
            return true;
        }, "Accept dialog"));
    }

    public boolean dismissDialog(Page page) {
        ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, true);
        return Boolean.TRUE.equals(handler.performActionWithRetry(null, () -> {
            page.onceDialog(Dialog::dismiss);
            return true;
        }, "Dismiss dialog"));
    }

    // =================================================================
    // SCREENSHOTS & TRACE
    // =================================================================
    public String takeScreenshot(Page page, String filename, String path, boolean fullPage) {
        ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, true);
        return handler.performActionWithRetry(null, () -> {
            String dateName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String destination = path + "/" + filename + "_" + dateName + ".png";
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(destination)).setFullPage(fullPage));
            log.info("Screenshot captured at {}", destination);
            return destination;
        }, "Take screenshot");
    }

    public String takeElementScreenshot(Page page, Locator locator, String filename, String path) {
        ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, true);
        return handler.performActionWithRetry(locator, () -> {
            String dateName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String destination = path + "/" + filename + "_" + dateName + ".png";
            locator.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get(destination)));
            log.info("Element screenshot captured at {}", destination);
            return destination;
        }, "Take element screenshot");
    }

    public void startTrace(BrowserContext context) {
        ElementActionHandler handler = new ElementActionHandler(context.pages().get(0), defaultTimeoutSeconds, pollingMillis, false);
        handler.performActionWithRetry(null, () -> {
            context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(true));
            log.info("Tracing started");
            return true;
        }, "Start trace");
    }

    public void stopTrace(BrowserContext context, String tracePath) {
        ElementActionHandler handler = new ElementActionHandler(context.pages().get(0), defaultTimeoutSeconds, pollingMillis, false);
        handler.performActionWithRetry(null, () -> {
            context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get(tracePath)));
            log.info("Trace saved to {}", tracePath);
            return true;
        }, "Stop trace");
    }

    // =================================================================
    // GENERIC TABLE UTILITIES
    // =================================================================
    public int getRowCount(Page page, Locator table) {
        try {
            ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, true);
            return handler.performActionWithRetry(table, () -> table.locator("tr").count(), "Get table row count");
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(table);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    public int getColumnCount(Page page, Locator row) {
        try {
            ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, true);
            return handler.performActionWithRetry(row, () -> row.locator("td,th").count(), "Get column count");
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(row);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    public String getCellText(Page page, Locator table, int rowIndex, int colIndex) {
        try {
            ElementActionHandler handler = new ElementActionHandler(page, defaultTimeoutSeconds, pollingMillis, true);
            return handler.performActionWithRetry(table, () -> {
                Locator cell = table.locator("tr").nth(rowIndex).locator("td,th").nth(colIndex);
                return cell.innerText();
            }, "Get cell text");
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(table);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }
}
