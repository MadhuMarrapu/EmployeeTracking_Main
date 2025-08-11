package com.qentelli.employeetrackingsystem.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qentelli.employeetrackingsystem.models.client.request.PIStandingRequest;
import com.qentelli.employeetrackingsystem.models.client.response.PIStandingResponse;

public interface PIStandingService {

	public PIStandingResponse create(PIStandingRequest dto);
	public PIStandingResponse update(Long id, PIStandingRequest dto);
	public PIStandingResponse get(Long id);
	public Page<PIStandingResponse> list(Pageable pg);
	public List<PIStandingResponse> list();
	public Page<PIStandingResponse> listByPi(int pi, Pageable pg);
	public Page<PIStandingResponse> listByProject(int projId, Pageable pg);
	public void delete(Long id);
}