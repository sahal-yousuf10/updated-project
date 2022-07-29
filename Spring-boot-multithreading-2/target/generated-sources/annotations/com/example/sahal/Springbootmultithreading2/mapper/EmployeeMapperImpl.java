package com.example.sahal.Springbootmultithreading2.mapper;

import com.example.sahal.Springbootmultithreading2.Model.Employee;
import com.example.sahal.Springbootmultithreading2.dto.EmployeeDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-07-29T18:20:29+0500",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 18.0.1.1 (Oracle Corporation)"
)
@Component
public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public EmployeeDto entityToDto(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        EmployeeDto employeeDto = new EmployeeDto();

        employeeDto.setId( employee.getId() );
        employeeDto.setFirstName( employee.getFirstName() );
        employeeDto.setLastName( employee.getLastName() );
        employeeDto.setEmail( employee.getEmail() );
        employeeDto.setGender( employee.getGender() );
        employeeDto.setCompanyId( employee.getCompanyId() );
        employeeDto.setJobTitle( employee.getJobTitle() );
        employeeDto.setRegistrationId( employee.getRegistrationId() );
        employeeDto.setCityId( employee.getCityId() );

        return employeeDto;
    }

    @Override
    public Employee dtoToEntity(EmployeeDto employeeDto) {
        if ( employeeDto == null ) {
            return null;
        }

        Employee employee = new Employee();

        employee.setId( employeeDto.getId() );
        employee.setFirstName( employeeDto.getFirstName() );
        employee.setLastName( employeeDto.getLastName() );
        employee.setEmail( employeeDto.getEmail() );
        employee.setGender( employeeDto.getGender() );
        employee.setCompanyId( employeeDto.getCompanyId() );
        employee.setJobTitle( employeeDto.getJobTitle() );
        employee.setRegistrationId( employeeDto.getRegistrationId() );
        employee.setCityId( employeeDto.getCityId() );

        return employee;
    }

    @Override
    public List<EmployeeDto> entityToDto(List<Employee> employeeList) {
        if ( employeeList == null ) {
            return null;
        }

        List<EmployeeDto> list = new ArrayList<EmployeeDto>( employeeList.size() );
        for ( Employee employee : employeeList ) {
            list.add( entityToDto( employee ) );
        }

        return list;
    }

    @Override
    public List<Employee> dtoToEntity(List<EmployeeDto> employeeDtoList) {
        if ( employeeDtoList == null ) {
            return null;
        }

        List<Employee> list = new ArrayList<Employee>( employeeDtoList.size() );
        for ( EmployeeDto employeeDto : employeeDtoList ) {
            list.add( dtoToEntity( employeeDto ) );
        }

        return list;
    }
}
