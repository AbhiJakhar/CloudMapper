package com.devops.mcp_infra_server.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CloudProvider {
    @NotBlank(message = "Provider name is required")
    private String name;

    @NotBlank(message = "Region is required")
    private String region;

    @NotNull(message = "Active status is required")
    private Boolean active;

    private String description;

    public  CloudProvider() {}

    public CloudProvider(String name, String region,Boolean active, String description) {
        this.name = name;
        this.region = region;
        this.active = active;
        this.description = description;
    }

    public String getName() {return name; }
    public void setName(String name){this.name=name; }

    public String getRegion() {return region; }
    public void setRegion(String region) {this.region=region; }

    public Boolean getActive() {return active; }
    public void setActive() {this.active=active; }

    public String getDescription(){return description; }
    public void setDescription(String description){ this.description=description; }

    @Override
    public String toString() {
        return "CloudProvider{" +
                "name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", active=" + active +
                ", description='" + description + '\'' +
                '}';
    }



}
