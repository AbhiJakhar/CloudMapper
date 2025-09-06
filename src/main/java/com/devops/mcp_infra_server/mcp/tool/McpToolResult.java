package com.devops.mcp_infra_server.mcp.tool;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class McpToolResult {
    private Object content;
    private boolean isError;
    private String errorMessage;

    // Success result
    public McpToolResult(Object content) {
        this.content = content;
        this.isError = false;
    }

    // Error result
    public McpToolResult(String errorMessage) {
        this.errorMessage = errorMessage;
        this.isError = true;
    }

    // Static helper methods
    public static McpToolResult success(Object content) {
        return new McpToolResult(content);
    }

    public static McpToolResult error(String message) {
        return new McpToolResult(message);
    }

    // Getters and Setters
    public Object getContent() { return content; }
    public void setContent(Object content) { this.content = content; }

    public boolean isError() { return isError; }
    public void setError(boolean error) { isError = error; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
