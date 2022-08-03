package com.example.sahal.Springbootmultithreading2.Controller;

import com.example.sahal.Springbootmultithreading2.Exception.EmployeeAlreadyExistException;
import com.example.sahal.Springbootmultithreading2.Exception.GlobalException;
import com.example.sahal.Springbootmultithreading2.Exception.ResourceNotFoundException;
import com.example.sahal.Springbootmultithreading2.Model.Employee;
import com.example.sahal.Springbootmultithreading2.Service.EmployeeService;
import com.example.sahal.Springbootmultithreading2.ValueObject.ResponseValueObject;
import com.example.sahal.Springbootmultithreading2.dto.EmployeeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.example.sahal.Springbootmultithreading2.constant.Constant.*;

@RestController
@Slf4j
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;

    @PostMapping(value = "/employees", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ResponseEntity<String> saveEmployeesThroughFile(
            @Valid @RequestParam(value = "files")MultipartFile[] files) throws Exception {
        String result = null;
        try {
            for (MultipartFile file : files) {
                result = employeeService.saveEmployeesThroughFile(file).get();
            }
        }
        catch (GlobalException ex){
            throw new GlobalException(ex.getMessage());
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result);
    }

    @PostMapping("/employee")
    public ResponseEntity<String> saveEmployee(
            @Valid @RequestBody EmployeeDto employeeDto) throws Exception{
        String result;
        try {
            result = employeeService.saveEmployee(employeeDto).get();
        }
        catch (GlobalException ex){
            throw new GlobalException(ex.getMessage());
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result);
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<String> updateEmployee(
            @Positive(message = ID_SHOULD_BE_POSITIVE_ERROR_MESSAGE)
            @PathVariable long id, @Valid @RequestBody EmployeeDto employeeDto) throws Exception{
        String result;
        try {
            result = employeeService.updateEmployee(id, employeeDto).get();
        }
        catch (GlobalException ex){
            throw new GlobalException(ex.getMessage());
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result);
    }

    @GetMapping(value = "/employees")
    public ResponseEntity<List<EmployeeDto>> findAllEmployees() throws Exception {
        List<EmployeeDto> employeeDtoList;
        try {
            employeeDtoList = employeeService.findAllEmployees().get();
        }
        catch (GlobalException ex){
            throw new GlobalException(ex.getMessage());
        }
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(employeeDtoList);
    }

    @GetMapping(value = "/employee/{id}")
    public ResponseEntity<ResponseValueObject> findEmployeeById(
            @Positive(message = ID_SHOULD_BE_POSITIVE_ERROR_MESSAGE)
            @PathVariable long id) throws Exception {
        ResponseValueObject vo;
        try {
            vo = employeeService.findEmployeeById(id);
        }
        catch (GlobalException ex){
            throw new GlobalException(ex.getMessage());
        }
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(vo);
    }

    @GetMapping("/employees/city/{name}")
    public ResponseEntity<ResponseValueObject> findEmployeesByCityName(
            @PathVariable String name) throws Exception {
        ResponseValueObject vo;
        try {
            vo = employeeService.findEmployeesByCityName(name).get();
        }
        catch (GlobalException ex){
            throw new GlobalException(ex.getMessage());
        }
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(vo);
    }

    @GetMapping("/employees/company/{name}")
    public ResponseEntity<ResponseValueObject> findEmployeesByCompanyName(
            @PathVariable String name) throws Exception {
        ResponseValueObject vo;
        try {
            vo = employeeService.findEmployeesByCompanyName(name).get();
        }
        catch (GlobalException ex){
            throw new GlobalException(ex.getMessage());
        }
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(vo);
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity deleteEmployee(
            @Positive(message = ID_SHOULD_BE_POSITIVE_ERROR_MESSAGE)
            @PathVariable long id) throws Exception {
        String result = null;
        try {
            result = employeeService.deleteEmployee(id).get();
        }
        catch (GlobalException ex){
            throw new GlobalException(ex.getMessage());
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "employees/thread")
    public ResponseEntity<List<Employee>> findAllEmployeesByThread() throws Exception {
        List<Employee> employeeList = new ArrayList<>();
        Employee employee = new Employee();
        try {
            for (long i = 1; i <= 3000; i++) {
                employee = employeeService.findAllEmployeesByThread(i).get();
                employeeList.add(employee);
            }
        }
        catch (GlobalException ex){
            throw new GlobalException(ex.getMessage());
        }
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(employeeList);
    }

    @DeleteMapping(value = "delete/test")
    public ResponseEntity<String> deleteTestData(){
        String message;
        message = employeeService.deleteTestData();
        return ResponseEntity.ok(message);
    }
}
