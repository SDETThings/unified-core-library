package web.selenium.pageActions;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import web.errorHandler.ElementMetaExtractor;
import web.errorHandler.ErrorReporter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class DriverActions  {
    private static final Logger log = LogManager.getLogger(DriverActions.class);

    // **************************************************************************************************
    //                               --- Common  WEB ELEMENT Interactions ---
    // **************************************************************************************************
    public boolean clickOnElement(WebDriver driver, WebElement ele)  {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            Boolean result = handler.performActionWithRetry(ele, () -> {
                ele.click();
                return true; // must return something
            }, "Click on Element");
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(ele);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }
    public boolean clickOnElementUsingJavaScript(WebDriver driver, WebElement ele) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            Boolean result = handler.performActionWithRetry(ele, () -> {
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeScript("arguments[0].click();", ele);
                return true; // must return something
            }, "Click on Element using java script");
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(ele);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }
    public boolean type(WebDriver driver, WebElement ele,String text) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            Boolean result = handler.performActionWithRetry(ele, () -> {
                ele.sendKeys(text);
                return true; // must return something
            }, "Enter text");
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(ele);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }
    public String getText(WebDriver driver, WebElement ele) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            return handler.performActionWithRetry(ele, ele::getText,"Get text from element");
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(ele);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }
    public boolean clearText(WebDriver driver,WebElement ele) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            Boolean isCleared =  handler.performActionWithRetry(ele, ()->{
                ele.clear();
                return true;
            },"Clear text");
            return Boolean.TRUE.equals(isCleared);
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(ele);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }
    public String getAttribute(WebDriver driver,String attributeKey,WebElement ele) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            return handler.performActionWithRetry(ele, ()-> ele.getAttribute(attributeKey),"Get attribute value from html tag");
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(ele);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }
    public boolean submit(WebDriver driver,WebElement ele){
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            Boolean result = handler.performActionWithRetry(ele, () -> {
                ele.submit();
                return true; // must return something
            }, "Click on Element");
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(ele);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    // **************************************************************************************************
    //                                  --- Verify WEB ELEMENT States ---
    // **************************************************************************************************

    public boolean isDisplayed(WebDriver driver, WebElement ele) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            Boolean result = handler.performActionWithRetry(ele, () -> {
                ele.isDisplayed();
                return true; // must return something
            }, "Click on Element");
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(ele);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }
    public boolean isSelected(WebDriver driver, WebElement ele) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            Boolean result = handler.performActionWithRetry(ele, () -> {
                ele.isSelected();
                return true; // must return something
            }, "Click on Element");
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(ele);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }
    public boolean isEnabled(WebDriver driver, WebElement ele) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            Boolean result = handler.performActionWithRetry(ele, () -> {
                ele.isEnabled();
                return true; // must return something
            }, "Click on Element");
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(ele);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    // **************************************************************************************************
    //                                  --- Dropdown / Select Actions ---
    // **************************************************************************************************
    public boolean selectByIndex(WebDriver driver,WebElement ele, int index) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            Boolean result = handler.performActionWithRetry(ele, () -> {
                Select s = new Select(ele);
                s.selectByIndex(index);
                return true; // must return something
            }, "Select dropdown option: "+index);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(ele);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }
    public boolean selectByValue(WebDriver driver, WebElement ele, String value) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            Boolean result = handler.performActionWithRetry(ele, () -> {
                Select s = new Select(ele);
                s.selectByValue(value);
                return true; // must return something
            }, "Select dropdown option: "+value);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(ele);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }
    public boolean selectByVisibleText(WebDriver driver,String visibletext, WebElement ele) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            Boolean result = handler.performActionWithRetry(ele, () -> {
                Select s = new Select(ele);
                s.selectByVisibleText(visibletext);
                return true; // must return something
            }, "Select dropdown option: "+visibletext);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(ele);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }
    public List<WebElement> getAllDropdownOptions(WebDriver driver, String visibletext, WebElement ele) {
        List<WebElement> result = null;
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            result = handler.performActionWithRetry(ele, () -> {
                Select s = new Select(ele);
                s.getOptions();
                return null;
            }, "Get all dropdown options");
            return result;
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(ele);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }
    // **************************************************************************************************
    //                                  --- Mouse / Keyboard Actions ---
    // **************************************************************************************************
    // ✅ Mouse hover on element
    public boolean mouseHover(WebDriver driver, WebElement ele) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            Boolean result = handler.performActionWithRetry(ele, () -> {
                new Actions(driver)
                        .moveToElement(ele)
                        .perform();
                return true;
            }, "Mouse hover on element");
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(ele);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;        }
    }

    // ✅ Drag from source to target
    public boolean dragAndDrop(WebDriver driver, WebElement source, WebElement target) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            Boolean result = handler.performActionWithRetry(source, () -> {
                new Actions(driver)
                        .dragAndDrop(source, target)
                        .perform();
                return true;
            }, "Drag and drop from source to target");
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(source);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    // ✅ Slider (basically drag by small offset)
    public boolean slider(WebDriver driver, WebElement ele, int x, int y) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            Boolean result = handler.performActionWithRetry(ele, () -> {
                new Actions(driver)
                        .dragAndDropBy(ele, x, y)
                        .perform();
                return true;
            }, "Slide element by offset (" + x + "," + y + ")");
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(ele);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    // ✅ Right click on element
    public boolean rightClick(WebDriver driver, WebElement ele) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            Boolean result = handler.performActionWithRetry(ele, () -> {
                new Actions(driver)
                        .contextClick(ele)
                        .perform();
                return true;
            }, "Right click on element");
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(ele);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    // ✅ Double click on element
    public boolean doubleClick(WebDriver driver, WebElement ele) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            Boolean result = handler.performActionWithRetry(ele, () -> {
                new Actions(driver)
                        .doubleClick(ele)
                        .perform();
                return true;
            }, "Double click on element");
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(ele);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    // ✅ Press custom key (example: TAB, ENTER, etc.)
    public boolean pressCustomKey(WebDriver driver, Keys key) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            Boolean result = handler.performActionWithRetry(null, () -> {
                new Actions(driver)
                        .sendKeys(key)
                        .perform();
                return true;
            }, "Press custom key: " + key.name());
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // **************************************************************************************************
    //                                  --- Scrolling & JS Helpers ---
    // **************************************************************************************************
