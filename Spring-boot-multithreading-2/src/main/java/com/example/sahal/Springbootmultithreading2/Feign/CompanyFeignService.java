package com.example.sahal.Springbootmultithreading2.Feign;

import com.example.sahal.Springbootmultithreading2.dto.CityDto;
import com.example.sahal.Springbootmultithreading2.dto.CompanyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "company-api",
        url = "http://localhost:8900",
        fallback = CompanyFeignFallback.class)
public interface CompanyFeignService {

    @GetMapping("/company/name/{name}")
    ResponseEntity<CompanyDto> findCompanyByName(@PathVariable String name);

    @GetMapping("/company/{id}")
    ResponseEntity<CompanyDto> findCompanyById(@PathVariable long id);
}
