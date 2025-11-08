package api.requestLogger;

import com.aventstack.extentreports.ExtentTest;

public class LoggingContext {
    private static final ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();

    public static void setExtentTest(ExtentTest test) {
        extentTestThreadLocal.set(test);
    }

    public static ExtentTest getExtentTest() {
        return extentTestThreadLocal.get();
    }

    public static void clear() {
        extentTestThreadLocal.remove();
    }
}
