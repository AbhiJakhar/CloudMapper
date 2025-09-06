package com.devops.mcp_infra_server.mcp.tool;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.concurrent.CompletableFuture;

public interface McpTool {

    String getName();

    String getDescription();

    JsonNode getInputSchema();

    CompletableFuture<McpToolResult> execute(JsonNode parameters);
}
