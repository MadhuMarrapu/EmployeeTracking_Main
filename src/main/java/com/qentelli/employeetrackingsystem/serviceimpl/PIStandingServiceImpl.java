package com.qentelli.employeetrackingsystem.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

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

		if (dto.getSelectedSprint() == null || dto.getSelectedSprint().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one sprint must be selected");
		}

		if (dto.getPiNumber() < 1 || dto.getPiNumber() > 4) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "piNumber must be between 1 and 4");
		}

		Project project = projectRepo.findById(dto.getProjectId()).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found " + dto.getProjectId()));

		PIStanding e = new PIStanding();
		e.setPiNumber(dto.getPiNumber());
		e.setProject(project);
		e.setFeature(dto.getFeature());
		e.setCompletionPercentage(dto.getCompletionPercentage());
		e.setStatusReport(dto.getStatusReport());

		// Set currentSprint to first selected sprint
		String firstSprint = normalizeSprint(dto.getSelectedSprint().get(0));
		if (!isValidSprint(firstSprint)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid sprint: " + firstSprint);
		}
		e.setCurrentSprint(SprintOrdinal.valueOf(firstSprint.replace("-", "_")));

		PIStanding saved = repo.save(e);
		return toResponse(saved, dto.getSelectedSprint());
	}

	@Override
	public PIStandingResponse update(Long id, PIStandingRequest dto) {
		PIStanding existing = repo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Standing not found " + id));

		if (dto.getSelectedSprint() == null || dto.getSelectedSprint().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one sprint must be selected");
		}

		if (dto.getPiNumber() < 1 || dto.getPiNumber() > 4) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "piNumber must be between 1 and 4");
		}

		Project project = projectRepo.findById(dto.getProjectId()).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found " + dto.getProjectId()));

		existing.setPiNumber(dto.getPiNumber());
		existing.setProject(project);
		existing.setFeature(dto.getFeature());
		existing.setCompletionPercentage(dto.getCompletionPercentage());
		existing.setStatusReport(dto.getStatusReport());

		String firstSprint = normalizeSprint(dto.getSelectedSprint().get(0));
		if (!isValidSprint(firstSprint)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid sprint: " + firstSprint);
		}
		existing.setCurrentSprint(SprintOrdinal.valueOf(firstSprint.replace("-", "_")));

		PIStanding saved = repo.save(existing);
		return toResponse(saved, dto.getSelectedSprint());
	}

	@Override
	public PIStandingResponse get(Long id) {
		PIStanding e = repo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Standing not found " + id));
		return toResponse(e, List.of(formatSprintOrdinal(e.getCurrentSprint())));
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
		return repo.findBySoftDeleteFalse(pg)
				.map(e -> toResponse(e, List.of(formatSprintOrdinal(e.getCurrentSprint()))));
	}

	@Override
	public List<PIStandingResponse> list() {
		return repo.findBySoftDeleteFalse().stream()
				.map(e -> toResponse(e, List.of(formatSprintOrdinal(e.getCurrentSprint()))))
				.collect(Collectors.toList());
	}

	@Override
	public Page<PIStandingResponse> listByPi(int pi, Pageable pg) {
		if (pi < 1 || pi > 4) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "piNumber must be between 1 and 4");
		}
		return repo.findByPiNumberAndSoftDeleteFalse(pi, pg)
				.map(e -> toResponse(e, List.of(formatSprintOrdinal(e.getCurrentSprint()))));
	}

	@Override
	public Page<PIStandingResponse> listByProject(int projId, Pageable pg) {
		return repo.findByProject_ProjectIdAndSoftDeleteFalse(projId, pg)
				.map(e -> toResponse(e, List.of(formatSprintOrdinal(e.getCurrentSprint()))));
	}

	// Converts entity to response DTO with sprint flags
	private PIStandingResponse toResponse(PIStanding e, List<String> selectedSprints) {
		List<String> normalized = selectedSprints.stream().map(this::normalizeSprint).collect(Collectors.toList());

		return new PIStandingResponse(e.getId(), e.getPiNumber(), e.getProject().getProjectId(),
				e.getProject().getProjectName(), e.getFeature(), formatSprintOrdinal(e.getCurrentSprint()),
				normalized.contains("SPRINT-0"), normalized.contains("SPRINT-1"), normalized.contains("SPRINT-2"),
				normalized.contains("SPRINT-3"), normalized.contains("SPRINT-4"), e.getCompletionPercentage(),
				e.getStatusReport());
	}

	private String normalizeSprint(String sprint) {
		if (sprint == null)
			return null;
		return sprint.trim().toUpperCase().replace(" ", "").replace("_", "-");
	}

	private boolean isValidSprint(String sprint) {
		if (sprint == null)
			return false;
		return sprint.equalsIgnoreCase("Sprint-0") || sprint.equalsIgnoreCase("Sprint-1")
				|| sprint.equalsIgnoreCase("Sprint-2") || sprint.equalsIgnoreCase("Sprint-3")
				|| sprint.equalsIgnoreCase("Sprint-4");
	}

	private String formatSprintOrdinal(SprintOrdinal sprintOrdinal) {
		if (sprintOrdinal == null)
			return null;
		return "Sprint-" + sprintOrdinal.name().split("_")[1];
	}
}