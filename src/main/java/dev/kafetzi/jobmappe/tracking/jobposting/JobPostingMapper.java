package dev.kafetzi.jobmappe.tracking.jobposting;

import dev.kafetzi.jobmappe.tracking.company.Company;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingCompactDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingDetailDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingRequestDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingSummaryDto;

import java.util.UUID;

public interface JobPostingMapper {
    JobPostingSummaryDto toSummaryDto(JobPosting jobPosting);
    JobPosting toEntity(JobPostingRequestDto jobPostingRequestDto, Company company);
    JobPostingDetailDto toDetailDto(JobPosting jobPosting, UUID applicationId);
    JobPostingCompactDto toCompactDto(JobPosting jobPosting, boolean hasApplication);
    void updateEntityFromDto(JobPostingRequestDto jobPostingRequestDto, JobPosting jobPosting);
}
