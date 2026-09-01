package dev.kafetzi.jobmappe.posting.controller;

import dev.kafetzi.jobmappe.posting.model.dto.CompanyResponseDto;
import dev.kafetzi.jobmappe.posting.model.dto.CompanyRequestDto;
import dev.kafetzi.jobmappe.posting.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<CompanyResponseDto>> getCompanies() {
        List<CompanyResponseDto> companies = companyService.getCompanies();

        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDto> getCompany(@PathVariable UUID id) {
        CompanyResponseDto company = companyService.getCompany(id);

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
