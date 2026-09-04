package dev.kafetzi.jobmappe.posting.mapper;

import dev.kafetzi.jobmappe.posting.model.Company;
import dev.kafetzi.jobmappe.posting.model.dto.CompanyCompactDto;
import dev.kafetzi.jobmappe.posting.model.dto.CompanyDetailDto;
import dev.kafetzi.jobmappe.posting.model.dto.CompanyResponseDto;
import dev.kafetzi.jobmappe.posting.model.dto.CompanyRequestDto;

public interface CompanyMapper {
    CompanyResponseDto toDto(Company company);
    CompanyCompactDto toCompactDto(Company company, int jobCount);
    CompanyDetailDto toDetailDto(Company company, int jobCount);
    Company toEntity(CompanyRequestDto request);
    void updateEntityFromDto(CompanyRequestDto companyRequestDto, Company company);
}
