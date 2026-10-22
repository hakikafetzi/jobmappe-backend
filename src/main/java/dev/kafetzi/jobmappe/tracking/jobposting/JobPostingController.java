package dev.kafetzi.jobmappe.tracking.jobposting;

import dev.kafetzi.jobmappe.common.pagination.PagedResponse;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingArchiveDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingCompactDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingDetailDto;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Tag(name = "Job postings", description = "Job postings of companies")
@RestController
@RequiredArgsConstructor
public class JobPostingController {
    private final JobPostingService jobPostingService;

    @Operation(summary = "List job postings",
            description = "Returns a page of job postings. Use filter (ALL, OPEN, APPLIED, INACTIVE, ARCHIVED). "
                    + "q searches title and company name and only works with filter ALL.")
    @ApiResponse(responseCode = "200", description = "Page of job postings")
    @ApiResponse(responseCode = "400", description = "Invalid sort property, or q combined with a filter other than ALL",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping("/api/job-postings")
    public ResponseEntity<PagedResponse<JobPostingCompactDto>> getAllJobPostings(
            @RequestParam(name = "q", defaultValue = "") String q,
            @RequestParam(name = "filter", defaultValue = "ALL") JobPostingFilter filter,
            @SortDefault(
                    sort = {"createdAt", "id"},
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        Page<JobPostingCompactDto> jobPostings = jobPostingService.getAllJobPostings(q, filter, pageable);

        return ResponseEntity.ok(PagedResponse.from(jobPostings));
    }

    @Operation(summary = "Get a job posting")
    @ApiResponse(responseCode = "200", description = "Job posting found")
    @ApiResponse(responseCode = "404", description = "Job posting not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping("/api/job-postings/{id}")
    public ResponseEntity<JobPostingDetailDto> getJobPosting(@PathVariable UUID id) {
        JobPostingDetailDto jobPosting = jobPostingService.getJobPosting(id);

        return ResponseEntity.ok(jobPosting);
    }

    @Operation(summary = "Update a job posting")
    @ApiResponse(responseCode = "200", description = "Job posting updated")
    @ApiResponse(responseCode = "400", description = "Invalid request body; field errors are listed in 'errors'",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Job posting not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @PutMapping("/api/job-postings/{id}")
    public ResponseEntity<JobPostingDetailDto> updateJobPosting(
            @PathVariable UUID id,
            @Valid @RequestBody JobPostingRequestDto jobPostingRequestDto
            ) {
        JobPostingDetailDto jobPosting = jobPostingService.updateJobPosting(id, jobPostingRequestDto);

        return ResponseEntity.ok(jobPosting);
    }

    @Operation(summary = "Create a job posting for a company")
    @ApiResponse(responseCode = "201", description = "Job posting created")
    @ApiResponse(responseCode = "400", description = "Invalid request body; field errors are listed in 'errors'",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Company not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @PostMapping("/api/companies/{companyId}/job-postings")
    public ResponseEntity<JobPostingDetailDto> createJobPosting(
            @PathVariable UUID companyId,
            @Valid @RequestBody JobPostingRequestDto jobPostingRequestDto
    ) {
        JobPostingDetailDto jobPosting = jobPostingService.createJobPosting(companyId, jobPostingRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/job-postings/{id}")
                .buildAndExpand(jobPosting.id())
                .toUri();

        return ResponseEntity.created(location).body(jobPosting);
    }

    @Operation(summary = "List the job postings of a company")
    @ApiResponse(responseCode = "200", description = "Page of job postings, empty if the company has none")
    @ApiResponse(responseCode = "400", description = "Invalid sort property",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Company not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping("/api/companies/{companyId}/job-postings")
    public ResponseEntity<PagedResponse<JobPostingCompactDto>> getAllJobPostingsByCompany(
            @PathVariable UUID companyId,
            @SortDefault(
                    sort = {"createdAt", "id"},
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        Page<JobPostingCompactDto> jobPostings = jobPostingService.getAllJobPostingsByCompanyId(companyId, pageable);

        return ResponseEntity.ok(PagedResponse.from(jobPostings));
    }

    @Operation(summary = "Archive or restore a job posting")
    @ApiResponse(responseCode = "200", description = "Job posting updated")
    @ApiResponse(responseCode = "400", description = "Invalid request body; field errors are listed in 'errors'",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Job posting not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409",
            description = "The posting has an application and cannot be archived, or is already in the requested state",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @PatchMapping("/api/job-postings/{id}/archive")
    public ResponseEntity<JobPostingDetailDto> setArchived(
            @PathVariable UUID id,
            @Valid @RequestBody JobPostingArchiveDto jobPostingArchiveDto
    ) {
        JobPostingDetailDto jobPosting = jobPostingService.setArchived(id, jobPostingArchiveDto);

        return ResponseEntity.ok(jobPosting);
    }
}
