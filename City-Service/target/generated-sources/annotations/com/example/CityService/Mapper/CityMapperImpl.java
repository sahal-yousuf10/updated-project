package com.example.CityService.Mapper;

import com.example.CityService.Dto.CityDto;
import com.example.CityService.model.City;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-07-18T16:00:59+0500",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 18.0.1.1 (Oracle Corporation)"
)
@Component
public class CityMapperImpl implements CityMapper {

    @Override
    public CityDto entityToDto(City city) {
        if ( city == null ) {
            return null;
        }

        CityDto cityDto = new CityDto();

        if ( city.getId() != null ) {
            cityDto.setId( city.getId() );
        }
        cityDto.setName( city.getName() );

        return cityDto;
    }

    @Override
    public City dtoToEntity(CityDto cityDto) {
        if ( cityDto == null ) {
            return null;
        }

        City city = new City();

        city.setId( cityDto.getId() );
        city.setName( cityDto.getName() );

        return city;
    }

    @Override
    public List<CityDto> entityToDto(List<City> cityList) {
        if ( cityList == null ) {
            return null;
        }

        List<CityDto> list = new ArrayList<CityDto>( cityList.size() );
        for ( City city : cityList ) {
            list.add( entityToDto( city ) );
        }

        return list;
    }

    @Override
    public List<City> dtoToEntity(List<CityDto> cityDtoList) {
        if ( cityDtoList == null ) {
            return null;
        }

        List<City> list = new ArrayList<City>( cityDtoList.size() );
        for ( CityDto cityDto : cityDtoList ) {
            list.add( dtoToEntity( cityDto ) );
        }

        return list;
    }
}
