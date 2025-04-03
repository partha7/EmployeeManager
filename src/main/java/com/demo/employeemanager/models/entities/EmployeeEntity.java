package com.demo.employeemanager.models.entities;

import com.demo.employeemanager.models.enums.Department;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(
        name = "employees",
        uniqueConstraints = {
                @UniqueConstraint(name = "email", columnNames = {"email"})
        },
        indexes = {
                @Index(name = "idx_email", columnList = "email")
        }
)
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private Integer age;

    private Double salary;

    @Column(name = "date_of_joining", nullable = false)
    private LocalDateTime dateOfJoining;

    @Enumerated(EnumType.STRING)
    private Department department;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
