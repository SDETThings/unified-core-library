package unifiedReports;

import unifiedReports.requestLogger.APIConsoleLogger;
import unifiedReports.requestLogger.APIReportLogger;
import unifiedReports.webLogger.WebConsoleLogger;
import unifiedReports.webLogger.WebReportLogger;

public class LoggerFactory {    private LoggerFactory() {
    }

    public static APIConsoleLogger getApiConsoleLogger() {
        return APIConsoleLogger.getInstance();
    }

    public static APIReportLogger getApiReportLogger() {
        return APIReportLogger.getInstance();
    }

    public static WebConsoleLogger getWebConsoleLogger() {
        return WebConsoleLogger.getInstance();
    }

    public static WebReportLogger getWebReportLogger() {
        return WebReportLogger.getInstance();
    }

}
