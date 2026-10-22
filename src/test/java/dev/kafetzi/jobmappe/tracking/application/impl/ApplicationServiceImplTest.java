package dev.kafetzi.jobmappe.tracking.application.impl;

import dev.kafetzi.jobmappe.exception.ConflictException;
import dev.kafetzi.jobmappe.exception.InvalidRequestException;
import dev.kafetzi.jobmappe.tracking.application.*;
import dev.kafetzi.jobmappe.tracking.application.dto.ApplicationDetailDto;
import dev.kafetzi.jobmappe.tracking.company.Company;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPosting;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPostingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceImplTest {
    @Mock ApplicationRepository applicationRepository;
    @Mock JobPostingRepository jobPostingRepository;
    @Mock ApplicationMapper applicationMapper;
    @InjectMocks ApplicationServiceImpl applicationService;

    @ParameterizedTest
    @CsvSource(value = {
            "APPLIED,  NULL",
            "NULL,  HIGH",
            "APPLIED,  HIGH"
    }, nullValues = "NULL")
    void getAllApplications_searchWithStatusOrPriority_throwsInvalidRequest(
            ApplicationStatus applicationStatus,
            ApplicationPriority applicationPriority
    ) {
        String q = "Test";
        Pageable pageable = PageRequest.of(0, 20, Sort.by("createdAt"));

        assertThatThrownBy(
                () -> applicationService.getAllApplications(q, applicationStatus, applicationPriority, pageable)
        )
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("Search is only supported with no Filter.");
    }

    @Test
    void startApplication_applicationAlreadyExists_throwsConflict() {
        UUID jobPostingId = UUID.randomUUID();
        when(applicationRepository.existsApplicationByJobPostingId(jobPostingId)).thenReturn(true);

        assertThatThrownBy(() -> applicationService.startApplication(jobPostingId))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Application for job posting with id %s already exists.", jobPostingId);
        verify(applicationRepository, never()).save(any());
    }

    @Test
    void startApplication_jobPostingArchived_throwsConflict() {
        UUID jobPostingId = UUID.randomUUID();
        JobPosting jobPosting = new JobPosting(new Company());
        jobPosting.archive();
        when(jobPostingRepository.findByIdWithCompany(jobPostingId)).thenReturn(Optional.of(jobPosting));

        assertThatThrownBy(() -> applicationService.startApplication(jobPostingId))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Job posting with id %s is archived and cannot be applied to.", jobPostingId);
        verify(applicationRepository, never()).save(any());
    }

    @Test
    void startApplication_validJobPosting_savesApplicationInProgress() {
        UUID jobPostingId = UUID.randomUUID();
        JobPosting jobPosting = new JobPosting(new Company());
        ApplicationDetailDto detailDto = new ApplicationDetailDto(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        when(jobPostingRepository.findByIdWithCompany(jobPostingId)).thenReturn(Optional.of(jobPosting));
        when(applicationRepository.save(any(Application.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(applicationMapper.toDetailDto(any(Application.class))).thenReturn(detailDto);

        ApplicationDetailDto result = applicationService.startApplication(jobPostingId);

        assertThat(result).isSameAs(detailDto);
        ArgumentCaptor<Application> captor = ArgumentCaptor.forClass(Application.class);
        verify(applicationRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(ApplicationStatus.IN_PROGRESS);
        assertThat(captor.getValue().getJobPosting()).isSameAs(jobPosting);
    }
}