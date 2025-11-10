package web.errorHandler;

import com.microsoft.playwright.Locator;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Field;
import java.util.List;

public class ElementMetaExtractor {
    public static String[] getElementDetails(Object elementRef) {
        List<Object> activePages = PageContext.getActivePages();

        for (Object page : activePages) {
            for (Field field : page.getClass().getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(page);

                    // Check for Selenium WebElement or Playwright Locator
                    if ((value instanceof WebElement && value.equals(elementRef)) || (value instanceof Locator && value.equals(elementRef))) {

                        String elementName = (field.isAnnotationPresent(ElementDescription.class))
                                ? field.getAnnotation(ElementDescription.class).value()
                                : field.getName();

                        return new String[]{
                                page.getClass().getSimpleName(),
                                elementName
                        };
                    }
                } catch (Exception ignored) {}
            }
        }
        return new String[]{"Unknown Page", "Unknown Element"};
    }
}
