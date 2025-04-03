package com.demo.employeemanager.models.wrappers;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApiError {
    private HttpStatus status;
    private String message;
    private List<String> errors;
}
