
package com.qentelli.employeetrackingsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;
    private String task;
    private String status;

    @ElementCollection
    @CollectionTable(name = "daily_update_accomplishments", joinColumns = @JoinColumn(name = "daily_update_id"))
    @Column(name = "accomplishment")
    private List<String> keyAccomplishments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;
}

