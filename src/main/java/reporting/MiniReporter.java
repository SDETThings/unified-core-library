package reporting;

import java.nio.file.Files;
import java.nio.file.Paths;

public class MiniReporter {

    private static final ThreadLocal<ParentTest> currentParent = new ThreadLocal<>();
    private static final ThreadLocal<ChildTest> currentChild = new ThreadLocal<>();

    public static void startParent(String testName) {
        ParentTest parent = ReportManager.getInstance().createParent(testName);
        currentParent.set(parent);
    }

    public static void startIteration(String iterationName, String description) {
        ParentTest parent = currentParent.get();
        if (parent == null) {
            throw new IllegalStateException("startParent() was not called before startIteration()");
        }

        ChildTest child = parent.createChild(iterationName + " : " + description);
        currentChild.set(child);
    }

    public static void pass(String msg) {
        currentChild.get().log(Status.PASS, msg);
    }
    public static void info(String msg) {
        currentChild.get().log(Status.INFO, msg);
    }
    public static void codeBlock(String content) {
        currentChild.get().log(
                Status.INFO,
                "<pre class='code-block'>" + escapeHtml(content) + "</pre>"
        );
    }
    private static String escapeHtml(String text) {
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    public static void fail(String msg) {
        currentChild.get().log(Status.FAIL, msg);
    }

    public static void skip(String msg) {
        currentChild.get().skip(msg);
    }

    public static void endIteration() {
        ChildTest child = currentChild.get();
        Status finalStatus = child.getFinalStatus();

        ReportManager.getInstance().updateCounters(finalStatus);
        currentChild.remove();
    }

    public static void flush() {
        String REPORT_DIR = "./src/test/resources/reports/mini-report";
        String REPORT_FILE = REPORT_DIR + "/index.html";
        try {
            Files.createDirectories(Paths.get(REPORT_DIR));
            HtmlReportWriter.write(REPORT_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

