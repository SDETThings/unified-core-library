package reporting;

import java.util.LinkedHashMap;
import java.util.Map;

public class ParentTest {

    private final String name;
    private final Map<String, ChildTest> children = new LinkedHashMap<>();

    public ParentTest(String name) {
        this.name = name;
    }

    public ChildTest createChild(String childName) {
        ChildTest child = new ChildTest(childName);
        children.put(childName, child);
        return child;
    }

    public String getName() {
        return name;
    }

    public Map<String, ChildTest> getChildren() {
        return children;
    }
}

