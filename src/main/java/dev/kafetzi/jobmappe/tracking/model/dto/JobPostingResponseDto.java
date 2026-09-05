package dev.kafetzi.jobmappe.tracking.model.dto;

import dev.kafetzi.jobmappe.tracking.model.JobPostingStatus;

import java.time.LocalDate;
import java.util.UUID;

public record JobPostingResponseDto(
        UUID id,
        String title,
        String description,
        String location,
        String url,
        JobPostingStatus status,
        LocalDate publishedAt
) {
}
