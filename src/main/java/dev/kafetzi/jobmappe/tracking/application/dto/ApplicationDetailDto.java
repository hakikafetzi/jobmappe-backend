package dev.kafetzi.jobmappe.tracking.application.dto;

import dev.kafetzi.jobmappe.tracking.application.ApplicationChannel;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanySummaryDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingSummaryDto;
import dev.kafetzi.jobmappe.tracking.application.ApplicationPriority;
import dev.kafetzi.jobmappe.tracking.application.ApplicationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ApplicationDetailDto(
        UUID id,
        JobPostingSummaryDto jobPosting,
        CompanySummaryDto company,
        ApplicationStatus status,
        ApplicationPriority priority,
        LocalDate appliedAt,
        ApplicationChannel appliedVia,
        String portalUrl,
        LocalDate closedAt,
        LocalDateTime createdAt
) {
}
