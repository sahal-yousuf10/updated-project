package com.example.CompanyService.Controller;

import com.example.CompanyService.Dto.CompanyDto;
import com.example.CompanyService.Service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping("/company")
    public ResponseEntity<String> saveCompany(
            @RequestBody CompanyDto companyDto) throws ExecutionException, InterruptedException {
        String result = companyService.saveCompany(companyDto).get();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/company/{id}")
    public ResponseEntity<String> updateCompany(
            @PathVariable long id, @RequestBody CompanyDto companyDto) throws Exception{
        String result;
        try {
            result = companyService.updateCompany(id, companyDto).get();
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }
        catch (Exception ex){
            log.error("Exception caught "+ex.getMessage());
            throw ex;
        }
    }

    @GetMapping(value = "/company")
    public ResponseEntity<List<CompanyDto>> findAllCompanies() throws ExecutionException, InterruptedException {
        List<CompanyDto> companyDtoList = companyService.findAllCompanies().get();
        return ResponseEntity.ok(companyDtoList);
    }

    @GetMapping(value = "/company/{id}")
    public ResponseEntity<CompanyDto> findCompanyById(
            @PathVariable long id) throws Exception {
        CompanyDto companyDto;
        try {
            companyDto = companyService.findCompanyById(id).get();
        }
        catch (Exception e){
            throw e;
        }
        return ResponseEntity.ok(companyDto);
    }

    @GetMapping(value = "/company/name/{name}")
    public ResponseEntity<CompanyDto> findCompanyByName(
            @PathVariable String name) throws Exception {
        CompanyDto companyDto;
        try {
            companyDto = companyService.findCompanyByName(name).get();
        }
        catch (Exception e){
            throw e;
        }
        return ResponseEntity.ok(companyDto);
    }
}
