package dev.kafetzi.jobmappe.tracking.company;

import dev.kafetzi.jobmappe.tracking.company.dto.CompanyCompactDto;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyDetailDto;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyRequestDto;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyResponseDto;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface CompanyService {
    Page<CompanyCompactDto> getCompanies(String q, Pageable pageable);
    CompanyDetailDto getCompany(UUID id);
    CompanyResponseDto createCompany(CompanyRequestDto requestDto);
    CompanyResponseDto updateCompany(UUID id, CompanyRequestDto requestDto);
}
