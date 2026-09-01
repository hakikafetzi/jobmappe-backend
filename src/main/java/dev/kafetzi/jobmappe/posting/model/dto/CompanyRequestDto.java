package dev.kafetzi.jobmappe.posting.model.dto;

import jakarta.validation.constraints.NotBlank;

public record CompanyRequestDto(
        @NotBlank(message = "The Name is required")
        String name,

        String website
) {
}
