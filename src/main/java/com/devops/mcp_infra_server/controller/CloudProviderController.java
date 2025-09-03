package com.devops.mcp_infra_server.controller;


import com.devops.mcp_infra_server.model.CloudProvider;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/providers")
public class CloudProviderController {
    private final List<CloudProvider> providers = new ArrayList<>();

    public CloudProviderController() {
        providers.add(new CloudProvider("aws", "us-east-1", true, "Amazon Web Services"));
        providers.add(new CloudProvider("azure", "eastus", true, "Microsoft Azure"));
        providers.add(new CloudProvider("gcp", "us-central1", true, "Google Cloud Platform"));
    }

    @GetMapping
    public List<CloudProvider> getAllProviders() {
        return providers;
    }

    @GetMapping("/{name}")
    public ResponseEntity<CloudProvider> getProvider(@PathVariable String name) {
        Optional<CloudProvider> provider = providers.stream().filter(p -> p.getName().equalsIgnoreCase(name)).findFirst();
        if(provider.isPresent()) {
            return ResponseEntity.ok(provider.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<CloudProvider> addprovider(@Valid @RequestBody CloudProvider provider) {
        boolean exists = providers.stream().anyMatch(p -> p.getName().equalsIgnoreCase(provider.getName()));
        if(exists) {
            return ResponseEntity.badRequest().build();
        }
        providers.add(provider);
        return ResponseEntity.ok(provider);
    }

    @PutMapping("/{name}")
    public ResponseEntity<CloudProvider> updateProvider(
            @PathVariable String name,
            @Valid @RequestBody CloudProvider updatedProvider) {
        System.out.println("Updating provider: " + name + " with data: " + updatedProvider);
        for (int i = 0; i < providers.size(); i++) {
            if (providers.get(i).getName().equalsIgnoreCase(name)) {
                System.out.println("Updating provider: " + name + " with data: " + updatedProvider);
                updatedProvider.setName(name); // Keep the original name
                System.out.println("Updating provider: " + name + " with data: " + updatedProvider);
                providers.set(i, updatedProvider);
                return ResponseEntity.ok(updatedProvider);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteProvider(@PathVariable String name) {
        boolean removed = providers.removeIf(p -> p.getName().equalsIgnoreCase(name));

        if (removed) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
