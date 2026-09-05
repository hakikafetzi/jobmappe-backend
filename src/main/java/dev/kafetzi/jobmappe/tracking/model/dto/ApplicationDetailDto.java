package dev.kafetzi.jobmappe.tracking.model.dto;

import dev.kafetzi.jobmappe.tracking.model.ApplicationPriority;
import dev.kafetzi.jobmappe.tracking.model.ApplicationStatus;

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
