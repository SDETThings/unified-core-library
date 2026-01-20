package web.playwright;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PlaywrightMcpManager {
    private static final Logger log = LogManager.getLogger(McpActionExecutor.class);
    private Process mcpProcess;

    public void start() {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "cmd.exe",
                    "/c",
                    "npx.cmd",
                    "@playwright/mcp"
            );

            pb.redirectErrorStream(true);
            mcpProcess = pb.start();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(mcpProcess.getInputStream()))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    log.error("[MCP] {}", line);
                }
            }
            System.out.println("Playwright MCP server started");

        } catch (IOException e) {
            throw new RuntimeException("Failed to start Playwright MCP server", e);
        }
    }

    public void stop() {
        if (mcpProcess != null && mcpProcess.isAlive()) {
            mcpProcess.destroy();
            System.out.println("Playwright MCP server stopped");
        }
    }
}
