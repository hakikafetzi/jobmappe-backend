package dev.kafetzi.jobmappe.tracking.company.dto;

import dev.kafetzi.jobmappe.common.text.TextUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CompanyRequestDto(
        @NotBlank(message = "The name is required")
        @Size(max = 255, message = "The name must not exceed 255 characters")
        String name,

        @Pattern(
                regexp = "https?://\\S+",
                flags = Pattern.Flag.CASE_INSENSITIVE,
                message = "The url must start with http:// or https://"
        )
        @Size(max = 255, message = "The website must not exceed 255 characters")
        String website,

        @Pattern(regexp = ".*\\S.*", message = "The street must not be blank or contain only whitespace")
        @Size(max = 255, message = "The street must not exceed 255 characters")
        String street,

        @Pattern(regexp = ".*\\S.*", message = "The postalCode must not be blank or contain only whitespace")
        @Size(max = 20, message = "The postalCode must not exceed 20 characters")
        String postalCode,

        @Pattern(regexp = ".*\\S.*", message = "The city must not be blank or contain only whitespace")
        @Size(max = 255, message = "The city must not exceed 255 characters")
        String city
) {
        public CompanyRequestDto {
                name = TextUtils.normalize(name);
                website = TextUtils.normalize(website);
                street = TextUtils.normalize(street);
                postalCode = TextUtils.normalize(postalCode);
                city = TextUtils.normalize(city);
        }
}
