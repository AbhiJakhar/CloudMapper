// This is how the server responds
//{
//  "jsonrpc": "2.0",
//  "id": "123",
//  "result": {
//    "content": [
//      {"name": "aws", "region": "us-east-1", "active": true},
//      {"name": "azure", "region": "eastus", "active": true}
//    ]
//  }
//}
package com.devops.mcp_infra_server.mcp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class McpResponse {
    private String jsonrpc = "2.0";
    private String id;              // Same ID as request
    private JsonNode result;        // Success data
    private McpError error;         // or error info

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
