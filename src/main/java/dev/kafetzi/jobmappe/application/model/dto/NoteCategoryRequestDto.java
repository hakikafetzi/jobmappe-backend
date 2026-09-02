package dev.kafetzi.jobmappe.application.model.dto;

import jakarta.validation.constraints.NotBlank;

public record NoteCategoryRequestDto(
        @NotBlank(message = "Note Category Name is required")
        String name
) {
}
