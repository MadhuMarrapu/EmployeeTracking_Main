package com.qentelli.employeetrackingsystem.serviceimpl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.qentelli.employeetrackingsystem.entity.PIStanding;
import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.enums.SprintOrdinal;
import com.qentelli.employeetrackingsystem.models.client.request.PIStandingRequest;
import com.qentelli.employeetrackingsystem.models.client.response.PIStandingResponse;
import com.qentelli.employeetrackingsystem.repository.PIStandingRepository;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;
import com.qentelli.employeetrackingsystem.service.PIStandingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PIStandingServiceImpl implements PIStandingService {

    private final PIStandingRepository repo;
    private final ProjectRepository projectRepo;

    @Override
    public PIStandingResponse create(PIStandingRequest dto) {
        if (dto.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New record must not contain id");
        }
        return persist(new PIStanding(), dto);
    }

    @Override
    public PIStandingResponse update(Long id, PIStandingRequest dto) {
        PIStanding existing = repo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Standing not found " + id));
        return persist(existing, dto);
    }

    @Override
    public PIStandingResponse get(Long id) {
        return toResponse(repo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Standing not found " + id)));
    }

    @Override
    public void delete(Long id) {
        PIStanding e = repo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Standing not found " + id));
        e.setSoftDelete(true);
        repo.save(e);
    }

    @Override
    public Page<PIStandingResponse> list(Pageable pg) {
        return repo.findBySoftDeleteFalse(pg).map(this::toResponse);
    }

    @Override
    public List<PIStandingResponse> list() {
        return repo.findBySoftDeleteFalse().stream().map(this::toResponse).toList();
    }

    @Override
    public Page<PIStandingResponse> listByPi(int pi, Pageable pg) {
        if (pi < 1 || pi > 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "piNumber must be between 1 and 4");
        }
        return repo.findByPiNumberAndSoftDeleteFalse(pi, pg).map(this::toResponse);
    }

    @Override
    public Page<PIStandingResponse> listByProject(int projId, Pageable pg) {
        return repo.findByProject_ProjectIdAndSoftDeleteFalse(projId, pg).map(this::toResponse);
    }

    // Core persistence logic
    private PIStandingResponse persist(PIStanding e, PIStandingRequest dto) {
        if (dto.getPiNumber() < 1 || dto.getPiNumber() > 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "piNumber must be between 1 and 4");
        }

        String sprintKey = normalizeSprint(dto.getSelectedSprint());
        if (!isValidSprint(sprintKey)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "selectedSprint must be one of: Sprint-0 to Sprint-4");
        }

        Project project = projectRepo.findById(dto.getProjectId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found " + dto.getProjectId()));

        e.setPiNumber(dto.getPiNumber());
        e.setProject(project);
        e.setFeature(dto.getFeature());
        e.setCompletionPercentage(dto.getCompletionPercentage());
        e.setStatusReport(dto.getStatusReport());

        // Set enum field only
        e.setCurrentSprint(SprintOrdinal.valueOf(sprintKey.replace("-", "_")));

        return toResponse(repo.save(e));
    }

    // Converts entity to response DTO with derived sprint flags
    private PIStandingResponse toResponse(PIStanding e) {
        SprintOrdinal sprint = e.getCurrentSprint();

        return new PIStandingResponse(
            e.getId(),
            e.getPiNumber(),
            e.getProject().getProjectId(),
            e.getProject().getProjectName(),
            e.getFeature(),
            formatSprintOrdinal(sprint),
            sprint == SprintOrdinal.SPRINT_0,
            sprint == SprintOrdinal.SPRINT_1,
            sprint == SprintOrdinal.SPRINT_2,
            sprint == SprintOrdinal.SPRINT_3,
            sprint == SprintOrdinal.SPRINT_4,
            e.getCompletionPercentage(),
            e.getStatusReport()
        );
    }

    // Normalizes input like "sprint 1", "SPRINT_2" → "SPRINT-2"
    private String normalizeSprint(String sprint) {
        if (sprint == null) return null;
        return sprint.trim().toUpperCase().replace(" ", "").replace("_", "-");
    }

    // Validates normalized sprint string
    private boolean isValidSprint(String sprint) {
        if (sprint == null) return false;
        return sprint.equalsIgnoreCase("Sprint-0") ||
               sprint.equalsIgnoreCase("Sprint-1") ||
               sprint.equalsIgnoreCase("Sprint-2") ||
               sprint.equalsIgnoreCase("Sprint-3") ||
               sprint.equalsIgnoreCase("Sprint-4");
    }

    // Converts enum to frontend-friendly format
    private String formatSprintOrdinal(SprintOrdinal sprintOrdinal) {
        if (sprintOrdinal == null) return null;
        return "Sprint-" + sprintOrdinal.name().split("_")[1];
    }
}