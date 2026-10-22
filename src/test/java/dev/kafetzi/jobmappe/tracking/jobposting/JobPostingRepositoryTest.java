package dev.kafetzi.jobmappe.tracking.jobposting;

import dev.kafetzi.jobmappe.TestcontainersConfiguration;
import dev.kafetzi.jobmappe.tracking.application.Application;
import dev.kafetzi.jobmappe.tracking.company.Company;
import dev.kafetzi.jobmappe.tracking.jobposting.projection.CountByCompanyProjection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
class JobPostingRepositoryTest {
    private static final Pageable PAGEABLE = PageRequest.of(0, 20);

    @Autowired JobPostingRepository jobPostingRepository;
    @Autowired TestEntityManager entityManager;

    private Company persistCompany(String name) {
        Company company = new Company();
        company.setName(name);
        return entityManager.persist(company);
    }

    private JobPosting persistJobPosting(Company company, String title, JobPostingStatus status) {
        JobPosting jobPosting = new JobPosting(company);
        jobPosting.setTitle(title);
        jobPosting.setUrl("https://example.com/job");
        jobPosting.setStatus(status);
        return entityManager.persist(jobPosting);
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findAllWithCompany_searchMatchesTitleIgnoringCase() {
        Company company = persistCompany("Acme");
        persistJobPosting(company, "Java Backend Developer", JobPostingStatus.ACTIVE);
        persistJobPosting(company, "Frontend Developer", JobPostingStatus.ACTIVE);
        flushAndClear();

        Page<JobPosting> result = jobPostingRepository.findAllWithCompany("JAVA", PAGEABLE);

        assertThat(result.getContent())
                .extracting(JobPosting::getTitle)
                .containsExactly("Java Backend Developer");
    }

    @Test
    void findAllWithCompany_searchMatchesCompanyName() {
        Company globex = persistCompany("Globex");
        Company acme = persistCompany("Acme");
        persistJobPosting(globex, "Developer", JobPostingStatus.ACTIVE);
        persistJobPosting(acme, "Tester", JobPostingStatus.ACTIVE);
        flushAndClear();

        Page<JobPosting> result = jobPostingRepository.findAllWithCompany("glob", PAGEABLE);

        assertThat(result.getContent())
                .extracting(JobPosting::getTitle)
                .containsExactly("Developer");
    }

    @Test
    void findAllWithoutApplications_returnsOnlyActiveNotArchivedPostingsWithoutApplication() {
        Company company = persistCompany("Acme");
        persistJobPosting(company, "Open", JobPostingStatus.ACTIVE);
        persistJobPosting(company, "Inactive", JobPostingStatus.INACTIVE);
        JobPosting archived = persistJobPosting(company, "Archived", JobPostingStatus.ACTIVE);
        archived.archive();
        JobPosting applied = persistJobPosting(company, "Applied", JobPostingStatus.ACTIVE);
        entityManager.persist(new Application(applied));
        flushAndClear();

        Page<JobPosting> result = jobPostingRepository.findAllWithoutApplications(JobPostingStatus.ACTIVE, PAGEABLE);

        assertThat(result.getContent())
                .extracting(JobPosting::getTitle)
                .containsExactly("Open");
    }

    @Test
    void findAllWithApplications_returnsOnlyPostingsWithApplication() {
        Company company = persistCompany("Acme");
        persistJobPosting(company, "Without application", JobPostingStatus.ACTIVE);
        JobPosting applied = persistJobPosting(company, "With application", JobPostingStatus.ACTIVE);
        entityManager.persist(new Application(applied));
        flushAndClear();

        Page<JobPosting> result = jobPostingRepository.findAllWithApplications(PAGEABLE);

        assertThat(result.getContent())
                .extracting(JobPosting::getTitle)
                .containsExactly("With application");
    }

    @Test
    void countJobPostingsPerCompanyIds_returnsCountsAndOmitsCompaniesWithoutPostings() {
        Company companyA = persistCompany("Company A");
        Company companyB = persistCompany("Company B");
        Company companyC = persistCompany("Company C");
        persistJobPosting(companyA, "A1", JobPostingStatus.ACTIVE);
        persistJobPosting(companyA, "A2", JobPostingStatus.ACTIVE);
        persistJobPosting(companyB, "B1", JobPostingStatus.ACTIVE);
        flushAndClear();

        List<CountByCompanyProjection> result = jobPostingRepository.countJobPostingsPerCompanyIds(
                List.of(companyA.getId(), companyB.getId(), companyC.getId())
        );

        Map<UUID, Long> countsByCompanyId = result.stream()
                .collect(Collectors.toMap(CountByCompanyProjection::getCompanyId, CountByCompanyProjection::getCount));
        assertThat(countsByCompanyId).containsExactlyInAnyOrderEntriesOf(
                Map.of(companyA.getId(), 2L, companyB.getId(), 1L)
        );
    }
}