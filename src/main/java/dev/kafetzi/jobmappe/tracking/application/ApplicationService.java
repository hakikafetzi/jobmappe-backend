package dev.kafetzi.jobmappe.tracking.application;

import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationCompactDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationDetailDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationPatchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ApplicationService {
    Page<ApplicationCompactDto> getAllApplications(
            String q, ApplicationStatus status, ApplicationPriority priority, Pageable pageable
    );
    ApplicationDetailDto startApplication(UUID jobPostingId);
    ApplicationDetailDto getApplication(UUID id);
    ApplicationDetailDto updateApplication(UUID id, ApplicationPatchDto applicationPatchDto);
}
