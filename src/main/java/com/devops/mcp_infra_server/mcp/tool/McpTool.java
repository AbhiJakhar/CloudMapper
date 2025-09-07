// Interface for MCP tools
package com.devops.mcp_infra_server.mcp.tool;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.concurrent.CompletableFuture;

public interface McpTool {

    // Name for the tool for AI assistants
    String getName();

    // Description for what this tool will be used
    String getDescription();

    //Json schema describing input parameters
    JsonNode getInputSchema();

    // Execute tool with given parameters
    CompletableFuture<McpToolResult> execute(JsonNode parameters);
}
