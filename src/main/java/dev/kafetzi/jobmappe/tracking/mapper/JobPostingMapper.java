package dev.kafetzi.jobmappe.tracking.mapper;

import dev.kafetzi.jobmappe.tracking.model.JobPosting;
import dev.kafetzi.jobmappe.tracking.model.dto.*;

public interface JobPostingMapper {
    JobPostingResponseDto toDto(JobPosting jobPosting);
    JobPosting toEntity(JobPostingRequestDto jobPostingRequestDto);
    JobPostingDetailDto toDetailDto(JobPosting jobPosting);
    JobPostingCompactDto toCompactDto(JobPosting jobPosting);
    void updateEntityFromDto(JobPostingRequestDto jobPostingRequestDto, JobPosting jobPosting);
}
