package web.selenium.pageActions;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import web.selenium.synchronisation.DriverWaits;
import web.selenium.synchronisation.WebDriverWaits;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class DriverActions extends DriverWaits {
    private WebDriverWaits webDriverWaits;
    private JavascriptExecutor javascriptExecutor;
    private static final Logger log = LogManager.getLogger(DriverActions.class);

    public DriverActions(WebDriverWaits webDriverWaits) {
        this.webDriverWaits = webDriverWaits;
    }
    public boolean clickOnWebElement(WebDriver driver, WebElement ele,int timeoutSeconds, int pollingIntervalSeconds) throws Exception {

        boolean flag = false;
        if (webDriverWaits.apply(driver, ele, timeoutSeconds, pollingIntervalSeconds)){
            waitForElementToBeClickable(ele, timeoutSeconds, pollingIntervalSeconds);
            scrollIntoViewAndHighlight(driver,ele);
            ele.click();
            flag = true;
        }else
        {
                log.error("Cannot find given web element : " + ele);
        }
        return flag;
    }
    public boolean sendKeys(WebDriver driver, WebElement ele,String text,int timeoutSeconds, int pollingIntervalSeconds) throws Exception {
        waitForElementToBeVisible(ele,timeoutSeconds,pollingIntervalSeconds);
        scrollIntoViewAndHighlight(driver,ele);
        boolean flag = false;
        try
        {
            if(webDriverWaits.apply(driver, ele, timeoutSeconds, pollingIntervalSeconds))
            {
                ele.sendKeys(text);
                flag = true;
            }else {
                log.error("Cannot find given web element : " + ele);
            }
        }catch (Exception e)
        {
            log.error("Cannot enter text : " + text + " in text box : " + ele);
            throw new Exception();
        }
        return flag;
    }
    public boolean enterText(WebDriver driver, WebElement ele,String text,int timeoutSeconds, int pollingIntervalSeconds) throws Exception {
        waitForElementToBeVisible(ele,timeoutSeconds,pollingIntervalSeconds);
        scrollIntoViewAndHighlight(driver,ele);
        boolean flag = false;
        try
        {
            if(webDriverWaits.apply(driver, ele, timeoutSeconds, pollingIntervalSeconds))
            {
                ele.sendKeys(text);
                flag = true;
            }else {
                log.error("Cannot find given web element : " + ele);
            }
        }catch (Exception e)
        {
            log.error("Cannot enter text : " + text + " in text box : " + ele);
            throw new Exception();
        }
        return flag;
    }
    public String getText(WebDriver driver, WebElement ele,int timeoutSeconds, int pollingIntervalSeconds) throws Exception {
        waitForElementToBeVisible(ele,timeoutSeconds,pollingIntervalSeconds);
        scrollIntoViewAndHighlight(driver,ele);

        return ele.getText();
    }
    public String getTitle(WebDriver driver) throws InterruptedException {
        String text = null;
        text = driver.getTitle();
        if (text!=null) {
            log.debug("Title of the page is: \"" + text + "\"");
        }else{
            throw  new RuntimeException("Title of the page is null ");
        }
        return text;
    }
    public void scrollByVisibilityOfElement(WebDriver driver, WebElement ele,int timeoutSeconds, int pollingIntervalSeconds) {
        waitForElementToBeVisible(ele,timeoutSeconds,pollingIntervalSeconds);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", ele);
    }
    public void click(WebDriver driver, WebElement ele,int timeoutSeconds, int pollingIntervalSeconds) throws InterruptedException {
        waitForElementToBeVisible(ele,timeoutSeconds,pollingIntervalSeconds);
        ele.click();
    }
    public boolean findElement(WebDriver driver, WebElement ele,int timeoutSeconds, int pollingIntervalSeconds) {
        waitForElementToBeVisible(ele,timeoutSeconds,pollingIntervalSeconds);
        boolean flag = false;
        try {
            ele.isDisplayed();
            flag = true;
        } catch (Exception e) {
            flag = false;
        } finally {
            if (flag) {
                System.out.println("Successfully Found element");

            } else {
                System.out.println("Unable to locate element at: "+ele);
            }
        }
        return flag;
    }
    public boolean isDisplayed(WebDriver driver, WebElement ele,int timeoutSeconds, int pollingIntervalSeconds) {
        waitForElementToBeVisible(ele,timeoutSeconds,pollingIntervalSeconds);
        boolean flag = false;
        flag = findElement(driver, ele,timeoutSeconds,pollingIntervalSeconds);
        if (flag) {
            flag = ele.isDisplayed();
            if (flag) {
                System.out.println("The element is Displayed");
            } else {
                System.out.println("The element is not Displayed");
            }
        } else {
            System.out.println("Not displayed ");
        }
        return flag;
    }
    public boolean isSelected(WebDriver driver, WebElement ele,int timeoutSeconds, int pollingIntervalSeconds) {
        waitForElementToBeVisible(ele,timeoutSeconds,pollingIntervalSeconds);
        boolean flag = false;
        flag = findElement(driver, ele,timeoutSeconds,pollingIntervalSeconds);
        if (flag) {
            flag = ele.isSelected();
            if (flag) {
                System.out.println("The element is Selected");
            } else {
                System.out.println("The element is not Selected");
            }
        } else {
            System.out.println("Not selected ");
        }
        return flag;
    }
    public boolean isEnabled(WebDriver driver, WebElement ele,int timeoutSeconds, int pollingIntervalSeconds) {
        waitForElementToBeEnabled(ele,timeoutSeconds,pollingIntervalSeconds);
        boolean flag;
        flag = findElement(driver, ele,timeoutSeconds,pollingIntervalSeconds);
        if (flag) {
            flag = ele.isEnabled();
            if (flag) {
                System.out.println("The element is Enabled");
            } else {
                System.out.println("The element is not Enabled: "+ele);
            }
        }
        return flag;
    }
    public boolean selectBySendkeys(String value, WebElement ele,int timeoutSeconds, int pollingIntervalSeconds) {
        waitForElementToBeVisible(ele,timeoutSeconds,pollingIntervalSeconds);
        boolean flag = false;
        try {
            ele.sendKeys(value);
            flag = true;
            return true;
        } catch (Exception e) {

            return false;
        } finally {
            if (flag) {
                System.out.println("Select value from the DropDown");
            } else {
                System.out.println("Not Selected value from the DropDown");
                // throw new ElementNotFoundException("", "", "")
            }
        }
    }
    public boolean selectByIndex(WebElement element, int index,int timeoutSeconds, int pollingIntervalSeconds) {
        waitForElementToBeVisible(element,timeoutSeconds,pollingIntervalSeconds);
        boolean flag = false;
        try {
            Select s = new Select(element);
            s.selectByIndex(index);
            flag = true;
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (flag) {
                System.out.println("Option selected by Index");
            } else {
                System.out.println("Option not selected by Index");
            }
        }
    }
    public boolean selectByValue(WebElement ele, String value,int timeoutSeconds, int pollingIntervalSeconds) {
        waitForElementToBeVisible(ele,timeoutSeconds,pollingIntervalSeconds);
        boolean flag = false;
        try {
            Select s = new Select(ele);
            s.selectByValue(value);
            flag = true;
            return true;
        } catch (Exception e) {

            return false;
        } finally {
            if (flag) {
                System.out.println("Option selected by Value");
            } else {
                System.out.println("Option not selected by Value");
            }
        }
    }
    public boolean selectByVisibleText(String visibletext, WebElement ele,int timeoutSeconds, int pollingIntervalSeconds) {
        waitForElementToBeVisible(ele,timeoutSeconds,pollingIntervalSeconds);
        boolean flag = false;
        try {
            Select s = new Select(ele);
            s.selectByVisibleText(visibletext);
            flag = true;
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (flag) {
                System.out.println("Option selected by VisibleText");
            } else {
                System.out.println("Option not selected by VisibleText");
            }
        }
    }
    public boolean JSClick(WebDriver driver, WebElement ele,int timeoutSeconds, int pollingIntervalSeconds) {
        waitForElementToBeClickable(ele,timeoutSeconds,pollingIntervalSeconds);
        boolean flag = false;
        try {
            // WebElement element = driver.findElement(locator);
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", ele);
            // driver.executeAsyncScript("arguments[0].click();", element);

            flag = true;

        }

        catch (Exception e) {
            throw e;

        } finally {
            if (flag) {
                System.out.println("Click Action is performed");
            } else if (!flag) {
                System.out.println("Click Action is not performed");
            }
        }
        return flag;
    }
    public boolean switchToFrameById(WebDriver driver, String idValue,int timeoutSeconds, int pollingIntervalSeconds) {
        boolean flag = false;
        try {
            driver.switchTo().frame(idValue);
            flag = true;
            return true;
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        } finally {
            if (flag) {
                System.out.println("Frame with Id \"" + idValue + "\" is selected");
            } else {
                System.out.println("Frame with Id \"" + idValue + "\" is not selected");
            }
        }
    }
    public boolean switchToFrameByName(WebDriver driver, String nameValue,int timeoutSeconds, int pollingIntervalSeconds) {
        boolean flag = false;
        try {
            driver.switchTo().frame(nameValue);
            flag = true;
            return true;
        } catch (Exception e) {

            return false;
        } finally {
            if (flag) {
                System.out.println("Frame with Name \"" + nameValue + "\" is selected");
            } else if (!flag) {
                System.out.println("Frame with Name \"" + nameValue + "\" is not selected");
            }
        }
    }
    public boolean switchToDefaultFrame(WebDriver driver,int timeoutSeconds, int pollingIntervalSeconds) {
        boolean flag = false;
        try {
            driver.switchTo().defaultContent();
            flag = true;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (flag) {
                // SuccessReport("SelectFrame ","Frame with Name is selected");
            } else if (!flag) {
                // failureReport("SelectFrame ","The Frame is not selected");
            }
        }
    }
    public void mouseOverElement(WebDriver driver, WebElement ele,int timeoutSeconds, int pollingIntervalSeconds) {
        waitForElementToBeVisible(ele,timeoutSeconds,pollingIntervalSeconds);
        boolean flag = false;
        try {
            new Actions(driver).moveToElement(ele).build().perform();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (flag) {
                System.out.println(" MouserOver Action is performed on ");
            } else {
                System.out.println("MouseOver action is not performed on");
            }
        }
    }
    public boolean moveToElement(WebDriver driver, WebElement ele,int timeoutSeconds, int pollingIntervalSeconds) {
        waitForElementToBeVisible(ele,timeoutSeconds,pollingIntervalSeconds);
        boolean flag = false;
        try {
            // WebElement element = driver.findElement(locator);
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].scrollIntoView(true);", ele);
            Actions actions = new Actions(driver);
            // actions.moveToElement(driver.findElement(locator)).build().perform();
            actions.moveToElement(ele).build().perform();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    public boolean mouseover(WebDriver driver, WebElement ele,int timeoutSeconds, int pollingIntervalSeconds) {
        waitForElementToBeVisible(ele,timeoutSeconds,pollingIntervalSeconds);
        boolean flag = false;
        try {
            new Actions(driver).moveToElement(ele).build().perform();
            flag = true;
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            /*
             * if (flag) {
             * SuccessReport("MouseOver ","MouserOver Action is performed on \""+locatorName
             * +"\""); } else {
             * failureReport("MouseOver","MouseOver action is not performed on \""
             * +locatorName+"\""); }
             */
        }
    }
    public boolean draggable(WebDriver driver, WebElement ele, int x, int y,int timeoutSeconds, int pollingIntervalSeconds) {
        waitForElementToBeVisible(ele,timeoutSeconds,pollingIntervalSeconds);
        javascriptExecutor.executeScript("arguments[0].scrollIntoView(true)");
        javascriptExecutor.executeScript("arguments[0].setAttribute('style','background: green; border: solid 2px red');");
        boolean flag = false;
        try {
            new Actions(driver).dragAndDropBy(ele, x, y).build().perform();
            Thread.sleep(5000);
            flag = true;
            return true;

        } catch (Exception e) {

            return false;

        } finally {
            if (flag) {
                System.out.println("Draggable Action is performed on \"" + ele + "\"");
            } else if (!flag) {
                System.out.println("Draggable action is not performed on \"" + ele + "\"");
            }
        }
    }
    public boolean draganddrop(WebDriver driver, WebElement source, WebElement target,int timeoutSeconds, int pollingIntervalSeconds) {
        waitForElementToBeVisible(source,timeoutSeconds,pollingIntervalSeconds);
        boolean flag = false;
        try {
            new Actions(driver).dragAndDrop(source, target).perform();
            flag = true;
            return true;
        } catch (Exception e) {

            return false;
        } finally {
            if (flag) {
                System.out.println("DragAndDrop Action is performed");
            } else if (!flag) {
                System.out.println("DragAndDrop Action is not performed");
            }
        }
    }
    public boolean slider(WebDriver driver, WebElement ele, int x, int y,int timeoutSeconds, int pollingIntervalSeconds) {
        waitForElementToBeVisible(ele,timeoutSeconds,pollingIntervalSeconds);
        boolean flag = false;
        try {
            // new Actions(driver).dragAndDropBy(dragitem, 400, 1).build()
            // .perform();
            new Actions(driver).dragAndDropBy(ele, x, y).build().perform();// 150,0
            Thread.sleep(5000);
            flag = true;
            return true;
        } catch (Exception e) {

            return false;
        } finally {
            if (flag) {
                System.out.println("Slider Action is performed");
            } else {
                System.out.println("Slider Action is not performed");
            }
        }
    }
    public boolean rightclick(WebDriver driver, WebElement ele,int timeoutSeconds, int pollingIntervalSeconds) {
        waitForElementToBeVisible(ele,timeoutSeconds,pollingIntervalSeconds);
        boolean flag = false;
        try {
            Actions clicker = new Actions(driver);
            clicker.contextClick(ele).perform();
            flag = true;
            return true;
            // driver.findElement(by1).sendKeys(Keys.DOWN);
        } catch (Exception e) {

            return false;
        } finally {
            if (flag) {
                System.out.println("RightClick Action is performed");
            } else {
                System.out.println("RightClick Action is not performed");
            }
        }
    }
    public boolean switchWindowByTitle(WebDriver driver, String windowTitle, int count,int timeoutSeconds, int pollingIntervalSeconds) {
        boolean flag = false;
        try {
            Set<String> windowList = driver.getWindowHandles();

            String[] array = windowList.toArray(new String[0]);

            driver.switchTo().window(array[count - 1]);

            if (driver.getTitle().contains(windowTitle)) {
                flag = true;
            } else {
                flag = false;
            }
            return flag;
        } catch (Exception e) {
            // flag = true;
            return false;
        } finally {
            if (flag) {
                System.out.println("Navigated to the window with title");
            } else {
                System.out.println("The Window with title is not Selected");
            }
        }
    }
    public boolean switchToNewWindow(WebDriver driver,int timeoutSeconds, int pollingIntervalSeconds) {
        boolean flag = false;
        try {

            Set<String> s = driver.getWindowHandles();
            Object popup[] = s.toArray();
            driver.switchTo().window(popup[1].toString());
            flag = true;
            return flag;
        } catch (Exception e) {
            flag = false;
            return flag;
        } finally {
            if (flag) {
                System.out.println("Window is Navigated with title");
            } else {
                System.out.println("The Window with title: is not Selected");
            }
        }
    }
    public boolean switchToOldWindow(WebDriver driver,int timeoutSeconds, int pollingIntervalSeconds) {
        boolean flag = false;
        try {

            Set<String> s = driver.getWindowHandles();
            Object popup[] = s.toArray();
            driver.switchTo().window(popup[0].toString());
            flag = true;
            return flag;
        } catch (Exception e) {
            flag = false;
            return flag;
        } finally {
            if (flag) {
                System.out.println("Focus navigated to the window with title");
            } else {
                System.out.println("The Window with title: is not Selected");
            }
        }
    }
    public int getColumncount(WebElement row,int timeoutSeconds, int pollingIntervalSeconds) {
        waitForElementToBeVisible(row,timeoutSeconds,pollingIntervalSeconds);
        List<WebElement> columns = row.findElements(By.tagName("td"));
        int a = columns.size();
        System.out.println(columns.size());
        for (WebElement column : columns) {
            System.out.print(column.getText());
            System.out.print("|");
        }
        return a;
    }
    public int getRowCount(WebElement table,int timeoutSeconds, int pollingIntervalSeconds) {
        waitForElementToBeVisible(table,timeoutSeconds,pollingIntervalSeconds);
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        int a = rows.size() - 1;
        return a;
    }
    public boolean Alert(WebDriver driver,int timeoutSeconds, int pollingIntervalSeconds) {
        boolean presentFlag = false;
        Alert alert = null;

        try {
            // Check the presence of alert
            alert = driver.switchTo().alert();
            // if present consume the alert
            alert.accept();
            presentFlag = true;
        } catch (NoAlertPresentException ex) {
            // Alert present; set the flag

            // Alert not present
            ex.printStackTrace();
        } finally {
            if (!presentFlag) {
                System.out.println("The Alert is handled successfully");
            } else {
                System.out.println("There was no alert to handle");
            }
        }

        return presentFlag;
    }
    public boolean isAlertPresent(WebDriver driver,int timeoutSeconds, int pollingIntervalSeconds) {
        try {
            driver.switchTo().alert();
            return true;
        } // try
        catch (NoAlertPresentException Ex) {
            return false;
        } // catch
    }
    public String getCurrentURL(WebDriver driver) {
        boolean flag = false;

        String text = driver.getCurrentUrl();
        if (flag) {
            System.out.println("Current URL is: \"" + text + "\"");
        }
        return text;
    }
    public String screenShot(WebDriver driver, String filename, String path) {
        String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File source = takesScreenshot.getScreenshotAs(OutputType.FILE);
        String destination = path + "/" + filename + "_" + dateName + ".png";
        try {
            FileUtils.copyFile(source, new File(destination));
        } catch (Exception e) {
            e.getMessage();
        }
        // This new path for jenkins
        //String newImageString = "./src/test/resources/Screenshot" + filename
        //+ "_" + dateName + ".png";

        return destination;
    }
    public void scrollIntoViewAndHighlight(WebDriver driver,WebElement ele){
        javascriptExecutor = (JavascriptExecutor) driver;
        String originalStyle = ele.getAttribute("style");
        javascriptExecutor.executeScript("arguments[0].scrollIntoView({ behavior: 'smooth', block: 'center' });", ele);
        javascriptExecutor.executeScript("arguments[0].style.border='3px solid green'; arguments[0].style.backgroundColor='yellow';", ele);
        //javascriptExecutor.executeScript("arguments[0].setAttribute('style', arguments[1]);", ele, originalStyle);

    }
    public void scrollIntoView(WebDriver driver,WebElement ele){
        javascriptExecutor = (JavascriptExecutor) driver;
        String originalStyle = ele.getAttribute("style");
        javascriptExecutor.executeScript("arguments[0].scrollIntoView({ behavior: 'smooth', block: 'center' });", ele);
        //javascriptExecutor.executeScript("arguments[0].setAttribute('style', arguments[1]);", ele, originalStyle);

    }
    public void unhighlight(WebDriver driver,WebElement ele){
        javascriptExecutor = (JavascriptExecutor) driver;
        String originalStyle = ele.getAttribute("style");
        javascriptExecutor.executeScript("arguments[0].setAttribute('style', arguments[1]);", ele, originalStyle);

    }

}
