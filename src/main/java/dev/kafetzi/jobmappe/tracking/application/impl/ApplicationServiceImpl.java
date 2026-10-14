package dev.kafetzi.jobmappe.tracking.application.impl;

import dev.kafetzi.jobmappe.common.pagination.PageableUtils;
import dev.kafetzi.jobmappe.exception.ConflictException;
import dev.kafetzi.jobmappe.exception.InvalidRequestException;
import dev.kafetzi.jobmappe.exception.ResourceNotFoundException;
import dev.kafetzi.jobmappe.tracking.application.*;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationCompactDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationDetailDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationPatchDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationResponseDto;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPosting;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPostingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobPostingRepository jobPostingRepository;
    private final ApplicationMapper applicationMapper;

    @Override
    public Page<ApplicationCompactDto> getAllApplications(
            String q, ApplicationStatus status, ApplicationPriority priority, Pageable pageable
    ) {
        Pageable stablePageable = PageableUtils.withStableSort(pageable, Application.class);
        String normalizedQuery = q.strip();
        Page<Application> page;

        if (!normalizedQuery.isEmpty() && (status != null || priority != null)) {
            throw new InvalidRequestException("Search is only supported with no Filter.");
        }

        if (status != null && priority != null) {
            page = applicationRepository.findAllByStatusAndPriority(status, priority, stablePageable);
        } else if (status != null) {
            page = applicationRepository.findAllByStatus(status, stablePageable);
        } else if (priority != null) {
            page = applicationRepository.findAllByPriority(priority, stablePageable);
        } else {
            page = applicationRepository.findAllWithJobPostingAndCompany(normalizedQuery, stablePageable);
        }

        return page
                .map(application -> {
                    Integer daysSinceApplication = null;

                    if (application.getAppliedAt() != null) {
                        daysSinceApplication = (int) java.time.temporal.ChronoUnit.DAYS.between(
                                application.getAppliedAt(),
                                java.time.LocalDate.now()
                        );
                    }

                    return applicationMapper.toCompactDto(application, daysSinceApplication);
                });
    }

    @Override
    @Transactional
    public ApplicationResponseDto startApplication(UUID jobPostingId) {
        if (applicationRepository.existsApplicationByJobPostingId(jobPostingId)) {
            throw new ConflictException(
                    "Application for job posting with id " + jobPostingId + " already exists."
            );
        }

        JobPosting jobPosting = jobPostingRepository.findById(jobPostingId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Job posting with id " + jobPostingId + " was not found."
                        )
                );

        if (jobPosting.getArchivedAt() != null) {
            throw new ConflictException(
                    "Job posting with id " + jobPostingId + " is archived and cannot be applied to."
            );
        }

        Application application = new Application(jobPosting);

        Application savedApplication = applicationRepository.save(application);

        return applicationMapper.toResponseDto(savedApplication);
    }

    @Override
    public ApplicationDetailDto getApplication(UUID id) {
        Application application = applicationRepository.findByIdWithJobPostingAndCompany(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application with id " + id + " was not found."
                        )
                );

        return applicationMapper.toDetailDto(application);
    }

    @Override
    @Transactional
    public ApplicationResponseDto updateApplication(UUID id, ApplicationPatchDto applicationPatchDto) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application with id " + id + " was not found."
                        )
                );

        if (applicationPatchDto.priority() != null) {
            application.setPriority(applicationPatchDto.priority());
        }

        if (applicationPatchDto.appliedVia() != null) {
            application.setAppliedVia(applicationPatchDto.appliedVia());
        }

        if (applicationPatchDto.portalUrl() != null) {
            application.setPortalUrl(applicationPatchDto.portalUrl());
        }

        if (applicationPatchDto.status() != null) {
            application.changeStatus(applicationPatchDto.status());
        }

        return applicationMapper.toResponseDto(application);
    }
}
