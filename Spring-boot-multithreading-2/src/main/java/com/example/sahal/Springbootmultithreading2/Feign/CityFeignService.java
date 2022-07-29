package com.example.sahal.Springbootmultithreading2.Feign;

import com.example.sahal.Springbootmultithreading2.dto.CityDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "city-api",
        url = "http://localhost:8099",
        fallback = CityFeignFallback.class)
public interface CityFeignService {

    @GetMapping("/city/name/{name}")
    ResponseEntity<CityDto> findCityByName(@PathVariable String name);

    @GetMapping("/city/{id}")
    ResponseEntity<CityDto> findCityById(@PathVariable long id);
}
