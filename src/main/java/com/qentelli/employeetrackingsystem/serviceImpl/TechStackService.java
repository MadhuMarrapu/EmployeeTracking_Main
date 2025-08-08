package com.qentelli.employeetrackingsystem.serviceImpl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.TechStack;

@Service
public class TechStackService {

	public List<String> getAllTechStacks() {
        return Arrays.stream(TechStack.values())
        		.map(stack->stack.getDisplayName().toUpperCase())
                .toList();
    }

}
