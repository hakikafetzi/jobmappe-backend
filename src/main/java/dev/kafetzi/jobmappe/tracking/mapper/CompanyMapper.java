package dev.kafetzi.jobmappe.tracking.mapper;

import dev.kafetzi.jobmappe.tracking.model.Company;
import dev.kafetzi.jobmappe.tracking.model.dto.CompanyCompactDto;
import dev.kafetzi.jobmappe.tracking.model.dto.CompanyDetailDto;
import dev.kafetzi.jobmappe.tracking.model.dto.CompanyResponseDto;
import dev.kafetzi.jobmappe.tracking.model.dto.CompanyRequestDto;

public interface CompanyMapper {
    CompanyResponseDto toDto(Company company);
    CompanyCompactDto toCompactDto(Company company, int jobCount);
    CompanyDetailDto toDetailDto(Company company, int jobCount);
    Company toEntity(CompanyRequestDto request);
    void updateEntityFromDto(CompanyRequestDto companyRequestDto, Company company);
}
