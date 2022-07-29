package com.example.sahal.Springbootmultithreading2.ValueObject;

import com.example.sahal.Springbootmultithreading2.Model.Employee;
import com.example.sahal.Springbootmultithreading2.dto.CityDto;
import com.example.sahal.Springbootmultithreading2.dto.CompanyDto;
import com.example.sahal.Springbootmultithreading2.dto.EmployeeDto;
import lombok.Data;

import java.util.List;

@Data
public class ResponseValueObject {

    private CityDto city;
    private CompanyDto company;
    private List<EmployeeDto> employeeList;
}
