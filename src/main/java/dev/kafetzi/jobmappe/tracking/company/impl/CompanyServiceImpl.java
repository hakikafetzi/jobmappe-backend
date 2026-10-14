package dev.kafetzi.jobmappe.tracking.company.impl;

import dev.kafetzi.jobmappe.common.pagination.PageableUtils;
import dev.kafetzi.jobmappe.exception.ConflictException;
import dev.kafetzi.jobmappe.exception.ResourceNotFoundException;
import dev.kafetzi.jobmappe.tracking.company.CompanyMapper;
import dev.kafetzi.jobmappe.tracking.company.Company;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyCompactDto;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyDetailDto;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyRequestDto;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyResponseDto;
import dev.kafetzi.jobmappe.tracking.company.CompanyRepository;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPostingRepository;
import dev.kafetzi.jobmappe.tracking.company.CompanyService;
import dev.kafetzi.jobmappe.tracking.jobposting.projection.CountByCompanyProjection;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

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
    public Page<CompanyCompactDto> getCompanies(String q, Pageable pageable) {
        String normalizedQuery = q.strip();
        Pageable stablePageable = PageableUtils.withStableSort(pageable, Company.class);
        Page<Company> companies = companyRepository.findByNameContainingIgnoreCase(normalizedQuery, stablePageable);

        List<UUID> companyIds = companies.getContent().stream()
                .map(Company::getId)
                .toList();

        Map<UUID, Long> jobCountsByCompanyId;

        if (companyIds.isEmpty()) {
            jobCountsByCompanyId = Map.of();
        } else {
            jobCountsByCompanyId = jobPostingRepository.countJobPostingsPerCompanyIds(companyIds)
                    .stream()
                    .collect(Collectors.toMap(
                            CountByCompanyProjection::getCompanyId,
                            CountByCompanyProjection::getCount
                    ));
        }

        return companies.map(company -> {
                    long jobCount = jobCountsByCompanyId.getOrDefault(company.getId(), 0L);
                    return companyMapper.toCompactDto(company, jobCount);
                });
    }

    @Override
    public CompanyDetailDto getCompany(UUID id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Company with id " + id + " was not found."
                        )
                );

        long jobCount = jobPostingRepository.countJobPostingsByCompanyId(company.getId());

        return companyMapper.toDetailDto(company, jobCount);
    }

    @Override
    @Transactional
    public CompanyResponseDto createCompany(CompanyRequestDto requestDto) {
        Company company = companyMapper.toEntity(requestDto);
        String normalizedCompanyName = company.getName().strip();

        if(companyRepository.existsByNameIgnoreCase(normalizedCompanyName)) {
            throw new ConflictException(
                    "Company with name '" + normalizedCompanyName + "' already exists."
            );
        }

        company.setName(normalizedCompanyName);
        Company newCompany = companyRepository.save(company);

        return companyMapper.toDto(newCompany);
    }

    @Override
    @Transactional
    public CompanyResponseDto updateCompany(UUID id, CompanyRequestDto requestDto) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Company with id " + id + " was not found."
                        )
                );

        String normalizedRequestName = requestDto.name().strip();

        if(companyRepository.existsByNameIgnoreCaseAndIdNot(normalizedRequestName, id)) {
            throw new ConflictException(
                    "Company with name '" + normalizedRequestName + "' already exists."
            );
        }

        companyMapper.updateEntityFromDto(requestDto, company);
        company.setName(normalizedRequestName);

        return companyMapper.toDto(company);
    }
}
