package web.errorHandler;

import com.microsoft.playwright.Locator;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Supplier;

public class ElementMetaExtractor {
    /*public static String[] getElementDetails(Object elementRef) {

        // Iterate through active pages to find the element reference
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
    }*/
    public static String[] getElementDetails(Object elementRef) {

        String incomingSelector = extractSelector(elementRef);
        if (incomingSelector == null) {
            return new String[]{"Unknown Page", "Unknown Element"};
        }

        for (Object page : PageContext.getActivePages()) {
            for (Field field : page.getClass().getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(page);

                    String storedSelector = extractSelector(value);

                    if (storedSelector != null &&
                            normalize(storedSelector).equals(normalize(incomingSelector))) {

                        String elementName = field.isAnnotationPresent(ElementDescription.class)
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
    private static String normalize(String selector) {
        return selector
                .replaceAll("Locator@.*>>", "")
                .replaceAll("\\s+", "")
                .toLowerCase();
    }
    private static String extractSelector(Object obj) {

        // Case 1: Playwright Locator
        if (obj instanceof Locator) {
            return obj.toString();
        }

        // Case 2: Supplier<Locator> (YOUR CURRENT DESIGN)
        if (obj instanceof Supplier<?>) {
            try {
                Object supplied = ((Supplier<?>) obj).get();
                if (supplied instanceof Locator) {
                    return supplied.toString();
                }
            } catch (Exception ignored) {}
        }

        // Case 3: String selector
        if (obj instanceof String) {
            return (String) obj;
        }

        // Case 4: Selenium WebElement (optional)
        if (obj instanceof WebElement) {
            return obj.toString();
        }

        return null;
    }

}
