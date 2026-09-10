package dev.kafetzi.jobmappe.tracking.company.impl;

import dev.kafetzi.jobmappe.tracking.company.CompanyMapper;
import dev.kafetzi.jobmappe.tracking.company.Company;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyCompactDto;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyDetailDto;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyResponseDto;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyRequestDto;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapperImpl implements CompanyMapper {

    @Override
    public CompanyResponseDto toDto(Company company) {
        return new CompanyResponseDto(
                company.getId(),
                company.getName(),
                company.getWebsite(),
                company.getStreet(),
                company.getPostalCode(),
                company.getCity()
        );
    }

    @Override
    public CompanyCompactDto toCompactDto(Company company, int jobCount) {
        return new CompanyCompactDto(
                company.getId(),
                company.getName(),
                jobCount
        );
    }

    @Override
    public CompanyDetailDto toDetailDto(Company company, int jobCount) {
        return new CompanyDetailDto(
                company.getId(),
                company.getName(),
                company.getWebsite(),
                company.getStreet(),
                company.getPostalCode(),
                company.getCity(),
                jobCount
        );
    }

    @Override
    public Company toEntity(CompanyRequestDto request) {
        Company company = new Company();
        company.setName(request.name());
        company.setWebsite(request.website());
        company.setStreet(request.street());
        company.setPostalCode(request.postalCode());
        company.setCity(request.city());

        return company;
    }

    @Override
    public void updateEntityFromDto(CompanyRequestDto companyRequestDto, Company company) {
        if (companyRequestDto == null || company == null) return;

        company.setName(companyRequestDto.name());
        company.setWebsite(companyRequestDto.website());
        company.setStreet(companyRequestDto.street());
        company.setPostalCode(companyRequestDto.postalCode());
        company.setCity(companyRequestDto.city());
    }
}
