package dev.kafetzi.jobmappe.tracking.application;

import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationCompactDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationDetailDto;

public interface ApplicationMapper {
    ApplicationCompactDto toCompactDto(Application application, Integer daysSinceApplication);
    ApplicationDetailDto toDetailDto(Application application);
}
