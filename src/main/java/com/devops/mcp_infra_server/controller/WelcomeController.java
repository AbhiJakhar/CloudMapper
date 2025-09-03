package com.devops.mcp_infra_server.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class WelcomeController {

    @Value("${mcp.server.name}")
    private String serverName;

    @Value("${mcp.server.version}")
    private String serverVersion;

    @Value("${mcp.server.description}")
    private String serverDescription;

    @GetMapping("/")
    public Map<String, Object> welcome() {
        Map<String, Object> response =new HashMap<>();
        response.put("name", serverName);
        response.put("version",serverVersion);
        response.put("description",serverDescription);
        response.put("status", "running");
        response.put("endpoints", new String[]{
                "GET /api/providers - List cloud providers",
                "POST /api/providers - Add cloud provider",
                "GET /api/providers/{name} - Get specific provider"
        });
        return response;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "mcp-infra-server");
        return response;
    }
}
