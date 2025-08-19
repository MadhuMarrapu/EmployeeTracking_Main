package com.qentelli.employeetrackingsystem.serviceimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.Release;
import com.qentelli.employeetrackingsystem.entity.Sprint;
import com.qentelli.employeetrackingsystem.entity.WeekRange;
import com.qentelli.employeetrackingsystem.entity.enums.StatusFlag;
import com.qentelli.employeetrackingsystem.exception.ReleaseNotFoundException;
import com.qentelli.employeetrackingsystem.exception.ResourceNotFoundException;
import com.qentelli.employeetrackingsystem.models.client.request.ReleaseRequestDTO;
import com.qentelli.employeetrackingsystem.models.client.response.ReleaseResponseDTO;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;
import com.qentelli.employeetrackingsystem.repository.ReleaseRepository;
import com.qentelli.employeetrackingsystem.repository.SprintRepository;
import com.qentelli.employeetrackingsystem.repository.WeekRangeRepository;
import com.qentelli.employeetrackingsystem.service.ReleaseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReleaseServiceImpl implements ReleaseService {

    private static final Logger logger = LoggerFactory.getLogger(ReleaseServiceImpl.class);

    private final ReleaseRepository releaseRepository;
    private final ProjectRepository projectRepository;
    private final WeekRangeRepository weekRangeRepository;
    private final SprintRepository sprintRepository;

    @Override
    public Release createRelease(ReleaseRequestDTO dto) {
        logger.info("Creating release for projectId={}, weekId={}", dto.getProjectId(), dto.getWeekId());

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        WeekRange week = weekRangeRepository.findById(dto.getWeekId())
                .orElseThrow(() -> new ResourceNotFoundException("WeekRange not found"));
        Sprint sprint = sprintRepository.findById(dto.getSprintId())
                .orElseThrow(() -> new ResourceNotFoundException("Sprint not found"));

        Release release = new Release();
        release.setProject(project);
        release.setWeek(week);
        release.setSprint(sprint);
        release.setMajor(dto.getMajor());
        release.setMinor(dto.getMinor());
        release.setIncidentCreated(dto.getIncidentCreated());
        release.setReleaseInformation(dto.getReleaseInformation());
        release.setStatusFlag(StatusFlag.ACTIVE); // ✅ lifecycle

        Release savedRelease = releaseRepository.save(release);
        logger.info("Release created with ID: {}", savedRelease.getReleaseId());
        return savedRelease;
    }

    @Override
    public List<ReleaseResponseDTO> getAllReleases() {
        logger.info("Fetching all ACTIVE releases");
        List<Release> releases = releaseRepository.findByStatusFlag(StatusFlag.ACTIVE);
        return releases.stream().map(this::mapToResponseDTO).toList();
    }

    @Override
    public Page<ReleaseResponseDTO> getPaginatedReleases(Pageable pageable) {
        logger.info("Fetching paginated ACTIVE releases");
        Page<Release> releasePage = releaseRepository.findByStatusFlag(StatusFlag.ACTIVE, pageable);
        return releasePage.map(this::mapToResponseDTO);
    }

    @Override
    public List<ReleaseResponseDTO> getReleasesByWeekId(int weekId) {
        logger.info("Fetching ACTIVE releases for weekId: {}", weekId);
        List<Release> releases = releaseRepository.findByWeek_WeekIdAndStatusFlag(weekId, StatusFlag.ACTIVE);
        return releases.stream().map(this::mapToResponseDTO).toList();
    }

    @Override
    public List<ReleaseResponseDTO> getReleasesBySprintId(int sprintId) {
        logger.info("Fetching ACTIVE releases for sprintId: {}", sprintId);
        List<Release> releases = releaseRepository.findBySprint_SprintIdAndStatusFlag(sprintId, StatusFlag.ACTIVE);
        return releases.stream().map(this::mapToResponseDTO).toList();
    }

    @Override
    public Release updateRelease(Long id, ReleaseRequestDTO dto) {
        logger.info("Updating release with ID: {}", id);

        Release release = releaseRepository.findById(id)
                .orElseThrow(() -> new ReleaseNotFoundException("Release not found with ID: " + id));
        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ReleaseNotFoundException("Project not found with ID: " + dto.getProjectId()));
        WeekRange week = weekRangeRepository.findById(dto.getWeekId())
                .orElseThrow(() -> new ReleaseNotFoundException("WeekRange not found with ID: " + dto.getWeekId()));
        Sprint sprint = sprintRepository.findById(dto.getSprintId())
                .orElseThrow(() -> new ReleaseNotFoundException("Sprint not found with ID: " + dto.getSprintId()));

        release.setProject(project);
        release.setWeek(week);
        release.setSprint(sprint);
        release.setMajor(dto.getMajor());
        release.setMinor(dto.getMinor());
        release.setIncidentCreated(dto.getIncidentCreated());
        release.setReleaseInformation(dto.getReleaseInformation());
        

        Release updatedRelease = releaseRepository.save(release);
        logger.info("Release updated successfully. ID: {}", updatedRelease.getReleaseId());
        return updatedRelease;
    }

    @Override
    public void deleteRelease(Long id) {
        logger.info("Soft deleting release ID: {}", id);
        Release release = releaseRepository.findById(id)
                .orElseThrow(() -> new ReleaseNotFoundException("Release not found with ID: " + id));
        release.setStatusFlag(StatusFlag.INACTIVE); // ✅ soft delete
        releaseRepository.save(release);
        logger.info("Release marked as INACTIVE. ID: {}", id);
    }

    @Override
    public List<ReleaseResponseDTO> getReleasesByStatusFlag(StatusFlag statusFlag) {
        logger.info("Fetching releases with statusFlag: {}", statusFlag);
        return releaseRepository.findByStatusFlag(statusFlag).stream()
                .map(this::mapToResponseDTO).toList();
    }

    private ReleaseResponseDTO mapToResponseDTO(Release release) {
        return new ReleaseResponseDTO(
                release.getProject().getProjectName(),
                release.getMajor(),
                release.getMinor(),
                release.getIncidentCreated(),
                release.getReleaseInformation(),
                release.getWeek() != null ? release.getWeek().getWeekId() : 0,
                release.getSprint() != null ? release.getSprint().getSprintId() : 0,
                release.getReleaseId(),
                release.getProject().getProjectId(),
                release.getStatusFlag() // ✅ include lifecycle
        );
    }
}