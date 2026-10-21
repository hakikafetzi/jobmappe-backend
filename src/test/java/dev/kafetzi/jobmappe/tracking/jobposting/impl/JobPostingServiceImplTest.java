package dev.kafetzi.jobmappe.tracking.jobposting.impl;

import dev.kafetzi.jobmappe.exception.ConflictException;
import dev.kafetzi.jobmappe.exception.InvalidRequestException;
import dev.kafetzi.jobmappe.tracking.application.ApplicationRepository;
import dev.kafetzi.jobmappe.tracking.company.Company;
import dev.kafetzi.jobmappe.tracking.company.CompanyRepository;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPosting;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPostingFilter;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPostingMapper;
import dev.kafetzi.jobmappe.tracking.jobposting.JobPostingRepository;
import dev.kafetzi.jobmappe.tracking.jobposting.dto.JobPostingArchiveDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
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
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobPostingServiceImplTest {
    @Mock JobPostingRepository jobPostingRepository;
    @Mock CompanyRepository companyRepository;
    @Mock ApplicationRepository applicationRepository;
    @Mock JobPostingMapper jobPostingMapper;
    @InjectMocks JobPostingServiceImpl jobPostingService;

    @ParameterizedTest
    @EnumSource(value = JobPostingFilter.class, names = {"ALL"}, mode = EnumSource.Mode.EXCLUDE)
    void getAllJobPostings_searchWithNonAllFilter_throwsInvalidRequest(JobPostingFilter jobPostingFilter) {
        String q = "Test";
        Pageable pageable = PageRequest.of(0, 20, Sort.by("title"));

        assertThatThrownBy(() -> jobPostingService.getAllJobPostings(q, jobPostingFilter, pageable))
                .isInstanceOf(InvalidRequestException.class);
        verifyNoInteractions(jobPostingRepository);
    }

    @Test
    void setArchived_archivePostingWithApplication_throwsConflict() {
        UUID jobPostingId = UUID.randomUUID();
        JobPostingArchiveDto archiveDto = new JobPostingArchiveDto(true);
        JobPosting jobPosting = new JobPosting(new Company());
        when(jobPostingRepository.findByIdWithCompany(jobPostingId)).thenReturn(Optional.of(jobPosting));
        when(applicationRepository.existsApplicationByJobPostingId(jobPostingId)).thenReturn(true);

        assertThatThrownBy(() -> jobPostingService.setArchived(jobPostingId, archiveDto))
                .isInstanceOf(ConflictException.class)
                .hasMessage(
                        "Cannot archive job posting with id %s because it has an associated application.",
                        jobPostingId
                );
        assertThat(jobPosting.getArchivedAt()).isNull();
    }
}