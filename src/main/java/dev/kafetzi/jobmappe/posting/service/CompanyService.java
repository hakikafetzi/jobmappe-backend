package dev.kafetzi.jobmappe.posting.service;

import dev.kafetzi.jobmappe.posting.model.dto.CompactCompanyDto;
import dev.kafetzi.jobmappe.posting.model.dto.CompanyRequestDto;
import dev.kafetzi.jobmappe.posting.model.dto.CompanyResponseDto;

import java.util.List;
import java.util.UUID;

public interface CompanyService {
    List<CompanyResponseDto> getCompanies();
    CompanyResponseDto getCompany(UUID id);
    CompactCompanyDto createCompany(CompanyRequestDto requestDto);
    CompactCompanyDto updateCompany(UUID id, CompanyRequestDto requestDto);
}
