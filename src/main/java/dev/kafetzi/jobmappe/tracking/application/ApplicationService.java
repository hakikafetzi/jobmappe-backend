package dev.kafetzi.jobmappe.tracking.application;

import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationCompactDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationDetailDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationPatchDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ApplicationService {
    Page<ApplicationCompactDto> getAllApplications(ApplicationStatus status, ApplicationPriority priority, Pageable pageable);
    ApplicationResponseDto startApplication(UUID jobPostingId);
    ApplicationDetailDto getApplication(UUID id);
    ApplicationResponseDto updateApplication(UUID id, ApplicationPatchDto applicationPatchDto);
}
