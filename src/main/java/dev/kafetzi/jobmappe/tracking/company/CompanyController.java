package dev.kafetzi.jobmappe.tracking.company;

import dev.kafetzi.jobmappe.common.pagination.PagedResponse;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyCompactDto;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyDetailDto;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyResponseDto;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import org.springframework.data.domain.Pageable;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<PagedResponse<CompanyCompactDto>> getCompanies(
            @SortDefault(
                    sort = {"name", "id"},
                    direction = Sort.Direction.ASC
            )
            Pageable pageable
    ) {
        Page<CompanyCompactDto> companies = companyService.getCompanies(pageable);

        return ResponseEntity.ok(PagedResponse.from(companies));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDetailDto> getCompany(@PathVariable UUID id) {
        CompanyDetailDto company = companyService.getCompany(id);

        return ResponseEntity.ok(company);
    }

    @PostMapping
    public ResponseEntity<CompanyResponseDto> createCompany(
            @Valid @RequestBody CompanyRequestDto companyRequestDto) {
        CompanyResponseDto company = companyService.createCompany(companyRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/companies/{id}")
                .buildAndExpand(company.id())
                .toUri();

        return ResponseEntity.created(location).body(company);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponseDto> updateCompany(
            @PathVariable UUID id,
            @Valid @RequestBody CompanyRequestDto companyRequestDto
    ){
        CompanyResponseDto company = companyService.updateCompany(id, companyRequestDto);

        return ResponseEntity.ok(company);
    }
}
