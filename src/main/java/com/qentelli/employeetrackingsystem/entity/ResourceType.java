package com.qentelli.employeetrackingsystem.entity;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ResourceType {
    TECH_STACK, PROJECT;

    @JsonValue
    public String getDisplayName() {
        // Converts "TECH_STACK" → "Tech Stack"
        String name = name().replace("_", " ").toLowerCase();
        String[] parts = name.split(" ");
        StringBuilder formatted = new StringBuilder();
        for (String part : parts) {
            formatted.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1)).append(" ");
        }
        return formatted.toString().trim();
    }

    @JsonCreator
    public static ResourceType fromString(String value) {
        return Arrays.stream(ResourceType.values())
                .filter(type -> type.name().equalsIgnoreCase(value.replace(" ", "_")))
                .findFirst()
                .orElseThrow(() ->
                    new IllegalArgumentException("Invalid resource type value: " + value +
                        ". Valid values: " + Arrays.toString(ResourceType.values()))
                );
    }
}
