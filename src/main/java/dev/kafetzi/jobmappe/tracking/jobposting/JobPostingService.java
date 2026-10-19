package dev.kafetzi.jobmappe.tracking.jobposting;

import dev.kafetzi.jobmappe.tracking.jobposting.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface JobPostingService {
    Page<JobPostingCompactDto> getAllJobPostings(String q, JobPostingFilter filter, Pageable pageable);
    JobPostingDetailDto getJobPosting(UUID postingId);
    JobPostingDetailDto createJobPosting(UUID companyId, JobPostingRequestDto jobPostingRequestDto);
    JobPostingDetailDto updateJobPosting(UUID id, JobPostingRequestDto jobPostingRequestDto);
    Page<JobPostingCompactDto> getAllJobPostingsByCompanyId(UUID companyId, Pageable pageable);
    JobPostingDetailDto setArchived(UUID id, JobPostingArchiveDto jobPostingArchiveDto);
}
