package dev.kafetzi.jobmappe.tracking.model.dto;

import dev.kafetzi.jobmappe.tracking.model.JobPostingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record JobPostingDetailDto(
        UUID id,
        CompanyResponseDto company,
        String title,
        String description,
        String location,
        String url,
        JobPostingStatus status,
        Boolean hasApplication,
        LocalDate publishedAt,
        LocalDateTime createdAt
) {
}
