// The Brain
package com.devops.mcp_infra_server.mcp;

import com.devops.mcp_infra_server.mcp.model.McpRequest;
import com.devops.mcp_infra_server.mcp.model.McpResponse;
import com.devops.mcp_infra_server.mcp.tool.McpTool;
import com.devops.mcp_infra_server.mcp.tool.McpToolResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class McpServerService {

    private static final Logger logger = LoggerFactory.getLogger(McpServerService.class);

    @Autowired
    private List<McpTool> tools;  // Spring will inject all McpTool implementations

    @Value("${mcp.server.name}")
    private String serverName;

    @Value("${mcp.server.version}")
    private String serverVersion;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public McpResponse handleRequest(McpRequest request) {
        logger.info("Handling MCP request: {}", request.getMethod());

        switch (request.getMethod()) {
            case "initialize":
                return handleInitialize(request);
            case "tools/list":
                return handleToolsList(request);
            case "tools/call":
                return handleToolCall(request);
            default:
                logger.warn("Unknown MCP method: {}", request.getMethod());
                return McpResponse.error(request.getId(), "Unknown method: " + request.getMethod());
        }
    }

    private McpResponse handleInitialize(McpRequest request) {
        logger.info("Initializing MCP server");

        ObjectNode result = objectMapper.createObjectNode();
        result.put("protocolVersion", "2024-11-05");

        ObjectNode serverInfo = objectMapper.createObjectNode();
        serverInfo.put("name", serverName);
        serverInfo.put("version", serverVersion);

        ObjectNode capabilities = objectMapper.createObjectNode();
        capabilities.put("tools", true);

        result.set("serverInfo", serverInfo);
        result.set("capabilities", capabilities);

        return McpResponse.success(request.getId(), result);
    }

    private McpResponse handleToolsList(McpRequest request) {
        logger.info("Listing available tools. Count: {}", tools.size());

        ArrayNode toolsArray = objectMapper.createArrayNode();

        for (McpTool tool : tools) {
            ObjectNode toolInfo = objectMapper.createObjectNode();
            toolInfo.put("name", tool.getName());
            toolInfo.put("description", tool.getDescription());
            toolInfo.set("inputSchema", tool.getInputSchema());
            toolsArray.add(toolInfo);
        }

        ObjectNode result = objectMapper.createObjectNode();
        result.set("tools", toolsArray);

        return McpResponse.success(request.getId(), result);
    }

    private McpResponse handleToolCall(McpRequest request) {
        JsonNode params = request.getParams();

        if (params == null || !params.has("name")) {
            return McpResponse.error(request.getId(), "Missing tool name in parameters");
        }

        String toolName = params.get("name").asText();
        JsonNode arguments = params.has("arguments") ? params.get("arguments") : null;

        logger.info("Calling tool: {} with arguments: {}", toolName, arguments);

        // Find the tool
        McpTool tool = tools.stream()
                .filter(t -> t.getName().equals(toolName))
                .findFirst()
                .orElse(null);

        if (tool == null) {
            return McpResponse.error(request.getId(), "Unknown tool: " + toolName);
        }

        try {
            // Execute the tool (this is async)
            CompletableFuture<McpToolResult> future = tool.execute(arguments);
            McpToolResult result = future.get(); // Wait for completion

            if (result.isError()) {
                return McpResponse.error(request.getId(), result.getErrorMessage());
            }

            ObjectNode response = objectMapper.createObjectNode();
            response.set("content", objectMapper.valueToTree(result.getContent()));

            return McpResponse.success(request.getId(), response);

        } catch (Exception e) {
            logger.error("Error executing tool: " + toolName, e);
            return McpResponse.error(request.getId(), "Tool execution failed: " + e.getMessage());
        }
    }
}