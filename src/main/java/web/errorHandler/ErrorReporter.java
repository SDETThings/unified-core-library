package web.errorHandler;

import unifiedReports.webLogger.WebConsoleLogger;
import unifiedReports.webLogger.WebReportLogger;

public class ErrorReporter {
    public static void reportBusinessError(Exception e, String page, String element, String action) {
        System.err.println("\n🚨Error Report:");
        System.err.println("   🧩 Page: " + page);
        System.err.println("   🎯 Action: " + action);
        System.err.println("   🔖 Element: " + element);
        System.err.println("   💬 Root Cause: " + e.getMessage());
        System.err.println("------------------------------------------------------------\n");
        WebConsoleLogger.logError("\n🚨Error Report:\n"+ "   🧩 Page: " + page +"\n"+
        "   🎯 Action: " + action +"\n"+
        "   🔖 Element: " + element  +"\n"+
        "   💬 Root Cause: \n" , e);
        WebReportLogger.logError("\n🚨Error Report:\n"+ "   🧩 Page: " + page +"\n"+
                "   🎯 Action: " + action +"\n"+
                "   🔖 Element: " + element  +"\n"+
                "   💬 Root Cause: \n" , e);
    }
}
