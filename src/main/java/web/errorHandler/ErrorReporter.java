package web.errorHandler;

public class ErrorReporter {
    public static void reportBusinessError(Exception e, String page, String element, String action) {
        System.err.println("\n🚨 Business-Level Error Report:");
        System.err.println("   🧩 Page: " + page);
        System.err.println("   🎯 Action: " + action);
        System.err.println("   🔖 Element: " + element);
        System.err.println("   💬 Root Cause: " + e.getMessage());
        System.err.println("------------------------------------------------------------\n");
    }
}
