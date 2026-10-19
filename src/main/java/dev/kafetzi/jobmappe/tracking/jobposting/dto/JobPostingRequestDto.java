package dev.kafetzi.jobmappe.tracking.jobposting.dto;

import dev.kafetzi.jobmappe.common.text.TextUtils;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPostingStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record JobPostingRequestDto(
        @NotBlank(message = "The title is required")
        @Size(max = 255, message = "The title must not exceed 255 characters")
        String title,

        String description,

        @Pattern(regexp = ".*\\S.*", message = "The location must not be blank or contain only whitespace")
        @Size(max = 255, message = "The location must not exceed 255 characters")
        String location,

        @NotBlank(message = "The url is required")
        @Size(max = 1000, message = "The url must not exceed 1000 characters")
        String url,

        @NotNull(message = "The status is required")
        JobPostingStatus status,

        @PastOrPresent(message = "The publishedAt date must not be in the future")
        LocalDate publishedAt
) {
        public JobPostingRequestDto {
                title = TextUtils.normalize(title);
                location = TextUtils.normalize(location);
                url = TextUtils.normalize(url);
        }
}
