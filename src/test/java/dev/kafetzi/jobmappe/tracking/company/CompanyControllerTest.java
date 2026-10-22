package dev.kafetzi.jobmappe.tracking.company;

import dev.kafetzi.jobmappe.exception.ConflictException;
import dev.kafetzi.jobmappe.exception.ResourceNotFoundException;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyCompactDto;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyDetailDto;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(CompanyController.class)
class CompanyControllerTest {
    @Autowired MockMvcTester mvc;
    @MockitoBean CompanyService companyService;

    @Test
    void createCompany_returnsCreatedWithLocation() {
        CompanyDetailDto companyDetailDto = new CompanyDetailDto(
                UUID.randomUUID(),"Test", null, null, null, null, 0
        );
        when(companyService.createCompany(any(CompanyRequestDto.class)))
                .thenReturn(companyDetailDto);

        assertThat(mvc.post().uri("/api/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": "Test"}
                        """))
                .hasStatus(HttpStatus.CREATED)
                .hasHeader("Location", "http://localhost/api/companies/" + companyDetailDto.id())
                .bodyJson().extractingPath("$.name").isEqualTo("Test");
    }

    @Test
    void createCompany_withInvalidBody_returnsBadRequest() {
        assertThat(mvc.post().uri("/api/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name": ""}
                    """))
                .hasStatus(HttpStatus.BAD_REQUEST)
                .bodyJson().extractingPath("$.errors.name").isNotNull();
    }

    @Test
    void getCompany_returnsOk() {
        UUID companyId = UUID.randomUUID();
        CompanyDetailDto companyDetailDto = new CompanyDetailDto(
                companyId,"Test", null, null, null, null, 0
        );
        when(companyService.getCompany(companyId)).thenReturn(companyDetailDto);

        assertThat(mvc.get().uri("/api/companies/" + companyId))
                .hasStatusOk()
                .bodyJson().extractingPath("$.name").isEqualTo("Test");
    }

    @Test
    void getCompany_withUnknownId_returnsNotFound() {
        UUID companyId = UUID.randomUUID();
        when(companyService.getCompany(companyId)).thenThrow(
                new ResourceNotFoundException("Company with id " + companyId + " was not found.")
        );

        assertThat(mvc.get().uri("/api/companies/" + companyId))
                .hasStatus(HttpStatus.NOT_FOUND)
                .bodyJson().extractingPath("$.title").isEqualTo("Resource not found");
    }

    @Test
    void createCompany_withDuplicateName_returnsConflict() {
        when(companyService.createCompany(any(CompanyRequestDto.class)))
                .thenThrow(new ConflictException("Company with name 'Test' already exists."));

        assertThat(mvc.post().uri("/api/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": "Test"}
                        """))
                .hasStatus(HttpStatus.CONFLICT)
                .bodyJson().extractingPath("$.title").isEqualTo("Conflict");
    }

    @Test
    void getCompany_withUnexpectedError_returnsInternalServerError() {
        UUID companyId = UUID.randomUUID();
        when(companyService.getCompany(companyId)).thenThrow(new RuntimeException("Unexpected error"));

        assertThat(mvc.get().uri("/api/companies/" + companyId))
                .hasStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyJson().extractingPath("$.title").isEqualTo("Internal server error");
    }

    @Test
    void getCompanies_returnsOkAndPassesSearchToService() {
        CompanyCompactDto companyCompactDto = new CompanyCompactDto(UUID.randomUUID(), "Test", 0);
        when(companyService.getCompanies(any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(companyCompactDto), PageRequest.of(0, 20), 1));

        assertThat(mvc.get().uri("/api/companies?q=te"))
                .hasStatusOk()
                .bodyJson().extractingPath("$.content[0].name").isEqualTo("Test");

        verify(companyService).getCompanies(eq("te"), any(Pageable.class));
    }

    @Test
    void updateCompany_returnsOk() {
        UUID companyId = UUID.randomUUID();
        CompanyDetailDto companyDetailDto = new CompanyDetailDto(
                companyId, "Updated", null, null, null, null, 0
        );
        when(companyService.updateCompany(eq(companyId), any(CompanyRequestDto.class)))
                .thenReturn(companyDetailDto);

        assertThat(mvc.put().uri("/api/companies/" + companyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": "Updated"}
                        """))
                .hasStatusOk()
                .bodyJson().extractingPath("$.name").isEqualTo("Updated");
    }
}