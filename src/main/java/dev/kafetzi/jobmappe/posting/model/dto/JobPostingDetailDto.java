package dev.kafetzi.jobmappe.posting.model.dto;

import dev.kafetzi.jobmappe.posting.model.JobPostingStatus;

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
        LocalDate publishedAt,
        LocalDateTime createdAt
) {
}
