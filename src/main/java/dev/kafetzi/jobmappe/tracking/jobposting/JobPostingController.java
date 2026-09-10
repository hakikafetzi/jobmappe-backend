package dev.kafetzi.jobmappe.tracking.jobposting;

import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingCompactDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingDetailDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingRequestDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class JobPostingController {
    private final JobPostingService jobPostingService;

    @GetMapping("/api/job-postings")
    public ResponseEntity<List<JobPostingCompactDto>> getAllJobPostings() {
        List<JobPostingCompactDto> jobPostings = jobPostingService.getAllJobPostings();

        return ResponseEntity.ok(jobPostings);
    }

    @GetMapping("/api/job-postings/{id}")
    public ResponseEntity<JobPostingDetailDto> getJobPosting(@PathVariable UUID id) {
        JobPostingDetailDto jobPosting = jobPostingService.getJobPosting(id);

        return ResponseEntity.ok(jobPosting);
    }

    @PutMapping("/api/job-postings/{id}")
    public ResponseEntity<JobPostingResponseDto> updateJobPosting(
            @PathVariable UUID id,
            @Valid @RequestBody JobPostingRequestDto jobPostingRequestDto
            ) {
        JobPostingResponseDto jobPosting = jobPostingService.updateJobPosting(id, jobPostingRequestDto);

        return ResponseEntity.ok(jobPosting);
    }

    @PostMapping("/api/companies/{companyId}/job-postings")
    public ResponseEntity<JobPostingResponseDto> createJobPosting(
            @PathVariable UUID companyId,
            @Valid @RequestBody JobPostingRequestDto jobPostingRequestDto
    ) {
        JobPostingResponseDto jobPosting = jobPostingService.createJobPosting(companyId, jobPostingRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/job-postings/{id}")
                .buildAndExpand(jobPosting.id())
                .toUri();

        return ResponseEntity.created(location).body(jobPosting);
    }

    @GetMapping("/api/companies/{companyId}/job-postings")
    public ResponseEntity<List<JobPostingCompactDto>> getAllJobPostingsByCompany(
            @PathVariable UUID companyId
    ) {
        List<JobPostingCompactDto> jobPostings = jobPostingService.getAllJobPostingsByCompanyId(companyId);

        return ResponseEntity.ok(jobPostings);
    }
}
