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
    public CompletableFuture<String> saveEmployeesThroughFile(MultipartFile file) throws Exception{
        try {
            List<Employee> employeeList = parseCSVFile(file);
            log.info("Saving list of employees of size " + employeeList.size() + " " + Thread.currentThread().getName());
            employeeRepository.saveAll(employeeList);
            String message = "Data saved successfully!";
            return CompletableFuture.completedFuture(message);
        }
        catch (Exception ex){
            log.error("Exception caught "+ex.getMessage());
            throw ex;
        }
    }

    @Async
    public CompletableFuture<List<EmployeeDto>> findAllEmployees() {
        List<EmployeeDto> employeeDtoList;
        try {
            log.info("Getting list of employees by "+Thread.currentThread().getName());
            List<Employee> employeeList = employeeRepository.findAll()
                    .stream()
                    .filter(Employee::isActive)
                    .collect(Collectors.toList());
            employeeDtoList = employeeMapper.entityToDto(employeeList);
        }
        catch (Exception ex){
            log.error("Exception caught "+ex.getMessage());
            throw ex;
        }
        return CompletableFuture.completedFuture(employeeDtoList);
    }

    @Async
    public CompletableFuture<Employee> findAllEmployeesByThread(long id) {
        Employee employee;
        try {
            employee = employeeRepository.findById(id).get();
            log.info("Finding employees by thread " + Thread.currentThread().getName());
        }
        catch (Exception ex){
            log.error("Exception caught "+ex.getMessage());
            throw ex;
        }
        return CompletableFuture.completedFuture(employee);
    }

    private List<Employee> parseCSVFile(MultipartFile file) throws Exception{
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
        }
        catch (Exception ex){
            log.error("Failed to parse CSV file "+ex.getMessage());
            throw ex;
        }
    }


    public ResponseValueObject findEmployeeById(long id) throws GlobalException {
        try {
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
        }
//        catch (ResourceNotFoundException  rnfe) {
//            log.error("Exception caught "+rnfe.getMessage());
//            throw rnfe;
//        }
        catch (Exception ex) {
            log.error("Exception caught "+ex.getMessage());
            ResourceNotFoundException resourceNotFoundException
                    = ex instanceof ResourceNotFoundException ? ((ResourceNotFoundException) ex) : null;
            if(resourceNotFoundException != null)
                throw new GlobalException(ex.getLocalizedMessage());
            else
                throw new GlobalException("abcd");
        }
    }

    @Async
    public CompletableFuture<ResponseValueObject> findEmployeesByCityName(String name) throws ExecutionException, InterruptedException {
        ResponseValueObject vo = new ResponseValueObject();
        List<Employee> employeeList;
        List<EmployeeDto> employeeDtoList = new ArrayList<>();
        try {
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
        }
        catch (Exception ex){
            log.error("Exception caught "+ex.getMessage());
            throw ex;
        }
        return CompletableFuture.completedFuture(vo);
    }

    @Async
    public CompletableFuture<ResponseValueObject> findEmployeesByCompanyName(String name) throws Exception{
        ResponseValueObject vo = new ResponseValueObject();
        List<Employee> employeeList;
        List<EmployeeDto> employeeDtoList;
        try {
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
        }
        catch (Exception ex){
            log.error("Exception caught "+ex.getMessage());
            throw ex;
        }
        return CompletableFuture.completedFuture(vo);
    }

    @Async
    public CompletableFuture<String> saveEmployee(EmployeeDto employeeDto) {
        Employee employee = employeeMapper.dtoToEntity(employeeDto);
        employee.setActive(true);
        String result = null;
        Optional<Employee> existingEmployee = Optional.ofNullable(
                employeeRepository.findByRegistrationId(employee.getRegistrationId())
                        .orElse(null));
        try {
            if (existingEmployee.equals(Optional.empty())) {
                employeeRepository.save(employee);
                result = "Employee created successfully";
            }
            else {
                result = "Employee already exist with registration id "+employee.getRegistrationId();
                throw new EmployeeAlreadyExistException("Employee already exist with registration id "+employee.getRegistrationId());
            }
        }
        catch (Exception ex){
            log.error("Exception caught "+ex.getMessage());
            throw ex;
        }
        finally {
            return CompletableFuture.completedFuture(result);
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
            } else {
                throw new ResourceNotFoundException("Employee with id " + id + " does not exist!");
            }
        }
        catch (Exception ex){
            log.error("Exception caught "+ex.getMessage());
            throw ex;
        }
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<String> deleteEmployee(long id) {
        String message = null;
        try {
            Optional<Employee> employee = Optional.ofNullable(employeeRepository.findById(id)
                    .filter(Employee::isActive).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id "+id)));
            if (employee.isPresent()) {
                employee.get().setActive(false);
                employeeRepository.save(employee.get());
                message = "Employee with id "+id+" deleted successfully!";
            }
        }
        catch (Exception ex){
            log.error("Exception caught "+ex.getMessage());
            throw ex;
        }
        return CompletableFuture.completedFuture(message);
    }
}
