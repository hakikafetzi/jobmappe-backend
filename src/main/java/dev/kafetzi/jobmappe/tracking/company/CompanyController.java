package dev.kafetzi.jobmappe.tracking.company;

import dev.kafetzi.jobmappe.common.pagination.PagedResponse;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyCompactDto;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyDetailDto;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyRequestDto;
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

@Tag(name = "Companies", description = "Companies that publish job postings")
@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @Operation(summary = "List companies",
            description = "Returns a page of companies with their number of job postings. Use q to search by name.")
    @ApiResponse(responseCode = "200", description = "Page of companies")
    @ApiResponse(responseCode = "400", description = "Invalid sort property",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping
    public ResponseEntity<PagedResponse<CompanyCompactDto>> getCompanies(
            @RequestParam(name = "q", defaultValue = "") String q,
            @ParameterObject @SortDefault(
                    sort = {"name", "id"},
                    direction = Sort.Direction.ASC
            )
            Pageable pageable
    ) {
        Page<CompanyCompactDto> companies = companyService.getCompanies(q, pageable);

        return ResponseEntity.ok(PagedResponse.from(companies));
    }

    @Operation(summary = "Get a company")
    @ApiResponse(responseCode = "200", description = "Company found")
    @ApiResponse(responseCode = "404", description = "Company not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDetailDto> getCompany(@PathVariable UUID id) {
        CompanyDetailDto company = companyService.getCompany(id);

        return ResponseEntity.ok(company);
    }

    @Operation(summary = "Create a company")
    @ApiResponse(responseCode = "201", description = "Company created")
    @ApiResponse(responseCode = "400", description = "Invalid request body; field errors are listed in 'errors'",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "A company with this name already exists",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @PostMapping
    public ResponseEntity<CompanyDetailDto> createCompany(
            @Valid @RequestBody CompanyRequestDto companyRequestDto) {
        CompanyDetailDto company = companyService.createCompany(companyRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/companies/{id}")
                .buildAndExpand(company.id())
                .toUri();

        return ResponseEntity.created(location).body(company);
    }

    @Operation(summary = "Update a company")
    @ApiResponse(responseCode = "200", description = "Company updated")
    @ApiResponse(responseCode = "400", description = "Invalid request body; field errors are listed in 'errors'",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Company not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Another company already uses this name",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @PutMapping("/{id}")
    public ResponseEntity<CompanyDetailDto> updateCompany(
            @PathVariable UUID id,
            @Valid @RequestBody CompanyRequestDto companyRequestDto
    ){
        CompanyDetailDto company = companyService.updateCompany(id, companyRequestDto);

        return ResponseEntity.ok(company);
    }
}
