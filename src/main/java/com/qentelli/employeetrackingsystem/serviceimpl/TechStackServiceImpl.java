package com.qentelli.employeetrackingsystem.serviceimpl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.enums.TechStack;
import com.qentelli.employeetrackingsystem.service.TechStackService;

@Service
public class TechStackServiceImpl implements TechStackService {

	@Override
	public List<String> getAllTechStacks() {
		return Arrays.stream(TechStack.values()).map(stack -> stack.getDisplayName().toUpperCase()).toList();
	}
}