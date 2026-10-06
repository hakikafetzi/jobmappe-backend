package dev.kafetzi.jobmappe.tracking.application;

import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationCompactDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationDetailDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationPatchDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationResponseDto;

import java.util.List;
import java.util.UUID;

public interface ApplicationService {
    List<ApplicationCompactDto> getAllApplications();
    ApplicationResponseDto startApplication(UUID jobPostingId);
    ApplicationDetailDto getApplication(UUID id);
    ApplicationResponseDto updateApplication(UUID id, ApplicationPatchDto applicationPatchDto);
}
