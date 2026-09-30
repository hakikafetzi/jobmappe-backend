package dev.kafetzi.jobmappe.tracking.jobposting.impl;

import dev.kafetzi.jobmappe.tracking.jobposting.JobPostingMapper;
import dev.kafetzi.jobmappe.tracking.company.Company;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPosting;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingCompactDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingDetailDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingRequestDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingResponseDto;
import dev.kafetzi.jobmappe.tracking.application.ApplicationRepository;
import dev.kafetzi.jobmappe.tracking.company.CompanyRepository;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPostingRepository;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPostingService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobPostingServiceImpl implements JobPostingService {
    private final JobPostingRepository jobPostingRepository;
    private final CompanyRepository companyRepository;
    private final ApplicationRepository applicationRepository;
    private final JobPostingMapper jobPostingMapper;

    @Override
    public List<JobPostingCompactDto> getAllJobPostings() {
        List<JobPosting> jobPostings = jobPostingRepository.findAllWithCompany();
        Set<UUID> jobPostingIds =  Set.copyOf(applicationRepository.findAllAppliedJobPostingIds());

        return jobPostings
                .stream()
                .map(
                        jobPosting -> jobPostingMapper.toCompactDto(
                                jobPosting,
                                jobPostingIds.contains(jobPosting.getId())
                        )
                )
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
    public JobPostingDetailDto getJobPosting(UUID postingId) {
        JobPosting jobPosting = jobPostingRepository.findByIdWithCompany(postingId)
                .orElseThrow(() -> new IllegalArgumentException("Job posting with id: " + postingId + " not found"));
        UUID applicationId = applicationRepository.findApplicationIdByJobPostingId(postingId)
                .orElse(null);


        return jobPostingMapper.toDetailDto(jobPosting, applicationId);
    }

    @Override
    @Transactional
    public JobPostingResponseDto updateJobPosting(UUID id, JobPostingRequestDto jobPostingRequestDto) {
        JobPosting jobPosting = jobPostingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job posting with id: " + id + " not found"));

        jobPostingMapper.updateEntityFromDto(jobPostingRequestDto, jobPosting);

        return jobPostingMapper.toDto(jobPosting);
    }

    @Override
    public List<JobPostingCompactDto> getAllJobPostingsByCompanyId(UUID companyId) {
        List<JobPosting> jobPostings = jobPostingRepository.findAllByCompanyId(companyId);
        Set<UUID> jobPostingIds =  Set.copyOf(applicationRepository.findAllAppliedJobPostingIds());

        return jobPostings
                .stream()
                .map(
                        jobPosting -> jobPostingMapper.toCompactDto(
                                jobPosting,
                                jobPostingIds.contains(jobPosting.getId())
                        )
                )
                .toList();
    }
}
