package com.demo.employeemanager.repositories;

import com.demo.employeemanager.models.entities.EmployeeEntity;
import com.demo.employeemanager.models.enums.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    Optional<EmployeeEntity> findFirstByNameStartingWith(String name);

    @Query("SELECT e FROM EmployeeEntity e WHERE " +
            "(:minAge is NULL OR e.age >= :minAge) AND " +
            "(:maxAge is NULL OR e.age <= :maxAge) AND " +
            "(:department is NULL OR e.department = :department) AND " +
            "(:minSalary is NULL OR e.salary >= :minSalary)")
    Page<EmployeeEntity> getEmployeesByFilter(Integer minAge, Integer maxAge, Department department, Double minSalary, Pageable pageable);
}
