package com.qentelli.employeetrackingsystem.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sprint")
public class Sprint {
  
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sprintId;

    private String sprintNumber;
    private String sprintName;
    private LocalDate fromDate;
    private LocalDate toDate;
   
    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WeekRange> weeks;
    
    
}
