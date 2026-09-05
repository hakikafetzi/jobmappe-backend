package dev.kafetzi.jobmappe.tracking.mapper;

import dev.kafetzi.jobmappe.tracking.model.JobPosting;
import dev.kafetzi.jobmappe.tracking.model.dto.*;

public interface JobPostingMapper {
    JobPostingResponseDto toDto(JobPosting jobPosting);
    JobPosting toEntity(JobPostingRequestDto jobPostingRequestDto);
    JobPostingDetailDto toDetailDto(JobPosting jobPosting, Boolean hasApplication);
    JobPostingCompactDto toCompactDto(JobPosting jobPosting, Boolean hasApplication);
    void updateEntityFromDto(JobPostingRequestDto jobPostingRequestDto, JobPosting jobPosting);
}
