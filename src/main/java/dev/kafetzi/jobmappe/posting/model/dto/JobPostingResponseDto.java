package dev.kafetzi.jobmappe.posting.model.dto;

import dev.kafetzi.jobmappe.posting.model.JobPostingStatus;

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
