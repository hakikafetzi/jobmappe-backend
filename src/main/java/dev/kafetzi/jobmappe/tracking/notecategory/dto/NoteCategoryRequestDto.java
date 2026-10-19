package dev.kafetzi.jobmappe.tracking.notecategory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NoteCategoryRequestDto(
        @NotBlank(message = "The name is required")
        @Size(max = 255, message = "The name must not exceed 255 characters")
        String name
) {
}
