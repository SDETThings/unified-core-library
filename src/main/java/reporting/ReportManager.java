package reporting;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReportManager {

    private static final ReportManager INSTANCE = new ReportManager();

    private final Map<String, ParentTest> parentTests = new ConcurrentHashMap<>();

    private int passed = 0;
    private int failed = 0;
    private int skipped = 0;

    private ReportManager() {}

    public static ReportManager getInstance() {
        return INSTANCE;
    }

    public ParentTest createParent(String name) {
        return parentTests.computeIfAbsent(name, ParentTest::new);
    }

    public void updateCounters(Status status) {
        switch (status) {
            case PASS -> passed++;
            case FAIL -> failed++;
            case SKIP -> skipped++;
        }
    }

    public int getPassed() { return passed; }
    public int getFailed() { return failed; }
    public int getSkipped() { return skipped; }

    public Collection<ParentTest> getAllParents() {
        return parentTests.values();
    }
}

