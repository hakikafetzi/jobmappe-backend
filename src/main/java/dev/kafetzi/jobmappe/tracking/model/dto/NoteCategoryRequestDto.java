package dev.kafetzi.jobmappe.tracking.model.dto;

import jakarta.validation.constraints.NotBlank;

public record NoteCategoryRequestDto(
        @NotBlank(message = "Note Category Name is required")
        String name
) {
}