// ✅ Scroll element into view using default behavior
    public boolean scrollByVisibilityOfElement(WebDriver driver, WebElement ele) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            Boolean result = handler.performActionWithRetry(ele, () -> {
                ((JavascriptExecutor) driver)
                        .executeScript("arguments[0].scrollIntoView({ behavior: 'smooth', block: 'center' });", ele);
                return true;
            }, "Scroll element into view (by visibility)");
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(ele);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    // ✅ Scroll element into view & highlight
    public boolean scrollIntoViewAndHighlight(WebDriver driver, WebElement ele) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            Boolean result = handler.performActionWithRetry(ele, () -> {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].scrollIntoView({ behavior: 'smooth', block: 'center' });", ele);
                js.executeScript("""
                    arguments[0].style.border='3px solid #00ff00';
                    arguments[0].style.backgroundColor='rgba(255,255,0,0.3)';
                """, ele);
                return true;
            }, "Scroll into view and highlight element");
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(ele);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    // ✅ Scroll element into view without highlighting
    public boolean scrollIntoView(WebDriver driver, WebElement ele) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            Boolean result = handler.performActionWithRetry(ele, () -> {
                ((JavascriptExecutor) driver)
                        .executeScript("arguments[0].scrollIntoView({ behavior: 'smooth', block: 'center' });", ele);
                return true;
            }, "Scroll element into view");
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(ele);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    // ✅ Remove highlight from element
    public boolean unhighlight(WebDriver driver, WebElement ele) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            Boolean result = handler.performActionWithRetry(ele, () -> {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                String originalStyle = (String) js.executeScript("return arguments[0].getAttribute('style');", ele);
                js.executeScript("arguments[0].setAttribute('style', arguments[1]);", ele, originalStyle);
                return true;
            }, "Unhighlight element");
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(ele);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    // ✅ Scroll entire page to bottom
    public boolean scrollToBottom(WebDriver driver) {
        ElementActionHandler handler = new ElementActionHandler(driver);
        Boolean result = handler.performActionWithRetry(null, () -> {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo({ top: document.body.scrollHeight, behavior: 'smooth' });");
            return true;
        }, "Scroll to bottom of page");
        return Boolean.TRUE.equals(result);
    }

    // ✅ Scroll entire page to top
    public boolean scrollToTop(WebDriver driver) {
        ElementActionHandler handler = new ElementActionHandler(driver);
        Boolean result = handler.performActionWithRetry(null, () -> {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo({ top: 0, behavior: 'smooth' });");
            return true;
        }, "Scroll to top of page");
        return Boolean.TRUE.equals(result);
    }

    // **************************************************************************************************
    //                                  --- Frame & Window Handling ---
    // **************************************************************************************************
    // ✅ Switch to frame using frame ID
    public boolean switchToFrameById(WebDriver driver, String idValue) {
        ElementActionHandler handler = new ElementActionHandler(driver);
        Boolean result = handler.performActionWithRetry(null, () -> {
            driver.switchTo().frame(idValue);
            return true;
        }, "Switch to frame by ID: " + idValue);
        return Boolean.TRUE.equals(result);
    }

    // ✅ Switch to frame using frame Name
    public boolean switchToFrameByName(WebDriver driver, String nameValue) {
        ElementActionHandler handler = new ElementActionHandler(driver);
        Boolean result = handler.performActionWithRetry(null, () -> {
            driver.switchTo().frame(nameValue);
            return true;
        }, "Switch to frame by Name: " + nameValue);
        return Boolean.TRUE.equals(result);
    }

    // ✅ Switch back to default content
    public boolean switchToDefaultFrame(WebDriver driver) {
        ElementActionHandler handler = new ElementActionHandler(driver);
        Boolean result = handler.performActionWithRetry(null, () -> {
            driver.switchTo().defaultContent();
            return true;
        }, "Switch to default frame");
        return Boolean.TRUE.equals(result);
    }

    // ✅ Switch to a specific window by title
    public boolean switchWindowByTitle(WebDriver driver, String windowTitle) {
        ElementActionHandler handler = new ElementActionHandler(driver);
        Boolean result = handler.performActionWithRetry(null, () -> {
            Set<String> handles = driver.getWindowHandles();
            for (String handle : handles) {
                driver.switchTo().window(handle);
                if (driver.getTitle().contains(windowTitle)) {
                    return true;
                }
            }
            return false; // Title not found
        }, "Switch to window by title: " + windowTitle);
        return Boolean.TRUE.equals(result);
    }

    // ✅ Switch to newly opened window (last handle)
    public boolean switchToNewWindow(WebDriver driver) {
        ElementActionHandler handler = new ElementActionHandler(driver);
        Boolean result = handler.performActionWithRetry(null, () -> {
            Set<String> handles = driver.getWindowHandles();
            String[] windows = handles.toArray(new String[0]);
            if (windows.length > 1) {
                driver.switchTo().window(windows[windows.length - 1]);
                return true;
            }
            return false;
        }, "Switch to new (latest) window");
        return Boolean.TRUE.equals(result);
    }

    // ✅ Switch to parent window (first handle)
    public boolean switchToParentWindow(WebDriver driver) {
        ElementActionHandler handler = new ElementActionHandler(driver);
        Boolean result = handler.performActionWithRetry(null, () -> {
            Set<String> handles = driver.getWindowHandles();
            String[] windows = handles.toArray(new String[0]);
            driver.switchTo().window(windows[0]);
            return true;
        }, "Switch to parent (first) window");
        return Boolean.TRUE.equals(result);
    }

    // ✅ Close all child windows and return to parent
    public boolean closeAllChildWindows(WebDriver driver) {
        ElementActionHandler handler = new ElementActionHandler(driver);
        Boolean result = handler.performActionWithRetry(null, () -> {
            String parentHandle = driver.getWindowHandle();
            Set<String> handles = driver.getWindowHandles();
            for (String handle : handles) {
                if (!handle.equals(parentHandle)) {
                    driver.switchTo().window(handle);
                    driver.close();
                }
            }
            driver.switchTo().window(parentHandle);
            return true;
        }, "Close all child windows and return to parent");
        return Boolean.TRUE.equals(result);
    }

    // **************************************************************************************************
    //                                  --- Alert Handling ---
    // **************************************************************************************************
    // ✅ Wait until alert is present (internal helper)
    private Alert waitForAlert(WebDriver driver, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return wait.until(ExpectedConditions.alertIsPresent());
        } catch (TimeoutException e) {
            return null;
        }
    }

    // ✅ Accept alert if present
    public boolean acceptAlert(WebDriver driver) {
        ElementActionHandler handler = new ElementActionHandler(driver);
        Boolean result = handler.performActionWithRetry(null, () -> {
            Alert alert = waitForAlert(driver, 5);
            if (alert != null) {
                alert.accept();
                return true;
            }
            return false;
        }, "Accept alert if present");
        return Boolean.TRUE.equals(result);
    }

    // ✅ Dismiss alert if present
    public boolean dismissAlert(WebDriver driver) {
        ElementActionHandler handler = new ElementActionHandler(driver);
        Boolean result = handler.performActionWithRetry(null, () -> {
            Alert alert = waitForAlert(driver, 5);
            if (alert != null) {
                alert.dismiss();
                return true;
            }
            return false;
        }, "Dismiss alert if present");
        return Boolean.TRUE.equals(result);
    }

    // ✅ Check if alert is present
    public boolean isAlertPresent(WebDriver driver) {
        ElementActionHandler handler = new ElementActionHandler(driver);
        Boolean result = handler.performActionWithRetry(null, () -> {
            try {
                driver.switchTo().alert();
                return true;
            } catch (NoAlertPresentException e) {
                return false;
            }
        }, "Check if alert is present");
        return Boolean.TRUE.equals(result);
    }

    // ✅ Get alert text (if any)
    public String getAlertText(WebDriver driver) {
        ElementActionHandler handler = new ElementActionHandler(driver);
        return handler.performActionWithRetry(null, () -> {
            Alert alert = waitForAlert(driver, 5);
            if (alert != null) {
                return alert.getText();
            }
            return null;
        }, "Get alert text");
    }

    // **************************************************************************************************
    //                                  --- Dynamic web table utilities ---
    // **************************************************************************************************
