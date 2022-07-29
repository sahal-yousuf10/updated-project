package com.example.CityService.Mapper;

import com.example.CityService.Dto.CityDto;
import com.example.CityService.model.City;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CityMapper {

    CityDto entityToDto(City city);
    City dtoToEntity(CityDto cityDto);
    List<CityDto> entityToDto(List<City> cityList);
    List<City> dtoToEntity(List<CityDto> cityDtoList);
}
