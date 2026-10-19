package dev.kafetzi.jobmappe.tracking.application;

import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationCompactDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationDetailDto;

public interface ApplicationMapper {
    ApplicationCompactDto toCompactDto(Application application);
    ApplicationDetailDto toDetailDto(Application application);
}
