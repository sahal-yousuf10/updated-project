package com.example.sahal.Springbootmultithreading2.mapper;

import com.example.sahal.Springbootmultithreading2.Model.Employee;
import com.example.sahal.Springbootmultithreading2.dto.EmployeeDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeDto entityToDto(Employee employee);
    Employee dtoToEntity(EmployeeDto employeeDto);
    List<EmployeeDto> entityToDto(List<Employee> employeeList);
    List<Employee> dtoToEntity(List<EmployeeDto> employeeDtoList);
}
