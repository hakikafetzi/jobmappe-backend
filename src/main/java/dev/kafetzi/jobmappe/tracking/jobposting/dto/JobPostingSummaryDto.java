package dev.kafetzi.jobmappe.tracking.jobposting.dto;

import dev.kafetzi.jobmappe.tracking.jobposting.JobPostingStatus;

import java.time.LocalDate;
import java.util.UUID;

public record JobPostingSummaryDto(
        UUID id,
        String title,
        String description,
        String location,
        String url,
        JobPostingStatus status,
        LocalDate publishedAt,
        LocalDate archivedAt
) {
}
