package dev.kafetzi.jobmappe.tracking.jobposting.dto;

import dev.kafetzi.jobmappe.tracking.jobposting.JobPostingStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record JobPostingRequestDto(
        @NotBlank(message = "The Job Title is required")
        @Size(max = 255, message = "The Job Title must not exceed 255 characters")
        String title,

        String description,

        @Size(max = 255, message = "The Location must not exceed 255 characters")
        String location,

        @NotBlank(message = "The source URL is required")
        @Size(max = 1000, message = "The source URL must not exceed 1000 characters")
        String url,

        @NotNull(message = "The status is required")
        JobPostingStatus status,

        LocalDate publishedAt
) {
}
