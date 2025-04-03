package com.demo.employeemanager.models.dtos;

import com.demo.employeemanager.annotations.ValidDepartment;
import com.demo.employeemanager.models.enums.Department;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private Long id;

    @NotBlank
    @Size(min = 4, max = 12)
    private String name;

    @NotBlank
    @Email
    private String email;

    @Min(17)
    @Max(60)
    @NotNull
    private Integer age;

    @Positive
    @Digits(integer = 8, fraction = 2)
    private Double salary;

    @PastOrPresent
    private LocalDateTime dateOfJoining;

    @ValidDepartment
    private Department department;
}