package com.demo.employeemanager.services;

import com.demo.employeemanager.models.dtos.EmployeeDTO;
import com.demo.employeemanager.models.entities.EmployeeEntity;
import com.demo.employeemanager.exceptions.ResourceNotFoundException;
import com.demo.employeemanager.repositories.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
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

    public EmployeeDTO findEmployeeByID(Long employeeId) {
        existsEmployeeById(employeeId);
        return modelMapper.map(employeeRepository.findById(employeeId).orElse(null), EmployeeDTO.class);
    }
}
