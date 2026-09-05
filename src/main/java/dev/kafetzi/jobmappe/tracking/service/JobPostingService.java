package dev.kafetzi.jobmappe.tracking.service;

import dev.kafetzi.jobmappe.tracking.model.dto.*;

import java.util.List;
import java.util.UUID;

public interface JobPostingService {
    List<JobPostingCompactDto> getAllJobPostings();
    JobPostingDetailDto getJobPosting(UUID postingId);
    JobPostingResponseDto createJobPosting(UUID companyId, JobPostingRequestDto jobPostingRequestDto);
    JobPostingResponseDto updateJobPosting(UUID id, JobPostingRequestDto jobPostingRequestDto);
    List<JobPostingCompactDto> getAllJobPostingsByCompanyId(UUID companyId);
}
