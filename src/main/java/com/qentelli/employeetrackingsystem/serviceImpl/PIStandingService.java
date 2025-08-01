package com.qentelli.employeetrackingsystem.serviceImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.qentelli.employeetrackingsystem.entity.PIStanding;
import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.models.client.request.PIStandingRequest;
import com.qentelli.employeetrackingsystem.models.client.response.PIStandingResponse;
import com.qentelli.employeetrackingsystem.repository.PIStandingRepository;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PIStandingService {
  
	private final PIStandingRepository repo;
    private final ProjectRepository   projectRepo;
 
    public PIStandingResponse create(PIStandingRequest dto) {
        if (dto.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "New record must not contain id");
        }
        return persist(new PIStanding(), dto);
    }
 
    public PIStandingResponse update(Long id, PIStandingRequest dto) {
        PIStanding existing = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Standing not found " + id));
 
        return persist(existing, dto);
    }
 
 
    public PIStandingResponse get(Long id) {
        return toResponse(repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Standing not found " + id)));
    }
 
   
    public Page<PIStandingResponse> list(Pageable pg) {
        return repo.findBySoftDeleteFalse(pg).map(this::toResponse);
    }
 
    public Page<PIStandingResponse> listByPi(int pi, Pageable pg) {
        return repo.findByPiNumberAndSoftDeleteFalse(pi, pg).map(this::toResponse);
    }
 
    public Page<PIStandingResponse> listByProject(int projId, Pageable pg) {
        return repo.findByProject_ProjectIdAndSoftDeleteFalse(projId, pg).map(this::toResponse);
    }
 
   public void delete(Long id) {
        PIStanding e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Standing not found " + id));
        e.setSoftDelete(true);
        repo.save(e);
    }
 
    /** Common create / update logic. */
    private PIStandingResponse persist(PIStanding e, PIStandingRequest dto) {
 
        /* validate PI‑number range 1–4 */
        if (dto.getPiNumber() < 1 || dto.getPiNumber() > 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "piNumber must be between 1 and 4");
        }
 
        /* pull project */
        Project project = projectRepo.findById(dto.getProjectId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project not found " + dto.getProjectId()));
 
        /* copy scalar fields */
        e.setPiNumber(dto.getPiNumber());
        e.setProject(project);
        e.setFeature(dto.getFeature());
 
        e.setSprint0(dto.isSprint0());
        e.setSprint1(dto.isSprint1());
        e.setSprint2(dto.isSprint2());
        e.setSprint3(dto.isSprint3());
        e.setSprint4(dto.isSprint4());
 
        e.setCompletionPercentage(dto.getCompletionPercentage());
        e.setStatusReport(dto.getStatusReport());
 
        return toResponse(repo.save(e));
    }
 
    /** entity → response DTO */
    private PIStandingResponse toResponse(PIStanding e) {
        return new PIStandingResponse(
                e.getId(),
                e.getPiNumber(),
                e.getProject().getProjectId(),
                e.getProject().getProjectName(),
                e.getFeature(),
                e.isSprint0(), e.isSprint1(), e.isSprint2(), e.isSprint3(), e.isSprint4(),
                e.getCompletionPercentage(),
                e.getStatusReport()
        );
    }        
}
