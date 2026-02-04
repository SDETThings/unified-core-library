package reporting;

import java.util.ArrayList;
import java.util.List;

public class ChildTest {

    private final String name;
    private final List<StepLog> logs = new ArrayList<>();
    private Status finalStatus = Status.PASS;

    public ChildTest(String name) {
        this.name = name;
    }

    public void log(Status status, String message) {
        logs.add(new StepLog(status, message));

        if (status == Status.FAIL) {
            finalStatus = Status.FAIL;
        }
    }

    public void skip(String reason) {
        logs.add(new StepLog(Status.SKIP, reason));
        finalStatus = Status.SKIP;
    }

    public Status getFinalStatus() {
        return finalStatus;
    }

    public String getName() {
        return name;
    }

    public List<StepLog> getLogs() {
        return logs;
    }
}

