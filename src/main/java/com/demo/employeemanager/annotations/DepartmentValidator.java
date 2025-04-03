package com.demo.employeemanager.annotations;

import com.demo.employeemanager.models.enums.Department;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class DepartmentValidator implements ConstraintValidator<ValidDepartment, Department> {

    @Override
    public boolean isValid(Department department, ConstraintValidatorContext constraintValidatorContext) {
        if (department == null) {
            return false;
        }
        return Arrays.stream(Department.values())
                .anyMatch(dept -> dept == department);
    }
}
