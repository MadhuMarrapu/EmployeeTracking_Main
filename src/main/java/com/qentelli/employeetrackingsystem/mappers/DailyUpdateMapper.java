/*
package com.qentelli.employeetrackingsystem.mappers;

import com.qentelli.employeetrackingsystem.entity.DailyUpdate;
import com.qentelli.employeetrackingsystem.entity.Employee;
import com.qentelli.employeetrackingsystem.models.client.request.DailyUpdateDTO;

import java.util.ArrayList;
import java.util.List;

public abstract class DailyUpdateMapper {

    public static DailyUpdateDTO toDTO(DailyUpdate entity) {
        if (entity == null) return null;

        DailyUpdateDTO dto = new DailyUpdateDTO();
        dto.setId(entity.getId());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setTask(entity.getTask());
        dto.setStatus(entity.getStatus());
        dto.setKeyAccomplishments(entity.getKeyAccomplishments());
        dto.setEmployeeId(entity.getEmployee() != null ? entity.getEmployee().getEmployeeId() : null);
        return dto;
    }

    public static DailyUpdate toEntity(DailyUpdateDTO dto) {
        if (dto == null) return null;

        DailyUpdate entity = new DailyUpdate();
        entity.setId(dto.getId());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setTask(dto.getTask());
        entity.setStatus(dto.getStatus());
        entity.setKeyAccomplishments(dto.getKeyAccomplishments());

        if (dto.getEmployeeId() != null) {
            Employee emp = new Employee();
            emp.setEmployeeId(dto.getEmployeeId());
            entity.setEmployee(emp);
        }

        return entity;
    }

    public static List<DailyUpdateDTO> toDTOList(List<DailyUpdate> entities) {
        if (entities == null) return null;

        List<DailyUpdateDTO> dtoList = new ArrayList<>();
        for (DailyUpdate entity : entities) {
            dtoList.add(toDTO(entity));
        }
        return dtoList;
    }

    public static List<DailyUpdate> toEntityList(List<DailyUpdateDTO> dtos) {
        if (dtos == null) return null;

        List<DailyUpdate> entities = new ArrayList<>();
        for (DailyUpdateDTO dto : dtos) {
            entities.add(toEntity(dto));
        }
        return entities;
    }
}
*/