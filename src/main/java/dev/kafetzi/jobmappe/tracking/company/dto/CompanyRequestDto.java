package dev.kafetzi.jobmappe.tracking.company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CompanyRequestDto(
        @NotBlank(message = "The Name is required")
        @Size(max = 255, message = "The Name must not exceed 255 characters")
        String name,

        @Size(max = 255, message = "The Website must not exceed 255 characters")
        String website,

        @Size(max = 255, message = "The Street must not exceed 255 characters")
        String street,

        @Size(max = 20, message = "The Postal Code must not exceed 20 characters")
        String postalCode,

        @Size(max = 255, message = "The City must not exceed 255 characters")
        String city
) {
}
