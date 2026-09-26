package dev.kafetzi.jobmappe.tracking.notecategory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NoteCategoryRequestDto(
        @NotBlank(message = "Note Category Name is required")
        @Size(max = 255, message = "The Note Category Name must not exceed 255 characters")
        String name
) {
}
