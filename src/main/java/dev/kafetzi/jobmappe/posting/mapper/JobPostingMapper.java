package dev.kafetzi.jobmappe.posting.mapper;

import dev.kafetzi.jobmappe.posting.model.JobPosting;
import dev.kafetzi.jobmappe.posting.model.dto.*;

public interface JobPostingMapper {
    JobPostingResponseDto toDto(JobPosting jobPosting);
    JobPosting toEntity(JobPostingRequestDto jobPostingRequestDto);
    JobPostingDetailDto toDetailDto(JobPosting jobPosting);
    JobPostingCompactDto toCompactDto(JobPosting jobPosting);
    void updateEntityFromDto(JobPostingRequestDto jobPostingRequestDto, JobPosting jobPosting);
}
