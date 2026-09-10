package dev.kafetzi.jobmappe.tracking.application.dto;

import dev.kafetzi.jobmappe.tracking.company.dto.CompanyResponseDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingResponseDto;
import dev.kafetzi.jobmappe.tracking.application.ApplicationPriority;
import dev.kafetzi.jobmappe.tracking.application.ApplicationStatus;

import java.time.LocalDate;
import java.util.UUID;

public record ApplicationDetailDto(
        UUID id,
        JobPostingResponseDto jobPosting,
        CompanyResponseDto company,
        ApplicationStatus status,
        ApplicationPriority priority,
        LocalDate appliedAt,
        String appliedVia,
        String portal,
        LocalDate closedAt
) {
}
