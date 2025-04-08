package com.demo.employeemanager.services;

import com.demo.employeemanager.models.dtos.EmployeeDTO;
import com.demo.employeemanager.models.entities.EmployeeEntity;
import com.demo.employeemanager.exceptions.ResourceNotFoundException;
import com.demo.employeemanager.models.enums.Department;
import com.demo.employeemanager.models.enums.SortOrder;
import com.demo.employeemanager.repositories.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    public EmployeeDTO findEmployeeByID(Long employeeId) {
        existsEmployeeById(employeeId);
        return modelMapper.map(employeeRepository.findById(employeeId).orElse(null), EmployeeDTO.class);
    }

    public List<EmployeeDTO> getEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map( employeeEntity -> modelMapper.map(employeeEntity, EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        EmployeeEntity toSaveEntity = modelMapper.map(employeeDTO, EmployeeEntity.class);
        EmployeeEntity savedEmployeeEntity = employeeRepository.save(toSaveEntity);
        return modelMapper.map(savedEmployeeEntity, EmployeeDTO.class);
    }

    public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO, Long employeeId) {
        existsEmployeeById(employeeId);
        employeeDTO.setId(employeeId);
        EmployeeEntity entity = employeeRepository.save(modelMapper.map(employeeDTO, EmployeeEntity.class));
        return modelMapper.map(entity, EmployeeDTO.class);
    }

    private void existsEmployeeById(Long employeeId) {
        EmployeeEntity entity = employeeRepository.findById(employeeId).orElse(null);
        if (entity == null) throw new ResourceNotFoundException("Employee not found with id: "+ employeeId);
    }

    public Boolean deleteEmployee(Long employeeId) {
        existsEmployeeById(employeeId);
        employeeRepository.deleteById(employeeId);
        return true;
    }

    public Optional<EmployeeDTO> findEmployeeByName(String name) {
        return employeeRepository.findFirstByNameStartingWith(name)
                .map(entity -> modelMapper.map(entity, EmployeeDTO.class));
    }

    public Page<EmployeeDTO> getEmployeesByFilter(Integer minAge, Integer maxAge, Department department, Double minSalary, String sortBy, String sortOrder, Integer page, Integer size) {
        Sort sort = sortOrder.equalsIgnoreCase(SortOrder.DESC.getValue())? Sort.by(Sort.Order.desc(sortBy)): Sort.by(Sort.Order.asc(sortBy));
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<EmployeeEntity> entities = employeeRepository.getEmployeesByFilter(minAge, maxAge, department, minSalary, pageable);
        return entities.map(employeeEntity -> modelMapper.map(employeeEntity, EmployeeDTO.class));
    }
}