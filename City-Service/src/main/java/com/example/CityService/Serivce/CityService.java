package com.example.CityService.Serivce;

import com.example.CityService.Dto.CityDto;
import com.example.CityService.Mapper.CityMapper;
import com.example.CityService.Repository.CityRepository;
import com.example.CityService.model.City;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CityMapper cityMapper;

    @Async
    public CompletableFuture<String> saveCity(CityDto cityDto) {
        City city = cityMapper.dtoToEntity(cityDto);
        String result = null;
        try {
            cityRepository.save(city);
            result = "City created successfully";
        }
        catch (Exception ex){
            result = "Exception caught "+ex;
            throw ex;
        }
        finally {
            return CompletableFuture.completedFuture(result);
        }
    }

    @Async
    public CompletableFuture<String> updateCity(long id, CityDto cityDto) throws Exception {

        boolean isCityAlreadyPresent = cityRepository.findById(id).isPresent();
        City updatedCity = cityMapper.dtoToEntity(cityDto);
        String result;
        if(isCityAlreadyPresent){
            updatedCity.setId(id);
            cityRepository.save(updatedCity);
            result = "City with id "+id+" updated successfully";
        }
        else {
            //result = "employee with id "+id+" does not exist, therefore new employee is created";
            throw new Exception("City with id "+id+" does not exist!");
        }
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<List<CityDto>> findAllCities() {

        List<CityDto> cityDtoList;
        try {
            log.info("Getting list of cities by "+Thread.currentThread().getName());
            List<City> cityList = cityRepository.findAll();
            cityDtoList = cityMapper.entityToDto(cityList);
        }
        catch (Exception ex){
            log.error("Exception caught "+ex.getMessage());
            throw ex;
        }
        return CompletableFuture.completedFuture(cityDtoList);
    }

    @Async
    public CompletableFuture<CityDto> findCityById(long id) throws Exception {

        City city = cityRepository.findById(id)
                .orElseThrow(()-> new Exception("City not found with id "+id));
        CityDto cityDto = cityMapper.entityToDto(city);
        return CompletableFuture.completedFuture(cityDto);
    }

    @Async
    public CompletableFuture<CityDto> findCityByName(String name) throws Exception {
        City city = cityRepository.findByName(name)
                .orElseThrow(()-> new Exception("City not found with name "+name));
        CityDto cityDto = cityMapper.entityToDto(city);
        return CompletableFuture.completedFuture(cityDto);
    }
}