// ✅ Waits for table visibility before interacting
    private WebElement waitForTable(WebDriver driver, WebElement table, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return wait.until(ExpectedConditions.visibilityOf(table));
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(table);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    // ✅ Get total row count (excluding header row if present)
    public int getRowCount(WebDriver driver, WebElement table) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            return handler.performActionWithRetry(table, () -> {
                List<WebElement> rows = table.findElements(By.xpath(".//tr"));
                return rows.size();
            }, "Get table row count");
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(table);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    // ✅ Get total column count (from header row if present)
    public int getColumnCount(WebDriver driver, WebElement table) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            return handler.performActionWithRetry(table, () -> {
                List<WebElement> headers = table.findElements(By.xpath(".//th"));
                if (!headers.isEmpty()) return headers.size();

                List<WebElement> firstRowCells = table.findElements(By.xpath(".//tr[1]/td"));
                return firstRowCells.size();
            }, "Get table column count");
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(table);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    // ✅ Get list of header names
    public List<String> getHeaderNames(WebDriver driver, WebElement table) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            return handler.performActionWithRetry(table, () -> {
                List<WebElement> headers = table.findElements(By.xpath(".//th"));
                if (headers.isEmpty()) return Collections.emptyList();

                return headers.stream()
                        .map(WebElement::getText)
                        .map(String::trim)
                        .collect(Collectors.toList());
            }, "Get header names");
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(table);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    // ✅ Get cell value by row and column index (1-based)
    public String getCellData(WebDriver driver, WebElement table, int rowIndex, int colIndex) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            return handler.performActionWithRetry(table, () -> {
                WebElement cell = table.findElement(By.xpath(".//tr[" + rowIndex + "]/td[" + colIndex + "]"));
                return cell.getText().trim();
            }, "Get cell data for row " + rowIndex + ", column " + colIndex);
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(table);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    // ✅ Get cell value by header name (if headers exist)
    public String getCellDataByHeader(WebDriver driver, WebElement table, int rowIndex, String headerName) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            return handler.performActionWithRetry(table, () -> {
                List<String> headers = getHeaderNames(driver, table);
                int colIndex = headers.indexOf(headerName.trim()) + 1;

                if (colIndex == 0) {
                    throw new NoSuchElementException("Header '" + headerName + "' not found");
                }

                WebElement cell = table.findElement(By.xpath(".//tr[" + (rowIndex + 1) + "]/td[" + colIndex + "]"));
                return cell.getText().trim();
            }, "Get cell data by header name: " + headerName);
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(table);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    // ✅ Find row index containing specific text
    public int findRowWithText(WebDriver driver, WebElement table, String searchText) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            return handler.performActionWithRetry(table, () -> {
                List<WebElement> rows = table.findElements(By.xpath(".//tr"));
                for (int i = 0; i < rows.size(); i++) {
                    if (rows.get(i).getText().contains(searchText)) {
                        return i + 1; // Return 1-based index
                    }
                }
                return -1; // Not found
            }, "Find row containing text: " + searchText);
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(table);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    // ✅ Get entire table data as List<Map<Header, Value>>
    public List<Map<String, String>> getAllTableData(WebDriver driver, WebElement table) {
        try {
            ElementActionHandler handler = new ElementActionHandler(driver);
            return handler.performActionWithRetry(table, () -> {
                List<String> headers = getHeaderNames(driver, table);
                List<Map<String, String>> tableData = new ArrayList<>();
                List<WebElement> rows = table.findElements(By.xpath(".//tr[td]")); // skip header-only rows

                for (WebElement row : rows) {
                    List<WebElement> cells = row.findElements(By.xpath(".//td"));
                    Map<String, String> rowData = new LinkedHashMap<>();
                    for (int i = 0; i < cells.size(); i++) {
                        String key = headers.size() > i ? headers.get(i) : "Column" + (i + 1);
                        rowData.put(key, cells.get(i).getText().trim());
                    }
                    tableData.add(rowData);
                }
                return tableData;
            }, "Get all table data as key-value map");
        } catch (Exception e) {
            String[] details = ElementMetaExtractor.getElementDetails(table);
            ErrorReporter.reportBusinessError(e, details[0], details[1], "Click");
            throw e;
        }
    }

    // **************************************************************************************************
    //                                  --- Windows Utilities ---
    // **************************************************************************************************
    // ✅ Get Current URL
    public String getCurrentURL(WebDriver driver) {
        ElementActionHandler handler = new ElementActionHandler(driver);
        return handler.performActionWithRetry(null, () -> {
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl == null || currentUrl.isEmpty()) {
                throw new RuntimeException("Current URL is null or empty");
            }
            log.info("Current URL: {}", currentUrl);
            return currentUrl;
        }, "Get current page URL");
    }

    // ✅ Get Page Title
    public String getTitle(WebDriver driver) {
        ElementActionHandler handler = new ElementActionHandler(driver);
        return handler.performActionWithRetry(null, () -> {
            String title = driver.getTitle();
            if (title == null || title.isEmpty()) {
                throw new RuntimeException("Page title is null or empty");
            }
            log.info("Page title: {}", title);
            return title;
        }, "Get page title");
    }

    // ✅ Navigate Back
    public String navigateBack(WebDriver driver) {
        ElementActionHandler handler = new ElementActionHandler(driver);
        return handler.performActionWithRetry(null, () -> {
            driver.navigate().back();
            String title = driver.getTitle();
            if (title == null || title.isEmpty()) {
                throw new RuntimeException("Failed to retrieve page title after navigating back");
            }
            log.info("Navigated back to page: {}", title);
            return title;
        }, "Navigate back in browser history");
    }

    // ✅ Navigate Forward
    public String navigateForward(WebDriver driver) {
        ElementActionHandler handler = new ElementActionHandler(driver);
        return handler.performActionWithRetry(null, () -> {
            driver.navigate().forward();
            String title = driver.getTitle();
            if (title == null || title.isEmpty()) {
                throw new RuntimeException("Failed to retrieve page title after navigating forward");
            }
            log.info("Navigated forward to page: {}", title);
            return title;
        }, "Navigate forward in browser history");
    }

    // ✅ Refresh Current Page
    public String refreshPage(WebDriver driver) {
        ElementActionHandler handler = new ElementActionHandler(driver);
        return handler.performActionWithRetry(null, () -> {
            driver.navigate().refresh();
            String title = driver.getTitle();
            if (title == null || title.isEmpty()) {
                throw new RuntimeException("Failed to retrieve page title after refresh");
            }
            log.info("Page refreshed successfully: {}", title);
            return title;
        }, "Refresh the current page");
    }

    // **************************************************************************************************
    //                                  --- Reporting and evidence Helpers ---
    // **************************************************************************************************
    private String generateFilePath(String filename, String path) {
        String dateName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return path + File.separator + filename + "_" + dateName + ".png";
    }

    // ✅ Generic helper to save screenshot images
    private String saveScreenshotImage(BufferedImage image, String destination) throws Exception {
        File file = new File(destination);
        FileUtils.forceMkdirParent(file);
        ImageIO.write(image, "PNG", file);
        return destination;
    }

    // ✅ 1. Capture normal viewport screenshot
    public String takeViewportScreenshot(WebDriver driver, String filename, String path) {
        ElementActionHandler handler = new ElementActionHandler(driver);
        return handler.performActionWithRetry(null, () -> {
            String destination = generateFilePath(filename, path);
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(source, new File(destination));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            log.info("Viewport screenshot saved at: {}", destination);
            return destination;
        }, "Capture viewport screenshot");
    }

    // ✅ 2. Capture full page screenshot (uses AShot stitching)
    public String takeFullPageScreenshot(WebDriver driver, String filename, String path) {
        ElementActionHandler handler = new ElementActionHandler(driver);
        return handler.performActionWithRetry(null, () -> {
            String destination = generateFilePath(filename, path);
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(100))
                    .takeScreenshot(driver);

            try {
                saveScreenshotImage(screenshot.getImage(), destination);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            log.info("Full page screenshot saved at: {}", destination);
            return destination;
        }, "Capture full page screenshot");
    }

    // ✅ 3. Capture specific element screenshot (uses AShot)
    public String takeElementScreenshot(WebDriver driver, WebElement element, String filename, String path) {
        ElementActionHandler handler = new ElementActionHandler(driver);
        return handler.performActionWithRetry(element, () -> {
            String destination = generateFilePath(filename, path);
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.simple())
                    .takeScreenshot(driver, element);

            try {
                saveScreenshotImage(screenshot.getImage(), destination);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            log.info("Element screenshot saved at: {}", destination);
            return destination;
        }, "Capture specific element screenshot");
    }

    // ✅ 4. Capture screenshot on failure (e.g., in @AfterMethod)
    public String captureScreenshotOnFailure(WebDriver driver, String filename, String path) {
        ElementActionHandler handler = new ElementActionHandler(driver);
        return handler.performActionWithRetry(null, () -> {
            String destination = generateFilePath(filename + "_FAILURE", path);
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(source, new File(destination));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            log.warn("Failure screenshot saved at: {}", destination);
            return destination;
        }, "Capture screenshot on test failure");
    }

    // ✅ 5. Attach screenshot to report (returns file path)
    public String attachScreenshotToReport(WebDriver driver, String filename, String path) {
        ElementActionHandler handler = new ElementActionHandler(driver);
        return handler.performActionWithRetry(null, () -> {
            String destination = generateFilePath(filename + "_REPORT", path);
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(source, new File(destination));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            log.info("Screenshot attached for report: {}", destination);
            return destination;
        }, "Attach screenshot to report");
    }


}
