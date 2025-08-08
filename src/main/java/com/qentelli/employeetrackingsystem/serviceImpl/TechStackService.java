package com.qentelli.employeetrackingsystem.serviceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.qentelli.employeetrackingsystem.entity.TechStack;

@Service
public class TechStackService {

	public List<String> getAllTechStacks() {
        return Arrays.stream(TechStack.values())
                     .map(Enum::name) // Converts enum to its name as String
                     .collect(Collectors.toList());
    }

}
