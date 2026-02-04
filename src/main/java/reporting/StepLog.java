package reporting;

public class StepLog {
    private final Status status;
    private final String message;
    private final String timestamp;

    public StepLog(Status status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }

    public Status getStatus() { return status; }
    public String getMessage() { return message; }
}

