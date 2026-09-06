package dev.kafetzi.jobmappe.tracking.service;

import dev.kafetzi.jobmappe.tracking.model.dto.ApplicationCompactDto;
import dev.kafetzi.jobmappe.tracking.model.dto.ApplicationDetailDto;
import dev.kafetzi.jobmappe.tracking.model.dto.ApplicationPatchDto;
import dev.kafetzi.jobmappe.tracking.model.dto.ApplicationResponseDto;

import java.util.List;
import java.util.UUID;

public interface ApplicationService {
    List<ApplicationCompactDto> getAllApplications();
    ApplicationResponseDto createApplication(UUID jobPostingId);
    ApplicationDetailDto getApplication(UUID id);
    ApplicationResponseDto updateApplication(UUID id, ApplicationPatchDto applicationPatchDto);
}
