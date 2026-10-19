package dev.kafetzi.jobmappe.tracking.application.impl;

import dev.kafetzi.jobmappe.tracking.application.ApplicationMapper;
import dev.kafetzi.jobmappe.tracking.company.CompanyMapper;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPostingMapper;
import dev.kafetzi.jobmappe.tracking.application.Application;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationCompactDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationMapperImpl implements ApplicationMapper {
    private final JobPostingMapper jobPostingMapper;
    private final CompanyMapper companyMapper;

    @Override
    public ApplicationCompactDto toCompactDto(Application application, Integer daysSinceApplication) {
        return new ApplicationCompactDto(
                application.getId(),
                application.getJobPosting().getTitle(),
                application.getJobPosting().getCompany().getName(),
                application.getStatus(),
                application.getPriority(),
                application.getAppliedAt(),
                daysSinceApplication
        );
    }

    @Override
    public ApplicationDetailDto toDetailDto(Application application) {
        return new ApplicationDetailDto(
                application.getId(),
                jobPostingMapper.toSummaryDto(application.getJobPosting()),
                companyMapper.toSummaryDto(application.getJobPosting().getCompany()),
                application.getStatus(),
                application.getPriority(),
                application.getAppliedAt(),
                application.getAppliedVia(),
                application.getPortalUrl(),
                application.getClosedAt(),
                application.getCreatedAt()
        );
    }
}
