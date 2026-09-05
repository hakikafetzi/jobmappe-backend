package dev.kafetzi.jobmappe.tracking.service;

import dev.kafetzi.jobmappe.tracking.model.dto.ApplicationCompactDto;
import dev.kafetzi.jobmappe.tracking.model.dto.ApplicationResponseDto;
import dev.kafetzi.jobmappe.tracking.model.dto.ApplicationStatusDto;

import java.util.List;
import java.util.UUID;

public interface ApplicationService {
    List<ApplicationCompactDto> getAllApplications();
    ApplicationStatusDto getApplicationByJobPostingId(UUID jobPostingId);
    ApplicationResponseDto createApplication(UUID jobPostingId);
}
