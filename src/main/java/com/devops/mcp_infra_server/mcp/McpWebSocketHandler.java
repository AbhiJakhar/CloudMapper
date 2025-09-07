// When AI sends JSON messages, parses them , route them to right handler and sends response back
package com.devops.mcp_infra_server.mcp;

import com.devops.mcp_infra_server.mcp.model.McpRequest;
import com.devops.mcp_infra_server.mcp.model.McpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class McpWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(McpWebSocketHandler.class);

    @Autowired
    private McpServerService mcpServerService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info("MCP client connected: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        logger.debug("Received MCP message: {}", payload);

        try {
            // Parse MCP request
            McpRequest request = objectMapper.readValue(payload, McpRequest.class);
            logger.info("Processing MCP request: {} (ID: {})", request.getMethod(), request.getId());

            // Handle the request
            McpResponse response = mcpServerService.handleRequest(request);

            // Send response
            String responseJson = objectMapper.writeValueAsString(response);
            session.sendMessage(new TextMessage(responseJson));
            logger.debug("Sent MCP response: {}", responseJson);

        } catch (Exception e) {
            logger.error("Error handling MCP message", e);

            // Send error response
            McpResponse errorResponse = McpResponse.error("unknown", "Failed to process request: " + e.getMessage());
            String errorJson = objectMapper.writeValueAsString(errorResponse);
            session.sendMessage(new TextMessage(errorJson));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.error("WebSocket transport error for session {}: {}", session.getId(), exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        logger.info("MCP client disconnected: {} (Status: {})", session.getId(), closeStatus);
    }
}
