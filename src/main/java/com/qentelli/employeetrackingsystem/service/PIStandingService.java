package com.qentelli.employeetrackingsystem.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qentelli.employeetrackingsystem.models.client.request.PIStandingRequest;
import com.qentelli.employeetrackingsystem.models.client.response.PIStandingResponse;

public interface PIStandingService {

    PIStandingResponse create(PIStandingRequest dto);
    PIStandingResponse update(Long id, PIStandingRequest dto);
    PIStandingResponse get(Long id);
    Page<PIStandingResponse> list(Pageable pg);
    List<PIStandingResponse> list();
    Page<PIStandingResponse> listByPi(int pi, Pageable pg);
    Page<PIStandingResponse> listByProject(int projId, Pageable pg);
    void delete(Long id);
}