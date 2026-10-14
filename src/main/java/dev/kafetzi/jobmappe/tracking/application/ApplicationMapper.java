package dev.kafetzi.jobmappe.tracking.application;

import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationCompactDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationDetailDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationResponseDto;

public interface ApplicationMapper {
    ApplicationCompactDto toCompactDto(Application application, Integer daysSinceApplication);
    ApplicationResponseDto toResponseDto(Application application);
    ApplicationDetailDto toDetailDto(Application application);
}
