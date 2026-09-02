package dev.kafetzi.jobmappe.posting.mapper;

import dev.kafetzi.jobmappe.posting.model.Company;
import dev.kafetzi.jobmappe.posting.model.dto.CompactCompanyDto;
import dev.kafetzi.jobmappe.posting.model.dto.CompanyResponseDto;
import dev.kafetzi.jobmappe.posting.model.dto.CompanyRequestDto;

public interface CompanyMapper {
    CompanyResponseDto toDto(Company company, int jobCount);
    CompactCompanyDto toDto(Company company);
    Company toEntity(CompanyRequestDto request);
    void updateEntityFromDto(CompanyRequestDto companyRequestDto, Company company);
}
