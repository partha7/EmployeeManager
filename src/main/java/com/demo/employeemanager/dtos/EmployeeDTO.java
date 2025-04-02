package com.demo.employeemanager.dtos;

import com.demo.employeemanager.enums.Department;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private Long id;
    private String name;
    private String email;
    private Integer age;
    private Double salary;
    private LocalDateTime dateOfJoining;
    private Department department;
}
