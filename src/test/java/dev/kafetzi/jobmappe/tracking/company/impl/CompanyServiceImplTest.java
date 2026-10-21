package dev.kafetzi.jobmappe.tracking.company.impl;

import dev.kafetzi.jobmappe.exception.ConflictException;
import dev.kafetzi.jobmappe.tracking.company.Company;
import dev.kafetzi.jobmappe.tracking.company.CompanyMapper;
import dev.kafetzi.jobmappe.tracking.company.CompanyRepository;
import dev.kafetzi.jobmappe.tracking.company.dto.CompanyRequestDto;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPostingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {
    @Mock CompanyRepository companyRepository;
    @Mock JobPostingRepository jobPostingRepository;
    @Mock CompanyMapper companyMapper;
    @InjectMocks CompanyServiceImpl companyService;

    @Test
    void createCompany_nameAlreadyExists_throwsConflict() {
        Company company = new Company();
        company.setName("Muster GmbH");
        CompanyRequestDto requestDto = new CompanyRequestDto(
                "Muster GmbH", null, null, null, null
        );
        when(companyMapper.toEntity(requestDto)).thenReturn(company);
        when(companyRepository.existsByNameIgnoreCase("Muster GmbH")).thenReturn(true);

        assertThatThrownBy(() -> companyService.createCompany(requestDto))
                .isInstanceOf(ConflictException.class);
        verify(companyRepository, never()).save(any());
    }

    @Test
    void updateCompany_nameUsedByOtherCompany_throwsConflict() {
        UUID companyId = UUID.randomUUID();
        CompanyRequestDto requestDto = new CompanyRequestDto(
                "Muster GmbH", null, null, null, null
        );
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(new Company()));
        when(companyRepository.existsByNameIgnoreCaseAndIdNot("Muster GmbH", companyId))
                .thenReturn(true);

        assertThatThrownBy(() -> companyService.updateCompany(companyId, requestDto))
                .isInstanceOf(ConflictException.class);
        verify(companyMapper, never()).updateEntityFromDto(any(), any());
    }
}