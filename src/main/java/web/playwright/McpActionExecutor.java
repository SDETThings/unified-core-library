package web.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web.playwright.pageActions.ElementActionHandler;

import java.util.function.*;

public class McpActionExecutor {
    private static final Logger log = LogManager.getLogger(McpActionExecutor.class);
    public static <T> T executeWithMcp(
            Page page,
            Locator locator,
            Supplier<T> action,
            String actionName) {

        PlaywrightMcpManager mcp = new PlaywrightMcpManager();
        mcp.start();

        try {
            log.warn("MCP fallback triggered for action '{}' on locator {}",
                    actionName, locator);

            // MCP-style stronger readiness
            locator.waitFor(new Locator.WaitForOptions()
                    .setState( WaitForSelectorState.ATTACHED));

            locator.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE));

            return action.get(); // re-run SAME action

        } catch (Exception e) {
            log.error("MCP execution failed for action '{}'", actionName, e);
            throw e;

        } finally {
            mcp.stop();
        }
    }
}
