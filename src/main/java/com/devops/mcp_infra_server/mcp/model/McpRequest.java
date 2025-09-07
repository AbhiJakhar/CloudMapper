// This is how AI assistant sends requests,
//Example request:
//        {
//         "jsonrpc": "2.0",
//         "id": "123",
//         "method": "tools/call",
//         "params": {
//             "name": "list_cloud_providers",
//             "arguments": {"active": true}
//          }
//        }
package com.devops.mcp_infra_server.mcp.model;

import com.fasterxml.jackson.databind.JsonNode;

public class McpRequest {
    private String jsonrpc = "2.0";    // Protocol version
    private String id;                 // Request ID
    private String method;             // What to do ("tools/list", "tools/call")
    private JsonNode params;           // Parameters for the method

    public McpRequest() {};

    public McpRequest(String id, String method,JsonNode params) {
        this.id=id;
        this.method=method;
        this.params=params;
    }

    // Getter and Setters
    public String getJsonrpc() { return jsonrpc; }
    public void setJsonrpc(String jsonrpc) { this.jsonrpc = jsonrpc; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public JsonNode getParams() { return params; }
    public void setParams(JsonNode params) { this.params = params; }

    @Override
    public String toString() {
        return "McpRequest{" +
                "jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", method='" + method + '\'' +
                ", params=" + params +
                '}';
    }
}
