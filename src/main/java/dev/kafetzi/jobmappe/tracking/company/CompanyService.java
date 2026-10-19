package dev.kafetzi.jobmappe.tracking.company;

import dev.kafetzi.jobmappe.tracking.company.dto.CompanyCompactDto;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyDetailDto;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyRequestDto;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface CompanyService {
    Page<CompanyCompactDto> getCompanies(String q, Pageable pageable);
    CompanyDetailDto getCompany(UUID id);
    CompanyDetailDto createCompany(CompanyRequestDto requestDto);
    CompanyDetailDto updateCompany(UUID id, CompanyRequestDto requestDto);
}
