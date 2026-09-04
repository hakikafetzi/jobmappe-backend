package dev.kafetzi.jobmappe.posting.service;

import dev.kafetzi.jobmappe.posting.model.dto.CompanyCompactDto;
import dev.kafetzi.jobmappe.posting.model.dto.CompanyDetailDto;
import dev.kafetzi.jobmappe.posting.model.dto.CompanyRequestDto;
import dev.kafetzi.jobmappe.posting.model.dto.CompanyResponseDto;

import java.util.List;
import java.util.UUID;

public interface CompanyService {
    List<CompanyCompactDto> getCompanies();
    CompanyDetailDto getCompany(UUID id);
    CompanyResponseDto createCompany(CompanyRequestDto requestDto);
    CompanyResponseDto updateCompany(UUID id, CompanyRequestDto requestDto);
}
