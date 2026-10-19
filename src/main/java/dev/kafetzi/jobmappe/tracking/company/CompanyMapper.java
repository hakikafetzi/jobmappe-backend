package dev.kafetzi.jobmappe.tracking.company;

import dev.kafetzi.jobmappe.tracking.company.dto.CompanyCompactDto;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyDetailDto;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanySummaryDto;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyRequestDto;

public interface CompanyMapper {
    CompanySummaryDto toSummaryDto(Company company);
    CompanyCompactDto toCompactDto(Company company, long jobCount);
    CompanyDetailDto toDetailDto(Company company, long jobCount);
    Company toEntity(CompanyRequestDto request);
    void updateEntityFromDto(CompanyRequestDto companyRequestDto, Company company);
}
