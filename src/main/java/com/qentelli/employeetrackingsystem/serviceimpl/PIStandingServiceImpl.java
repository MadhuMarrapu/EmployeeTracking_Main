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
import com.qentelli.employeetrackingsystem.entity.enums.StatusFlag;
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

        List<SprintOrdinal> sprintEnums = mapToSprintOrdinals(dto.getSelectedSprint());

        if (sprintEnums.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one sprint must be selected");
        }

        if (dto.getPiNumber() < 1 || dto.getPiNumber() > 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "piNumber must be between 1 and 4");
        }

        Project project = projectRepo.findById(dto.getProjectId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found " + dto.getProjectId()));

        PIStanding e = new PIStanding();
        e.setPiNumber(dto.getPiNumber());
        e.setProject(project);
        e.setFeature(dto.getFeature());
        e.setCompletionPercentage(dto.getCompletionPercentage());
        e.setStatusReport(dto.getStatusReport());
        e.setSelectedSprints(sprintEnums);
        e.setStatusFlag(StatusFlag.ACTIVE); // ✅ set lifecycle status

        PIStanding saved = repo.save(e);
        return toResponse(saved);
    }

    @Override
    public PIStandingResponse update(Long id, PIStandingRequest dto) {
        PIStanding existing = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Standing not found " + id));

        List<SprintOrdinal> sprintEnums = mapToSprintOrdinals(dto.getSelectedSprint());

        if (sprintEnums.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one sprint must be selected");
        }

        if (dto.getPiNumber() < 1 || dto.getPiNumber() > 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "piNumber must be between 1 and 4");
        }

        Project project = projectRepo.findById(dto.getProjectId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found " + dto.getProjectId()));

        existing.setPiNumber(dto.getPiNumber());
        existing.setProject(project);
        existing.setFeature(dto.getFeature());
        existing.setCompletionPercentage(dto.getCompletionPercentage());
        existing.setStatusReport(dto.getStatusReport());
        existing.setSelectedSprints(sprintEnums);
   

        PIStanding saved = repo.save(existing);
        return toResponse(saved);
    }

    @Override
    public PIStandingResponse get(Long id) {
        PIStanding e = repo.findById(id)
                .filter(p -> p.getStatusFlag() == StatusFlag.ACTIVE)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Standing not found " + id));
        return toResponse(e);
    }

    @Override
    public void delete(Long id) {
        PIStanding e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Standing not found " + id));
        e.setStatusFlag(StatusFlag.INACTIVE); // ✅ soft delete
        repo.save(e);
    }

    @Override
    public Page<PIStandingResponse> list(Pageable pg) {
        return repo.findByStatusFlag(StatusFlag.ACTIVE, pg).map(this::toResponse);
    }

    @Override
    public List<PIStandingResponse> list() {
        return repo.findByStatusFlag(StatusFlag.ACTIVE).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public Page<PIStandingResponse> listByPi(int pi, Pageable pg) {
        if (pi < 1 || pi > 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "piNumber must be between 1 and 4");
        }
        return repo.findByPiNumberAndStatusFlag(pi, StatusFlag.ACTIVE, pg).map(this::toResponse);
    }

    @Override
    public Page<PIStandingResponse> listByProject(int projId, Pageable pg) {
        return repo.findByProject_ProjectIdAndStatusFlag(projId, StatusFlag.ACTIVE, pg).map(this::toResponse);
    }

    private PIStandingResponse toResponse(PIStanding e) {
        List<SprintOrdinal> sprints = e.getSelectedSprints();

        List<String> clientFormattedSprints = sprints.stream()
                .map(s -> s.name().replace("SPRINT_", "Sprint-"))
                .toList();

        return new PIStandingResponse(
                e.getId(),
                e.getPiNumber(),
                e.getProject().getProjectId(),
                e.getProject().getProjectName(),
                e.getFeature(),
                sprints.contains(SprintOrdinal.SPRINT_0),
                sprints.contains(SprintOrdinal.SPRINT_1),
                sprints.contains(SprintOrdinal.SPRINT_2),
                sprints.contains(SprintOrdinal.SPRINT_3),
                sprints.contains(SprintOrdinal.SPRINT_4),
                e.getCompletionPercentage(),
                e.getStatusReport(),
                clientFormattedSprints,
                e.getStatusFlag() // ✅ include lifecycle status in response
        );
    }

    private List<SprintOrdinal> mapToSprintOrdinals(List<String> rawSprints) {
        if (rawSprints == null) return List.of();

        return rawSprints.stream().map(s -> {
            try {
                return SprintOrdinal.valueOf(s.trim().toUpperCase().replace("-", "_"));
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid sprint value: " + s);
            }
        }).toList();
    }
}