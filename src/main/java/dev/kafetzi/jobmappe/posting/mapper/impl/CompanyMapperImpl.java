package dev.kafetzi.jobmappe.posting.mapper.impl;

import dev.kafetzi.jobmappe.posting.mapper.CompanyMapper;
import dev.kafetzi.jobmappe.posting.model.Company;
import dev.kafetzi.jobmappe.posting.model.dto.CompactCompanyDto;
import dev.kafetzi.jobmappe.posting.model.dto.CompanyResponseDto;
import dev.kafetzi.jobmappe.posting.model.dto.CompanyRequestDto;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapperImpl implements CompanyMapper {

    @Override
    public CompanyResponseDto toDto(Company company, int jobCount) {
        return new CompanyResponseDto(
                company.getId(),
                company.getName(),
                company.getWebsite(),
                jobCount
        );
    }

    @Override
    public CompactCompanyDto toDto(Company company) {
        return new CompactCompanyDto(
                company.getId(),
                company.getName(),
                company.getWebsite()
        );
    }

    @Override
    public Company toEntity(CompanyRequestDto request) {
        Company company = new Company();
        company.setName(request.name());
        company.setWebsite(request.website());

        return company;
    }

    @Override
    public void updateEntityFromDto(CompanyRequestDto companyRequestDto, Company company) {
        if (companyRequestDto == null || company == null) return;

        company.setName(companyRequestDto.name());
        company.setWebsite(companyRequestDto.website());
    }
}
