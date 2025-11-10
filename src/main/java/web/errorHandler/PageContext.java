package web.errorHandler;

import java.util.*;
public class PageContext {
    private static final ThreadLocal<List<Object>> activePages = ThreadLocal.withInitial(ArrayList::new);

    public static void register(Object pageObject) {
        List<Object> pages = activePages.get();
        if (!pages.contains(pageObject)) {
            pages.add(pageObject);
        }
    }

    public static List<Object> getActivePages() {
        return activePages.get();
    }

    public static void clear() {
        activePages.get().clear();
    }
}
