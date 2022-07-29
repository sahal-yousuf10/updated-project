package com.example.CityService.Controller;

import com.example.CityService.Dto.CityDto;
import com.example.CityService.Serivce.CityService;
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
public class CityController {

    @Autowired
    private CityService cityService;

    @PostMapping("/city")
    public ResponseEntity<String> saveCity(
            @RequestBody CityDto cityDto) throws ExecutionException, InterruptedException {
        CompletableFuture<String> result = cityService.saveCity(cityDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result.get());
    }

    @PutMapping("/city/{id}")
    public ResponseEntity<String> updateCity(
            @PathVariable long id, @RequestBody CityDto cityDto) throws Exception{
        CompletableFuture<String> result;
        try {
            result = cityService.updateCity(id, cityDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(result.get());
        }
        catch (Exception ex){
            log.error("Exception caught "+ex.getMessage());
            throw ex;
        }
    }

    @GetMapping(value = "/city")
    public ResponseEntity<List<CityDto>> findAllCities() throws ExecutionException, InterruptedException {
        List<CityDto> cityDtoList = cityService.findAllCities().get();
        return ResponseEntity.ok(cityDtoList);
    }

    @GetMapping(value = "/city/{id}")
    public ResponseEntity<CityDto> findCityById(
            @PathVariable long id) throws Exception {
        CityDto cityDto;
        try {
            cityDto = cityService.findCityById(id).get();
        }
        catch (Exception e){
            throw e;
        }
        return ResponseEntity.ok(cityDto);
    }

    @GetMapping(value = "/city/name/{name}")
    public ResponseEntity<CityDto> findCityByName(
            @PathVariable String name) throws Exception {
        CompletableFuture<CityDto> cityDto;
        try {
            cityDto = cityService.findCityByName(name);
        }
        catch (Exception e){
            throw e;
        }
        return ResponseEntity.ok(cityDto.get());
    }
}
