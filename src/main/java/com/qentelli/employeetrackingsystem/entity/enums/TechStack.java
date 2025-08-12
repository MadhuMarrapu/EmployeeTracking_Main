package com.qentelli.employeetrackingsystem.entity.enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TechStack {
	FRONTEND, BACKEND, FULL_STACK, TESTING, UI, UX, JAVA, RPA;

	@JsonValue
	public String getDisplayName() {
		// Converts "FULL_STACK" → "Full Stack", "UX" → "Ux"
		String name = name().replace("_", " ").toLowerCase();
		String[] parts = name.split(" ");
		StringBuilder formatted = new StringBuilder();
		for (String part : parts) {
			formatted.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1)).append(" ");
		}
		return formatted.toString().trim();
	}

	@JsonCreator
	public static TechStack fromString(String value) {
		return Arrays.stream(TechStack.values()).filter(stack -> stack.name().equalsIgnoreCase(value.replace(" ", "_")))
				.findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid tech stack value: " + value));
	}

}
