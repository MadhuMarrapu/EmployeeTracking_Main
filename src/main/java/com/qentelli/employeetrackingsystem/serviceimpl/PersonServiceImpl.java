package com.qentelli.employeetrackingsystem.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qentelli.employeetrackingsystem.entity.Person;
import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.enums.Roles;
import com.qentelli.employeetrackingsystem.entity.enums.Status;
import com.qentelli.employeetrackingsystem.exception.DuplicatePersonException;
import com.qentelli.employeetrackingsystem.exception.PersonNotFoundException;
import com.qentelli.employeetrackingsystem.models.client.request.PersonDTO;
import com.qentelli.employeetrackingsystem.repository.PersonRepository;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;
import com.qentelli.employeetrackingsystem.service.PersonService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private static final String PERSON_NOT_FOUND = "Person not found";

    private final PersonRepository personRepo;
    private final ProjectRepository projectRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PersonDTO create(PersonDTO dto) {
        boolean exists = personRepo.existsByEmail(dto.getEmail())
                || personRepo.existsByEmployeeCode(dto.getEmployeeCode());
        if (exists) {
            throw new DuplicatePersonException("Person with this email or employee code already exists");
        }

        Person person = modelMapper.map(dto, Person.class);
        person.setPassword(passwordEncoder.encode(dto.getPassword()));
        person.setConfirmPassword(passwordEncoder.encode(dto.getConfirmPassword()));
        person.setStatusFlag(Status.ACTIVE);
        if (dto.getProjectIds() != null && !dto.getProjectIds().isEmpty()) {
            List<Project> projects = projectRepo.findAllById(dto.getProjectIds());
            List<Integer> foundIds = projects.stream().map(Project::getProjectId).toList();
            List<Integer> missingIds = dto.getProjectIds().stream().filter(id -> !foundIds.contains(id)).toList();
            if (!missingIds.isEmpty()) {
                throw new IllegalArgumentException("Invalid project IDs: " + missingIds);
            }
            person.setProjects(projects);
        }

        if (dto.getTechStack() != null) {
            person.setTechStack(dto.getTechStack());
        }

        Person saved = personRepo.save(person);
        return convertToDTO(saved);
    }

    @Override
    public void tagProjectsToEmployee(Integer personId, List<Integer> projectIds) {
        Person person = personRepo.findById(personId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        List<Project> projectsToAdd = projectRepo.findAllById(projectIds);
        List<Project> existingProjects = person.getProjects();
        for (Project p : projectsToAdd) {
            if (!existingProjects.contains(p)) {
                existingProjects.add(p);
            }
        }
        personRepo.save(person);
    }

    @Override
    public Page<PersonDTO> searchPersonsByName(String name, Pageable pageable) {
        Page<Person> page = personRepo.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseAndStatusFlag(
                name, name, Status.ACTIVE, pageable);
        return page.map(this::convertToDTO);
    }

    @Override
    public List<PersonDTO> getAllResponses() {
        return personRepo.findByStatusFlag(Status.ACTIVE).stream()
                .map(this::convertToDTO).toList();
    }

    @Override
    public PersonDTO getByIdResponse(Integer id) {
        return personRepo.findById(id)
                .filter(p -> p.getStatusFlag() == Status.ACTIVE)
                .map(this::convertToDTO)
                .orElseThrow(() -> new PersonNotFoundException(PERSON_NOT_FOUND));
    }

    @Override
    public List<PersonDTO> getByRoleResponse(Roles role) {
        return personRepo.findByRole(role).stream()
                .filter(p -> p.getStatusFlag() == Status.ACTIVE)
                .map(this::convertToDTO).toList();
    }

    @Override
    public Page<PersonDTO> getByRoleResponse(Roles role, Pageable pageable) {
        Page<Person> page = personRepo.findByRoleAndStatusFlag(role, Status.ACTIVE, pageable);
        return page.map(this::convertToDTO);
    }

    @Override
    public Page<PersonDTO> getPersonsByProjectId(Integer projectId, Pageable pageable) {
        Page<Person> page = personRepo.findByProjects_ProjectIdAndStatusFlag(projectId, Status.ACTIVE, pageable);
        return page.map(this::convertToDTO);
    }

    @Override
    @Transactional
    public PersonDTO update(Integer id, PersonDTO dto) {
        Person person = personRepo.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(PERSON_NOT_FOUND + " with id : " + id));

        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setEmail(dto.getEmail());
        person.setEmployeeCode(dto.getEmployeeCode());
        person.setPassword(passwordEncoder.encode(dto.getPassword()));
        person.setConfirmPassword(passwordEncoder.encode(dto.getConfirmPassword()));
        person.setRole(dto.getRole());

        if (dto.getTechStack() != null) {
            person.setTechStack(dto.getTechStack());
        }

        if (dto.getProjectIds() != null) {
            List<Project> projects = projectRepo.findAllById(dto.getProjectIds());
            person.setProjects(projects);
        }

        Person saved = personRepo.save(person);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public void softDeletePersonById(Integer personId) {
        Person person = personRepo.findById(personId)
                .orElseThrow(() -> new PersonNotFoundException(PERSON_NOT_FOUND + " with id :" + personId));
        person.setStatusFlag(Status.INACTIVE);
        personRepo.save(person);
    }

    @Override
    public Person getPersonEntity(String email) {
        return personRepo.findByEmail(email)
                .filter(p -> p.getStatusFlag() == Status.ACTIVE)
                .orElseThrow(() -> new PersonNotFoundException("Person not found with email: " + email));
    }

    @Override
    public boolean isProjectExists(Integer projectId) {
        return projectRepo.existsById(projectId);
    }

    private PersonDTO convertToDTO(Person person) {
        PersonDTO dto = modelMapper.map(person, PersonDTO.class);
        List<Project> projects = person.getProjects();
        if (projects != null) {
            dto.setProjectIds(projects.stream().map(Project::getProjectId).toList());
            dto.setProjectNames(projects.stream().map(Project::getProjectName).toList());
        }
        return dto;
    }
}