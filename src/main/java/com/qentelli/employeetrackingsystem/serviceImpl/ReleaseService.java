package com.qentelli.employeetrackingsystem.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.Release;
import com.qentelli.employeetrackingsystem.entity.WeekRange;
import com.qentelli.employeetrackingsystem.exception.ResourceNotFoundException;
import com.qentelli.employeetrackingsystem.models.client.request.ReleaseRequestDTO;
import com.qentelli.employeetrackingsystem.models.client.response.ReleaseResponseDTO;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;
import com.qentelli.employeetrackingsystem.repository.ReleaseRepository;
import com.qentelli.employeetrackingsystem.repository.WeekRangeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReleaseService {

    private static final Logger logger = LoggerFactory.getLogger(ReleaseService.class);

    private final ReleaseRepository releaseRepository;
    private final ProjectRepository projectRepository;
    private final WeekRangeRepository weekRangeRepository;

    // ✅ Create
    public Release createRelease(ReleaseRequestDTO dto) {
        logger.info("Creating release for projectId={}, weekId={}", dto.getProjectId(), dto.getWeekId());

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        WeekRange week = weekRangeRepository.findById(dto.getWeekId())
                .orElseThrow(() -> new ResourceNotFoundException("WeekRange not found"));

        Release release = new Release();
        release.setProject(project);
        release.setWeek(week);
        release.setMajor(dto.getMajor());
        release.setMinor(dto.getMinor());
        release.setIncidentCreated(dto.getIncidentCreated());
        release.setReleaseInformation(dto.getReleaseInformation());

        Release savedRelease = releaseRepository.save(release);
        logger.info("Release created with ID: {}", savedRelease.getReleaseId());

        return savedRelease;
    }

    // ✅ Read All
    public List<ReleaseResponseDTO> getAllReleases() {
        logger.info("Fetching all releases");
        List<ReleaseResponseDTO> list = releaseRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        logger.info("Total releases found: {}", list.size());
        return list;
    }

    // ✅ Read by ID
    public ReleaseResponseDTO getReleaseById(Long id) {
        logger.info("Fetching release by ID: {}", id);
        Release release = releaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Release not found"));
        return mapToResponseDTO(release);
    }

    // ✅ Update
    public Release updateRelease(Long id, ReleaseRequestDTO dto) {
        logger.info("Updating release with ID: {}", id);

        Release release = releaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Release not found"));

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        WeekRange week = weekRangeRepository.findById(dto.getWeekId())
                .orElseThrow(() -> new ResourceNotFoundException("WeekRange not found"));

        release.setProject(project);
        release.setWeek(week);
        release.setMajor(dto.getMajor());
        release.setMinor(dto.getMinor());
        release.setIncidentCreated(dto.getIncidentCreated());
        release.setReleaseInformation(dto.getReleaseInformation());

        Release updatedRelease = releaseRepository.save(release);
        logger.info("Release updated successfully. ID: {}", updatedRelease.getReleaseId());

        return updatedRelease;
    }

    // ✅ Delete
    public void deleteRelease(Long id) {
        logger.info("Attempting to delete release ID: {}", id);
        if (!releaseRepository.existsById(id)) {
            logger.warn("Release not found for deletion. ID: {}", id);
            throw new ResourceNotFoundException("Release not found");
        }
        releaseRepository.deleteById(id);
        logger.info("Release deleted. ID: {}", id);
    }

    // 🧭 Mapper
    private ReleaseResponseDTO mapToResponseDTO(Release release) {
        return new ReleaseResponseDTO(
                release.getProject().getProjectName(),
                release.getMajor(),
                release.getMinor(),
                release.getIncidentCreated(),
                release.getReleaseInformation()
        );
    }
}