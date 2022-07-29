package com.example.sahal.Springbootmultithreading2.Feign;

import com.example.sahal.Springbootmultithreading2.dto.CityDto;
import com.example.sahal.Springbootmultithreading2.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

@Component
@Slf4j
public class CityFeignFallback implements CityFeignService{

    @Override
    public ResponseEntity<CityDto> findCityByName(String name){
        log.info("Fall back validation");
        CityDto cityDto = new CityDto();
        ErrorDto errorDto = new ErrorDto();
        errorDto.setTimeStamp(new Date());
        errorDto.setDetails("Error");
        errorDto.setMessage("Service down");
        cityDto.setErrors(errorDto);
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(cityDto);
    }

    @Override
    public ResponseEntity<CityDto> findCityById(@PathVariable long id){
        log.info("Fall back validation");
        CityDto cityDto = new CityDto();
        ErrorDto errorDto = new ErrorDto();
        errorDto.setTimeStamp(new Date());
        errorDto.setDetails("Error");
        errorDto.setMessage("Service down");
        cityDto.setErrors(errorDto);
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(cityDto);
    }
}
