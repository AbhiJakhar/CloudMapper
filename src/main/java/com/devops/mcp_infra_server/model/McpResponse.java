package com.devops.mcp_infra_server.model;

import com.fasterxml.jackson.databind.JsonNode;

public class McpResponse {
    private String jsonrpc = "2.0";
    private String id;
    private JsonNode result;
    private McpError error;

    public McpResponse() {}

    // Success response
    public McpResponse (String id, JsonNode result) {
        this.id=id;
        this.result=result;
    }

    // Error response
    public McpResponse (String id, McpError error) {
        this.id=id;
        this.error=error;
    }

    public static McpResponse success(String id, JsonNode result) {
        return new McpResponse(id, result);
    }

    public static McpResponse error(String id, String message) {
        return new McpResponse(id, new McpError(-1, message));
    }

    // Getters and Setters
    public String getJsonrpc() { return jsonrpc; }
    public void setJsonrpc(String jsonrpc) { this.jsonrpc = jsonrpc; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public JsonNode getResult() { return result; }
    public void setResult(JsonNode result) { this.result = result; }

    public McpError getError() { return error; }
    public void setError(McpError error) { this.error = error; }
}
