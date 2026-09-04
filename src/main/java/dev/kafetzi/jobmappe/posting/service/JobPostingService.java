package dev.kafetzi.jobmappe.posting.service;

import dev.kafetzi.jobmappe.posting.model.dto.*;

import java.util.List;
import java.util.UUID;

public interface JobPostingService {
    List<JobPostingCompactDto> getAllJobPostings();
    JobPostingDetailDto getJobPosting(UUID postingId);
    JobPostingResponseDto createJobPosting(UUID companyId, JobPostingRequestDto jobPostingRequestDto);
    JobPostingResponseDto updateJobPosting(UUID id, JobPostingRequestDto jobPostingRequestDto);
    List<JobPostingCompactDto> getAllJobPostingsByCompanyId(UUID companyId);
}
