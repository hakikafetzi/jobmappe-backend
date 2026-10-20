package dev.kafetzi.jobmappe.tracking.jobposting.impl;

import dev.kafetzi.jobmappe.common.pagination.PageableUtils;
import dev.kafetzi.jobmappe.exception.ConflictException;
import dev.kafetzi.jobmappe.exception.InvalidRequestException;
import dev.kafetzi.jobmappe.exception.ResourceNotFoundException;
import dev.kafetzi.jobmappe.tracking.jobposting.*;
import dev.kafetzi.jobmappe.tracking.company.Company;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.*;
import dev.kafetzi.jobmappe.tracking.application.ApplicationRepository;
import dev.kafetzi.jobmappe.tracking.company.CompanyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobPostingServiceImpl implements JobPostingService {
    private final JobPostingRepository jobPostingRepository;
    private final CompanyRepository companyRepository;
    private final ApplicationRepository applicationRepository;
    private final JobPostingMapper jobPostingMapper;

    @Override
    public Page<JobPostingCompactDto> getAllJobPostings(String q, JobPostingFilter filter, Pageable pageable) {
        Pageable stablePageable = PageableUtils.withStableSort(pageable, JobPosting.class);
        String normalizedQuery = q.strip();

        if (!normalizedQuery.isEmpty() && filter != JobPostingFilter.ALL) {
            throw new InvalidRequestException("Search is only supported with filter ALL.");
        }

        Page<JobPosting> jobPostings = switch (filter) {
            case ALL -> jobPostingRepository.findAllWithCompany(normalizedQuery, stablePageable);
            case INACTIVE -> jobPostingRepository.findAllByStatus(JobPostingStatus.INACTIVE, stablePageable);
            case ARCHIVED -> jobPostingRepository.findAllByArchivedAtIsNotNull(stablePageable);
            case OPEN -> jobPostingRepository.findAllWithoutApplications(JobPostingStatus.ACTIVE, stablePageable);
            case APPLIED -> jobPostingRepository.findAllWithApplications(stablePageable);
        };

        return mapToCompactDtoPage(jobPostings);
    }

    @Override
    @Transactional
    public JobPostingDetailDto createJobPosting(UUID companyId, JobPostingRequestDto jobPostingRequestDto) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Company with id " + companyId + " was not found."
                        )
                );

        JobPosting jobPosting = jobPostingMapper.toEntity(jobPostingRequestDto, company);
        JobPosting jobPostingSaved = jobPostingRepository.save(jobPosting);
        log.info("Job posting {} created", jobPostingSaved.getId());

        return jobPostingMapper.toDetailDto(jobPostingSaved, null);
    }

    @Override
    public JobPostingDetailDto getJobPosting(UUID postingId) {
        JobPosting jobPosting = jobPostingRepository.findByIdWithCompany(postingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Job posting with id " + postingId + " was not found."
                        )
                );

        return mapToDetailDto(jobPosting);
    }

    @Override
    @Transactional
    public JobPostingDetailDto updateJobPosting(UUID id, JobPostingRequestDto jobPostingRequestDto) {
        JobPosting jobPosting = jobPostingRepository.findByIdWithCompany(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Job posting with id " + id + " was not found."
                        )
                );

        jobPostingMapper.updateEntityFromDto(jobPostingRequestDto, jobPosting);
        log.info("Job posting {} updated", jobPosting.getId());
        
        return mapToDetailDto(jobPosting);
    }

    @Override
    public Page<JobPostingCompactDto> getAllJobPostingsByCompanyId(UUID companyId, Pageable pageable) {
        if (!companyRepository.existsById(companyId)) {
            throw new ResourceNotFoundException(
                    "Company with id " + companyId + " was not found."
            );
        }

        Page<JobPosting> jobPostings = jobPostingRepository.findAllByCompanyId(
                companyId, PageableUtils.withStableSort(pageable, JobPosting.class)
        );

        return mapToCompactDtoPage(jobPostings);
    }

    @Transactional
    @Override
    public JobPostingDetailDto setArchived(UUID id, JobPostingArchiveDto jobPostingArchiveDto) {
        JobPosting jobPosting = jobPostingRepository.findByIdWithCompany(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Job posting with id " + id + " was not found."
                        )
                );

        if (jobPostingArchiveDto.archived() && applicationRepository.existsApplicationByJobPostingId(id)) {
            throw new ConflictException(
                    "Cannot archive job posting with id " + id + " because it has an associated application."
            );
        }

        if (jobPostingArchiveDto.archived()) {
            jobPosting.archive();
            log.info("Job posting {} archived", jobPosting.getId());
        } else {
            jobPosting.restore();
            log.info("Job posting {} restored", jobPosting.getId());
        }

        return mapToDetailDto(jobPosting);
    }

    private Page<JobPostingCompactDto> mapToCompactDtoPage(Page<JobPosting> jobPostings) {

        List<UUID> jobPostingIds = jobPostings.getContent().stream()
                .map(JobPosting::getId)
                .toList();

        Set<UUID> jobPostingAppliedIds;

        if (jobPostingIds.isEmpty()) {
            jobPostingAppliedIds = Set.of();
        } else {
            jobPostingAppliedIds = Set.copyOf(applicationRepository.findAllAppliedJobPostingIds(jobPostingIds));
        }

        return jobPostings
                .map(
                        jobPosting -> jobPostingMapper.toCompactDto(
                                jobPosting,
                                jobPostingAppliedIds.contains(jobPosting.getId())
                        )
                );
    }

    private JobPostingDetailDto mapToDetailDto(JobPosting jobPosting) {
        UUID applicationId = applicationRepository.findApplicationIdByJobPostingId(jobPosting.getId())
                .orElse(null);

        return jobPostingMapper.toDetailDto(jobPosting, applicationId);
    }
}
