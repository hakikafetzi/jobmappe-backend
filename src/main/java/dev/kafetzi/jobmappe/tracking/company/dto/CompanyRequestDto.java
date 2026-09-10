package dev.kafetzi.jobmappe.tracking.company.dto;

import jakarta.validation.constraints.NotBlank;

public record CompanyRequestDto(
        @NotBlank(message = "The Name is required")
        String name,
        String website,
        String street,
        String postalCode,
        String city
) {
}
