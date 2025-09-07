package com.devops.mcp_infra_server.mcp.tool;

import com.devops.mcp_infra_server.model.CloudProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class ListProvidersTools implements McpTool {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Same sample data as your controller (we'll improve this later)
    private final List<CloudProvider> providers = new ArrayList<>();

    public ListProvidersTools() {
        providers.add(new CloudProvider("aws", "us-east-1", true, "Amazon Web Services"));
        providers.add(new CloudProvider("azure", "eastus", true, "Microsoft Azure"));
        providers.add(new CloudProvider("gcp", "us-central1", false, "Google Cloud Platform"));
    }

    @Override
    public String getName() {
        return "list_cloud_providers";
    }

    @Override
    public String getDescription() {
        return "List all configured cloud providers and their status";
    }

    @Override
    public JsonNode getInputSchema() {
        ObjectNode schema = objectMapper.createObjectNode();
        schema.put("type", "object");

        ObjectNode properties = objectMapper.createObjectNode();

        // Optional filter parameter
        ObjectNode providerFilter = objectMapper.createObjectNode();
        providerFilter.put("type", "string");
        providerFilter.put("description", "Filter by provider name (optional)");
        properties.set("provider", providerFilter);

        ObjectNode activeFilter = objectMapper.createObjectNode();
        activeFilter.put("type", "boolean");
        activeFilter.put("description", "Filter by active status (optional)");
        properties.set("active", activeFilter);

        schema.set("properties", properties);

        // Final schema
        // {
        //  "type": "object",
        //  "properties": {
        //    "provider": {
        //      "type": "string",
        //      "description": "Filter by provider name (optional)"
        //    },
        //    "active": {
        //      "type": "boolean",
        //      "description": "Filter by active status (optional)"
        //    }
        //  }
        //}
        return schema;
    }

    @Override
    public CompletableFuture<McpToolResult> execute(JsonNode parameters) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<CloudProvider> filteredProviders = new ArrayList<>(providers);

                // Apply filters if provided
                if (parameters != null && parameters.has("provider")) {
                    String providerFilter = parameters.get("provider").asText();
                    filteredProviders.removeIf(p ->
                            !p.getName().toLowerCase().contains(providerFilter.toLowerCase()));
                }

                if (parameters != null && parameters.has("active")) {
                    boolean activeFilter = parameters.get("active").asBoolean();
                    filteredProviders.removeIf(p -> p.getActive() != activeFilter);
                }

                return McpToolResult.success(filteredProviders);

            } catch (Exception e) {
                return McpToolResult.error("Failed to list providers: " + e.getMessage());
            }
        });
    }
}