package dev.kafetzi.jobmappe.tracking.application;

import dev.kafetzi.jobmappe.common.pagination.PagedResponse;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationCompactDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationDetailDto;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationPatchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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

@Tag(name = "Applications", description = "Job applications and their status")
@RestController
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @Operation(summary = "List applications",
            description = "Returns a page of applications, optionally filtered by status and priority. "
                    + "q searches job title and company name and cannot be combined with a filter.")
    @ApiResponse(responseCode = "200", description = "Page of applications")
    @ApiResponse(responseCode = "400",
            description = "Invalid sort property, unknown status or priority, or q combined with a filter",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping("/api/applications")
    public ResponseEntity<PagedResponse<ApplicationCompactDto>> getAllApplications(
            @RequestParam(name = "q", defaultValue = "") String q,
            @RequestParam(name = "status", required = false) ApplicationStatus status,
            @RequestParam(name = "priority", required = false) ApplicationPriority priority,
            @ParameterObject @SortDefault(
                    sort = {"createdAt", "id"},
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        Page<ApplicationCompactDto> applications = applicationService.getAllApplications(q, status, priority, pageable);

        return ResponseEntity.ok(PagedResponse.from(applications));
    }

    @Operation(summary = "Get an application")
    @ApiResponse(responseCode = "200", description = "Application found")
    @ApiResponse(responseCode = "404", description = "Application not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping("/api/applications/{id}")
    public ResponseEntity<ApplicationDetailDto> getApplication(@PathVariable UUID id) {
        ApplicationDetailDto application = applicationService.getApplication(id);

        return ResponseEntity.ok(application);
    }

    @Operation(summary = "Partially update an application",
            description = "Only the provided fields are changed. A status change must be an allowed transition.")
    @ApiResponse(responseCode = "200", description = "Application updated")
    @ApiResponse(responseCode = "400", description = "Invalid request body; field errors are listed in 'errors'",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Application not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "The status transition is not allowed",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @PatchMapping("/api/applications/{id}")
    public ResponseEntity<ApplicationDetailDto> updateApplication(
            @PathVariable UUID id,
            @Valid @RequestBody ApplicationPatchDto applicationPatchDto
    ) {
        ApplicationDetailDto application = applicationService.updateApplication(id, applicationPatchDto);

        return ResponseEntity.ok(application);
    }

    @Operation(summary = "Start an application for a job posting",
            description = "Creates an application with status IN_PROGRESS.")
    @ApiResponse(responseCode = "201", description = "Application started")
    @ApiResponse(responseCode = "404", description = "Job posting not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "The posting already has an application or is archived",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @PostMapping("/api/job-postings/{jobPostingId}/applications")
    public ResponseEntity<ApplicationDetailDto> startApplication(@PathVariable UUID jobPostingId) {
        ApplicationDetailDto application = applicationService.startApplication(jobPostingId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/applications/{id}")
                .buildAndExpand(application.id())
                .toUri();

        return ResponseEntity.created(location).body(application);
    }

}
