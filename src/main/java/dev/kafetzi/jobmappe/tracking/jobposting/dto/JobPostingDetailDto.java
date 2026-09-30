package dev.kafetzi.jobmappe.tracking.jobposting.dto;

import dev.kafetzi.jobmappe.tracking.company.dto.CompanyResponseDto;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPostingStatus;

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
        UUID applicationId,
        LocalDate publishedAt,
        LocalDateTime createdAt
) {
}
