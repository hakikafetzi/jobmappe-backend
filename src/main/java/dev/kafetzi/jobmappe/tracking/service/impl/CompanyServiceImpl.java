package dev.kafetzi.jobmappe.tracking.service.impl;

import dev.kafetzi.jobmappe.tracking.mapper.CompanyMapper;
import dev.kafetzi.jobmappe.tracking.model.Company;
import dev.kafetzi.jobmappe.tracking.model.dto.CompanyCompactDto;
import dev.kafetzi.jobmappe.tracking.model.dto.CompanyDetailDto;
import dev.kafetzi.jobmappe.tracking.model.dto.CompanyRequestDto;
import dev.kafetzi.jobmappe.tracking.model.dto.CompanyResponseDto;
import dev.kafetzi.jobmappe.tracking.repository.CompanyRepository;
import dev.kafetzi.jobmappe.tracking.repository.JobPostingRepository;
import dev.kafetzi.jobmappe.tracking.service.CompanyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final JobPostingRepository jobPostingRepository;
    private final CompanyMapper companyMapper;

    @Override
    public List<CompanyCompactDto> getCompanies() {
        List<Company> companies = companyRepository.findAll();

        Map<UUID, Long> jobCountsByCompanyId = jobPostingRepository.countJobPostingsPerCompany()
                .stream()
                .collect(Collectors.toMap(
                        row -> (UUID) row[0],
                        row -> (Long) row[1]
                ));

        return companies.stream()
                .map(company -> {
                    long jobCount = jobCountsByCompanyId.getOrDefault(company.getId(), 0L);
                    return companyMapper.toCompactDto(company, (int) jobCount);
                })
                .toList();
    }

    @Override
    public CompanyDetailDto getCompany(UUID id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + id));

        int jobCount = (int) jobPostingRepository.countJobPostingsByCompanyId(company.getId());

        return companyMapper.toDetailDto(company, jobCount);
    }

    @Override
    @Transactional
    public CompanyResponseDto createCompany(CompanyRequestDto requestDto) {
        Company company = companyMapper.toEntity(requestDto);

        if(companyRepository.existsByNameIgnoreCase(company.getName())){
            throw new IllegalArgumentException("Company already exists with name " + company.getName());
        }

        Company newCompany = companyRepository.save(company);

        return companyMapper.toDto(newCompany);
    }

    @Override
    @Transactional
    public CompanyResponseDto updateCompany(UUID id, CompanyRequestDto requestDto) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with id " + id));

        companyMapper.updateEntityFromDto(requestDto, company);

        return companyMapper.toDto(company);
    }
}
