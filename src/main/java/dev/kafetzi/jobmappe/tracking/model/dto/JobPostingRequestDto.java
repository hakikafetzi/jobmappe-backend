package dev.kafetzi.jobmappe.tracking.model.dto;

import dev.kafetzi.jobmappe.tracking.model.JobPostingStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record JobPostingRequestDto(
        @NotBlank(message = "The Job Title is required")
        String title,

        String description,

        String location,

        @NotBlank(message = "The source URL is required")
        String url,

        @NotNull(message = "The status is required")
        JobPostingStatus status,

        LocalDate publishedAt
) {
}
