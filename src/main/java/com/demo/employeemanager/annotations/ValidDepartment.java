package com.demo.employeemanager.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {DepartmentValidator.class})
public @interface ValidDepartment {

    String message() default "Invalid department";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
