package dev.kafetzi.jobmappe.posting.service.impl;

import dev.kafetzi.jobmappe.posting.mapper.JobPostingMapper;
import dev.kafetzi.jobmappe.posting.model.Company;
import dev.kafetzi.jobmappe.posting.model.JobPosting;
import dev.kafetzi.jobmappe.posting.model.dto.JobPostingRequestDto;
import dev.kafetzi.jobmappe.posting.model.dto.JobPostingResponseDto;
import dev.kafetzi.jobmappe.posting.model.dto.JobPostingWithoutCompanyResponseDto;
import dev.kafetzi.jobmappe.posting.repository.CompanyRepository;
import dev.kafetzi.jobmappe.posting.repository.JobPostingRepository;
import dev.kafetzi.jobmappe.posting.service.JobPostingService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobPostingServiceImpl implements JobPostingService {
    private final JobPostingRepository jobPostingRepository;
    private final CompanyRepository companyRepository;
    private final JobPostingMapper jobPostingMapper;

    @Override
    public List<JobPostingResponseDto> getAllJobPostings() {
        return jobPostingRepository.findAllWithCompany()
                .stream()
                .map(jobPostingMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public JobPostingResponseDto createJobPosting(UUID companyId, JobPostingRequestDto jobPostingRequestDto) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company with id" + companyId + "Not Found"));

        JobPosting jobPosting = jobPostingMapper.toEntity(jobPostingRequestDto);
        jobPosting.setCompany(company);

        JobPosting jobPostingSaved = jobPostingRepository.save(jobPosting);

        return jobPostingMapper.toDto(jobPostingSaved);
    }

    @Override
    public JobPostingResponseDto getJobPosting(UUID postingId) {
        JobPosting jobPosting = jobPostingRepository.findByIdWithCompany(postingId)
                .orElseThrow(() -> new IllegalArgumentException("Job posting with id: " + postingId + " not found"));

        return jobPostingMapper.toDto(jobPosting);
    }

    @Override
    public List<JobPostingWithoutCompanyResponseDto> getAllJobPostingsByCompanyId(UUID companyId) {
        return jobPostingRepository.findAllByCompanyId(companyId)
                .stream()
                .map(jobPostingMapper::toDtoWithoutCompany)
                .toList();
    }

    @Override
    @Transactional
    public JobPostingResponseDto updateJobPosting(UUID id, JobPostingRequestDto jobPostingRequestDto) {
        JobPosting jobPosting = jobPostingRepository.findByIdWithCompany(id)
                .orElseThrow(() -> new IllegalArgumentException("Job posting with id: " + id + " not found"));

        jobPostingMapper.updateEntityFromDto(jobPostingRequestDto, jobPosting);

        return jobPostingMapper.toDto(jobPosting);
    }
}
