package dev.kafetzi.jobmappe.tracking.company;

import dev.kafetzi.jobmappe.tracking.company.dto.CompanyCompactDto;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyDetailDto;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyResponseDto;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyRequestDto;

public interface CompanyMapper {
    CompanyResponseDto toDto(Company company);
    CompanyCompactDto toCompactDto(Company company, int jobCount);
    CompanyDetailDto toDetailDto(Company company, int jobCount);
    Company toEntity(CompanyRequestDto request);
    void updateEntityFromDto(CompanyRequestDto companyRequestDto, Company company);
}
