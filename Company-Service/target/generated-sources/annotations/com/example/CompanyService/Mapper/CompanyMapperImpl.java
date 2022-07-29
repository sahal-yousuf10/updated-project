package com.example.CompanyService.Mapper;

import com.example.CompanyService.Dto.CompanyDto;
import com.example.CompanyService.Model.Company;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-07-18T17:24:07+0500",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 18.0.1.1 (Oracle Corporation)"
)
@Component
public class CompanyMapperImpl implements CompanyMapper {

    @Override
    public CompanyDto entityToDto(Company company) {
        if ( company == null ) {
            return null;
        }

        CompanyDto companyDto = new CompanyDto();

        companyDto.setId( company.getId() );
        companyDto.setName( company.getName() );

        return companyDto;
    }

    @Override
    public Company dtoToEntity(CompanyDto companyDto) {
        if ( companyDto == null ) {
            return null;
        }

        Company company = new Company();

        company.setId( companyDto.getId() );
        company.setName( companyDto.getName() );

        return company;
    }

    @Override
    public List<CompanyDto> entityToDto(List<Company> companyList) {
        if ( companyList == null ) {
            return null;
        }

        List<CompanyDto> list = new ArrayList<CompanyDto>( companyList.size() );
        for ( Company company : companyList ) {
            list.add( entityToDto( company ) );
        }

        return list;
    }

    @Override
    public List<Company> dtoToEntity(List<CompanyDto> companyDtoList) {
        if ( companyDtoList == null ) {
            return null;
        }

        List<Company> list = new ArrayList<Company>( companyDtoList.size() );
        for ( CompanyDto companyDto : companyDtoList ) {
            list.add( dtoToEntity( companyDto ) );
        }

        return list;
    }
}
