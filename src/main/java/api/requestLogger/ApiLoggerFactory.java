package api.requestLogger;

public class ApiLoggerFactory {
    private ApiLoggerFactory() {}

    public static APIConsoleLogger getConsoleLogger() {
        return APIConsoleLogger.getInstance();
    }

    public static APIReportLogger getReportLogger() {
        return APIReportLogger.getInstance();
    }
}
