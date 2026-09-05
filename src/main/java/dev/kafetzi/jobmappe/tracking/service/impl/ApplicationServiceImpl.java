package dev.kafetzi.jobmappe.tracking.service.impl;

import dev.kafetzi.jobmappe.tracking.mapper.ApplicationMapper;
import dev.kafetzi.jobmappe.tracking.model.Application;
import dev.kafetzi.jobmappe.tracking.model.ApplicationPriority;
import dev.kafetzi.jobmappe.tracking.model.ApplicationStatus;
import dev.kafetzi.jobmappe.tracking.model.dto.ApplicationCompactDto;
import dev.kafetzi.jobmappe.tracking.model.dto.ApplicationResponseDto;
import dev.kafetzi.jobmappe.tracking.model.dto.ApplicationStatusDto;
import dev.kafetzi.jobmappe.tracking.repository.ApplicationRepository;
import dev.kafetzi.jobmappe.tracking.service.ApplicationService;
import dev.kafetzi.jobmappe.tracking.model.JobPosting;
import dev.kafetzi.jobmappe.tracking.repository.JobPostingRepository;
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
    public ApplicationStatusDto getApplicationByJobPostingId(UUID jobPostingId) {
        Application application = applicationRepository.findApplicationByJobPostingId(jobPostingId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Application not found for job posting ID: " + jobPostingId)
                );

        return applicationMapper.toStatusDto(application);
    }

    @Override
    public ApplicationResponseDto createApplication(UUID jobPostingId) {
        if (applicationRepository.existsApplicationByJobPostingId(jobPostingId)) {
            throw new IllegalArgumentException("Application already exists for job posting ID: " + jobPostingId);
        }

        JobPosting jobPosting = jobPostingRepository.findById(jobPostingId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Job posting not found for ID: " + jobPostingId)
                );

        Application application = new Application();
        application.setJobPosting(jobPosting);
        application.setStatus(ApplicationStatus.IN_PROGRESS);
        application.setPriority(ApplicationPriority.NONE);

        Application savedApplication = applicationRepository.save(application);

        return applicationMapper.toResponseDto(savedApplication);
    }
}
