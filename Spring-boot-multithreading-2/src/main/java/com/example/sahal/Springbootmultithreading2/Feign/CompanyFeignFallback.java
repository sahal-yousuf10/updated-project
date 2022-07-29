package com.example.sahal.Springbootmultithreading2.Feign;

import com.example.sahal.Springbootmultithreading2.dto.CompanyDto;
import com.example.sahal.Springbootmultithreading2.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

@Component
@Slf4j
public class CompanyFeignFallback implements CompanyFeignService{

    @Override
    public ResponseEntity<CompanyDto> findCompanyByName(String name){
        log.info("Fall back validation");
        CompanyDto companyDto = new CompanyDto();
        ErrorDto errorDto = new ErrorDto();
        errorDto.setTimeStamp(new Date());
        errorDto.setDetails("Error");
        errorDto.setMessage("Service Unavailable");
        companyDto.setError(errorDto);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(companyDto);
    }

    @Override
    public ResponseEntity<CompanyDto> findCompanyById(@PathVariable long id){
        log.info("Fall back validation");
        CompanyDto companyDto = new CompanyDto();
        ErrorDto errorDto = new ErrorDto();
        errorDto.setTimeStamp(new Date());
        errorDto.setDetails("Error");
        errorDto.setMessage("Service Unavailable");
        companyDto.setError(errorDto);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(companyDto);
    }

}
