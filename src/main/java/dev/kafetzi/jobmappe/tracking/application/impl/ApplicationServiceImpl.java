package dev.kafetzi.jobmappe.tracking.application.impl;

import dev.kafetzi.jobmappe.exception.ConflictException;
import dev.kafetzi.jobmappe.exception.ResourceNotFoundException;
import dev.kafetzi.jobmappe.tracking.application.ApplicationMapper;
import dev.kafetzi.jobmappe.tracking.application.Application;
import dev.kafetzi.jobmappe.tracking.application.ApplicationPriority;
import dev.kafetzi.jobmappe.tracking.application.ApplicationStatus;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationCompactDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationDetailDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationPatchDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationResponseDto;
import dev.kafetzi.jobmappe.tracking.application.ApplicationRepository;
import dev.kafetzi.jobmappe.tracking.application.ApplicationService;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPosting;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPostingRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobPostingRepository jobPostingRepository;
    private final ApplicationMapper applicationMapper;

    @Override
    public List<ApplicationCompactDto> getAllApplications() {
        return applicationRepository.findAllWithJobPostingAndCompany().stream()
                .map(application -> {
                    Integer daysSinceApplication = null;

                    if (application.getAppliedAt() != null) {
                        daysSinceApplication = (int) java.time.temporal.ChronoUnit.DAYS.between(
                                application.getAppliedAt(),
                                java.time.LocalDate.now()
                        );
                    }

                    return applicationMapper.toCompactDto(application, daysSinceApplication);
                })
                .toList();
    }

    @Override
    @Transactional
    public ApplicationResponseDto createApplication(UUID jobPostingId) {
        if (applicationRepository.existsApplicationByJobPostingId(jobPostingId)) {
            throw new ConflictException(
                    "Application for job posting with ID " + jobPostingId + " already exists."
            );
        }

        JobPosting jobPosting = jobPostingRepository.findById(jobPostingId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Job posting with ID " + jobPostingId + " was not found."
                        )
                );

        Application application = new Application();
        application.setJobPosting(jobPosting);
        application.setStatus(ApplicationStatus.IN_PROGRESS);
        application.setPriority(ApplicationPriority.NONE);

        Application savedApplication = applicationRepository.save(application);

        return applicationMapper.toResponseDto(savedApplication);
    }

    @Override
    public ApplicationDetailDto getApplication(UUID id) {
        Application application = applicationRepository.findByIdWithJobPostingAndCompany(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application with ID " + id + " was not found."
                        )
                );

        return applicationMapper.toDetailDto(application);
    }

    @Override
    @Transactional
    public ApplicationResponseDto updateApplication(UUID id, ApplicationPatchDto applicationPatchDto) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application with ID " + id + " was not found."
                        )
                );

        if (applicationPatchDto.status() != null) {
            application.setStatus(applicationPatchDto.status());
        }

        if (applicationPatchDto.priority() != null) {
            application.setPriority(applicationPatchDto.priority());
        }

        if (applicationPatchDto.appliedVia() != null) {
            application.setAppliedVia(applicationPatchDto.appliedVia());
        }

        if (applicationPatchDto.portalUrl() != null) {
            application.setPortalUrl(applicationPatchDto.portalUrl());
        }

        return applicationMapper.toResponseDto(application);
    }
}
