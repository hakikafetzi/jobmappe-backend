package dev.kafetzi.jobmappe.tracking.company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CompanyRequestDto(
        @NotBlank(message = "The Name is required")
        @Size(max = 255, message = "The Name must not exceed 255 characters")
        String name,

        @Pattern(regexp = ".*\\S.*", message = "The website must not be blank or contain only whitespace")
        @Size(max = 255, message = "The Website must not exceed 255 characters")
        String website,

        @Pattern(regexp = ".*\\S.*", message = "The Street must not be blank or contain only whitespace")
        @Size(max = 255, message = "The Street must not exceed 255 characters")
        String street,

        @Pattern(regexp = ".*\\S.*", message = "The Postal Code must not be blank or contain only whitespace")
        @Size(max = 20, message = "The Postal Code must not exceed 20 characters")
        String postalCode,

        @Pattern(regexp = ".*\\S.*", message = "The City must not be blank or contain only whitespace")
        @Size(max = 255, message = "The City must not exceed 255 characters")
        String city
) {
}
