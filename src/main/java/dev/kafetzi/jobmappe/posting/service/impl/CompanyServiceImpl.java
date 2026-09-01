package dev.kafetzi.jobmappe.posting.service.impl;

import dev.kafetzi.jobmappe.posting.mapper.CompanyMapper;
import dev.kafetzi.jobmappe.posting.model.Company;
import dev.kafetzi.jobmappe.posting.model.dto.CompanyRequestDto;
import dev.kafetzi.jobmappe.posting.model.dto.CompanyResponseDto;
import dev.kafetzi.jobmappe.posting.repository.CompanyRepository;
import dev.kafetzi.jobmappe.posting.service.CompanyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Override
    public List<CompanyResponseDto> getCompanies() {
        return companyRepository.findAllWithJobPostings()
                .stream()
                .map(companyMapper::toDto)
                .toList();
    }

    @Override
    public CompanyResponseDto getCompany(UUID id) {
        Company company = companyRepository.findByIdWithJobPostings(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + id));

        return companyMapper.toDto(company);
    }

    @Override
    @Transactional
    public CompanyResponseDto createCompany(CompanyRequestDto requestDto) {
        Company company = companyMapper.toEntity(requestDto);

        if(companyRepository.existsByNameIgnoreCase(company.getName())){
            throw new IllegalArgumentException("Company already exists with name " + company.getName());
        }

        Company updatedCompany = companyRepository.save(company);

        return companyMapper.toDto(updatedCompany);
    }

    @Override
    @Transactional
    public CompanyResponseDto updateCompany(UUID id, CompanyRequestDto requestDto) {
        Company company = companyRepository.findByIdWithJobPostings(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with id " + id));

        companyMapper.updateEntityFromDto(requestDto, company);

        return companyMapper.toDto(company);
    }
}
