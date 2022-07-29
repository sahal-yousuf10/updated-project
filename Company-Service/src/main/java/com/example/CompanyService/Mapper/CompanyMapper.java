package com.example.CompanyService.Mapper;

import com.example.CompanyService.Dto.CompanyDto;
import com.example.CompanyService.Model.Company;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    CompanyDto entityToDto(Company company);
    Company dtoToEntity(CompanyDto companyDto);
    List<CompanyDto> entityToDto(List<Company> companyList);
    List<Company> dtoToEntity(List<CompanyDto> companyDtoList);
}
