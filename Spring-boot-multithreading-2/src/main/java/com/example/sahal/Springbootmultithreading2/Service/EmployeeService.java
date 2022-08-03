package com.example.sahal.Springbootmultithreading2.Service;

import com.example.sahal.Springbootmultithreading2.Exception.EmployeeAlreadyExistException;
import com.example.sahal.Springbootmultithreading2.Exception.GlobalException;
import com.example.sahal.Springbootmultithreading2.Exception.ResourceNotFoundException;
import com.example.sahal.Springbootmultithreading2.Feign.CityFeignService;
import com.example.sahal.Springbootmultithreading2.Feign.CompanyFeignService;
import com.example.sahal.Springbootmultithreading2.Model.Employee;
import com.example.sahal.Springbootmultithreading2.Repository.EmployeeRepository;
import com.example.sahal.Springbootmultithreading2.ValueObject.ResponseValueObject;
import com.example.sahal.Springbootmultithreading2.dto.CityDto;
import com.example.sahal.Springbootmultithreading2.dto.CompanyDto;
import com.example.sahal.Springbootmultithreading2.dto.EmployeeDto;
import com.example.sahal.Springbootmultithreading2.dto.ErrorDto;
import com.example.sahal.Springbootmultithreading2.mapper.EmployeeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private CityFeignService cityFeignService;

    @Autowired
    private CompanyFeignService companyFeignService;

    @Async
    public CompletableFuture<String> saveEmployeesThroughFile(MultipartFile file) throws GlobalException {
        try {
//            Employee employee = null;
//            employee.getCityId();
            List<Employee> employeeList = parseCSVFile(file);
            log.info("Saving list of employees of size " + employeeList.size() + " " + Thread.currentThread().getName());
            employeeRepository.saveAll(employeeList);
            String message = "Data saved successfully!";
            return CompletableFuture.completedFuture(message);
        } catch (Exception ex) {
            log.error("Exception caught "+ex.getMessage());
            throw new GlobalException("Data not saved due to some internal error!");
        }
    }

    @Async
    public CompletableFuture<List<EmployeeDto>> findAllEmployees() throws GlobalException {
        List<EmployeeDto> employeeDtoList;
        try {
//            Employee employee =null;
//            employee.getCityId();
            log.info("Getting list of employees by "+Thread.currentThread().getName());
            List<Employee> employeeList = employeeRepository.findAll()
                    .stream()
                    .filter(Employee::isActive)
                    .collect(Collectors.toList());
            employeeDtoList = employeeMapper.entityToDto(employeeList);
            return CompletableFuture.completedFuture(employeeDtoList);
        } catch (Exception ex) {
            log.error("Exception caught "+ex.getMessage());
            throw new GlobalException("Employees not found due to some internal error!");
        }
    }

    @Async
    public CompletableFuture<Employee> findAllEmployeesByThread(long id) throws GlobalException {
        Employee employee;
        try {
            employee = employeeRepository.findById(id).get();
            log.info("Finding employees by thread " + Thread.currentThread().getName());
            return CompletableFuture.completedFuture(employee);
        } catch (Exception ex) {
            log.error("Exception caught "+ex.getMessage());
            throw new GlobalException("Not found due to some internal error!");
        }
    }

    private List<Employee> parseCSVFile(MultipartFile file) throws GlobalException {
        final List<Employee> employeeList = new ArrayList<>();
        try {
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    final String[] data = line.split(",");
                    final Employee employee = new Employee();
                    employee.setFirstName(data[0]);
                    employee.setLastName(data[1]);
                    employee.setEmail(data[2]);
                    employee.setGender(data[3]);
                    employee.setCompanyId(Long.parseLong(data[4]));
                    employee.setJobTitle(data[5]);
                    employee.setRegistrationId(Long.parseLong(data[6]));
                    employee.setCityId(Long.parseLong(data[7]));
                    employee.setActive(true);
                    employeeList.add(employee);
                }
                return employeeList;
            }
        } catch (Exception ex) {
            log.error("Failed to parse CSV file "+ex.getMessage());
            throw new GlobalException("Failed to parse CSV file due to some internal error!");
        }
    }

    public ResponseValueObject findEmployeeById(long id) throws GlobalException {
        try {
//            EmployeeDto employeeDto2 = null;
//            employeeDto2.getCityId();
            Employee employee = employeeRepository.findById(id)
                    .filter(Employee::isActive)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
            EmployeeDto employeeDto = employeeMapper.entityToDto(employee);
            CityDto cityDto = cityFeignService.findCityById(employeeDto.getCityId()).getBody();
            CompanyDto companyDto = companyFeignService.findCompanyById(employeeDto.getCompanyId()).getBody();
            ResponseValueObject vo = new ResponseValueObject();
            List<EmployeeDto> employeeDtoList = new ArrayList<>();
            employeeDtoList.add(employeeDto);
            vo.setCity(cityDto);
            vo.setCompany(companyDto);
            vo.setEmployeeList(employeeDtoList);
            return vo;
        } catch (Exception ex) {
            log.error("Exception caught "+ex.getMessage());
            ResourceNotFoundException resourceNotFoundException
                    = ex instanceof ResourceNotFoundException ? ((ResourceNotFoundException) ex) : null;
            if(resourceNotFoundException != null)
                throw new ResourceNotFoundException(ex.getLocalizedMessage());
            else
                throw new GlobalException("Employee with id "+id+" not found due to some internal error!");
        }
    }

    @Async
    public CompletableFuture<ResponseValueObject> findEmployeesByCityName(String name) throws GlobalException {
        ResponseValueObject vo = new ResponseValueObject();
        List<Employee> employeeList;
        List<EmployeeDto> employeeDtoList;
        try {
//            Employee employee = null;
//            employee.getCityId();
            CityDto cityDto = cityFeignService.findCityByName(name).getBody();
            if (cityDto != null && cityDto.getErrors() == null) {
                long cityId = cityDto.getId();
                employeeList = employeeRepository.findAllByCityId(cityId)
                        .stream()
                        .filter(Employee::isActive)
                        .collect(Collectors.toList());
                employeeDtoList = employeeMapper.entityToDto(employeeList);
                vo.setCity(cityDto);
                vo.setEmployeeList(employeeDtoList);
            }
            else if (cityDto.getErrors() != null) {
                CityDto cityDto1 = new CityDto();
                ErrorDto error = new ErrorDto();
                error.setTimeStamp(cityDto.getErrors().getTimeStamp());
                error.setMessage(cityDto.getErrors().getMessage());
                error.setDetails(cityDto.getErrors().getDetails());
                cityDto1.setErrors(error);
                vo.setCity(cityDto1);
            }
            return CompletableFuture.completedFuture(vo);
        } catch (Exception ex) {
            log.error("Exception caught "+ex.getMessage());
            throw new GlobalException("Employee by city not found due to some internal error!");
        }
    }

    @Async
    public CompletableFuture<ResponseValueObject> findEmployeesByCompanyName(String name) throws GlobalException{
        ResponseValueObject vo = new ResponseValueObject();
        List<Employee> employeeList;
        List<EmployeeDto> employeeDtoList;
        try {
//            Employee employee = null;
//            employee.getCityId();
            CompanyDto companyDto = companyFeignService.findCompanyByName(name).getBody();
            if (companyDto != null && companyDto.getError() == null) {
                long companyId = companyDto.getId();
                employeeList = employeeRepository.findAllByCompanyId(companyId)
                        .stream()
                        .filter(Employee::isActive)
                        .collect(Collectors.toList());
                employeeDtoList = employeeMapper.entityToDto(employeeList);
                vo.setCompany(companyDto);
                vo.setEmployeeList(employeeDtoList);
            }
            else if (companyDto.getError() != null) {
                CompanyDto companyDto1 = new CompanyDto();
                ErrorDto errorDto = new ErrorDto();
                errorDto.setTimeStamp(companyDto.getError().getTimeStamp());
                errorDto.setMessage(companyDto.getError().getMessage());
                errorDto.setDetails(companyDto.getError().getDetails());
                companyDto1.setError(errorDto);
                vo.setCompany(companyDto1);
            }
            return CompletableFuture.completedFuture(vo);
        } catch (Exception ex) {
            log.error("Exception caught "+ex.getMessage());
            throw new GlobalException("Employee by city not found due to some internal error!");
        }
    }

    @Async
    public CompletableFuture<String> saveEmployee(EmployeeDto employeeDto) throws GlobalException {
        try {
//        Employee employee1 =null;
//        employee1.getRegistrationId();
        Employee employee = employeeMapper.dtoToEntity(employeeDto);
        employee.setActive(true);
        String result = null;
        Optional<Employee> existingEmployee = Optional.ofNullable(
                employeeRepository.findByRegistrationId(employee.getRegistrationId())
                        .orElse(null));
            if (existingEmployee.equals(Optional.empty())) {
                employeeRepository.save(employee);
                result = "Employee created successfully";
                return CompletableFuture.completedFuture(result);
            }
            else {
                throw new EmployeeAlreadyExistException("Employee already exist with registration id "+employee.getRegistrationId());
            }
        } catch (Exception ex) {
            log.error("Exception caught "+ex.getMessage());
            EmployeeAlreadyExistException employeeAlreadyExistException
                    = ex instanceof EmployeeAlreadyExistException ? ((EmployeeAlreadyExistException) ex) : null;
            if(employeeAlreadyExistException != null)
                throw new EmployeeAlreadyExistException(ex.getLocalizedMessage());
            else
                throw new GlobalException("Employee not created due to some internal error!");
        }
    }

    @Async
    public CompletableFuture<String> updateEmployee(long id, EmployeeDto employeeDto) throws Exception{
        String result;
        try {
            boolean isEmployeeAlreadyPresent = employeeRepository.findById(id)
                    .filter(Employee::isActive)
                    .isPresent();
            Employee updatedEmployee = employeeMapper.dtoToEntity(employeeDto);
            updatedEmployee.setActive(true);
            if (isEmployeeAlreadyPresent) {
                updatedEmployee.setId(id);
                employeeRepository.save(updatedEmployee);
                result = "Employee with id " + id + " updated successfully";
                return CompletableFuture.completedFuture(result);
            } else {
                throw new ResourceNotFoundException("Employee with id " + id + " does not exist!");
            }
        } catch (Exception ex) {
            log.error("Exception caught "+ex.getMessage());
            ResourceNotFoundException resourceNotFoundException
                    = ex instanceof ResourceNotFoundException ? ((ResourceNotFoundException) ex) : null;
            if(resourceNotFoundException != null)
                throw new ResourceNotFoundException(ex.getLocalizedMessage());
            else
                throw new GlobalException("Employee not updated due to some internal error!");
        }
    }

    @Async
    public CompletableFuture<String> deleteEmployee(long id) throws GlobalException {
        String message = null;
        try {
//            Employee employee1 = null;
//            employee1.getCityId();
            Optional<Employee> employee = Optional.ofNullable(employeeRepository.findById(id)
                    .filter(Employee::isActive).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id "+id)));
            if (employee.isPresent()) {
                employee.get().setActive(false);
                employeeRepository.save(employee.get());
                message = "Employee with id "+id+" deleted successfully!";
            }
            return CompletableFuture.completedFuture(message);
        } catch (Exception ex) {
            log.error("Exception caught "+ex.getMessage());
            throw new GlobalException("Employee not deleted due to some internal error!");
        }
    }

    public String deleteTestData() {
        employeeRepository.deleteTestData();
        return "Test data deleted successfully";
    }
}
