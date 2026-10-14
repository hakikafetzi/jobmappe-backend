package dev.kafetzi.jobmappe.tracking.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    @Query(
            value = """
                    SELECT a FROM Application a
                    WHERE LOWER(a.jobPosting.title) LIKE LOWER(CONCAT('%', :q, '%'))
                    OR LOWER(a.jobPosting.company.name) LIKE LOWER(CONCAT('%', :q, '%'))
                    """,
            countQuery = """
                    SELECT COUNT(a) FROM Application a
                    WHERE LOWER(a.jobPosting.title) LIKE LOWER(CONCAT('%', :q, '%'))
                    OR LOWER(a.jobPosting.company.name) LIKE LOWER(CONCAT('%', :q, '%'))
                    """
    )
    @EntityGraph(attributePaths = {"jobPosting", "jobPosting.company"})
    Page<Application> findAllWithJobPostingAndCompany(@Param("q") String q, Pageable pageable);

    @Query("""
            SELECT a FROM Application a
            WHERE a.id = :id
            """)
    @EntityGraph(attributePaths = {"jobPosting", "jobPosting.company"})
    Optional<Application> findByIdWithJobPostingAndCompany(@Param("id") UUID id);

    boolean existsApplicationByJobPostingId(UUID jobPostingId);

    @Query("""
            SELECT a.jobPosting.id FROM Application a
            WHERE a.jobPosting.id IN :jobPostingIds
            """)
    List<UUID> findAllAppliedJobPostingIds(@Param("jobPostingIds") Collection<UUID> jobPostingIds);

    @Query("""
            SELECT a.id FROM Application a
            WHERE a.jobPosting.id = :jobPostingId
            """)
    Optional<UUID> findApplicationIdByJobPostingId(@Param("jobPostingId") UUID jobPostingId);

    @EntityGraph(attributePaths = {"jobPosting", "jobPosting.company"})
    Page<Application> findAllByStatus(ApplicationStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"jobPosting", "jobPosting.company"})
    Page<Application> findAllByPriority(ApplicationPriority priority, Pageable pageable);

    @EntityGraph(attributePaths = {"jobPosting", "jobPosting.company"})
    Page<Application> findAllByStatusAndPriority(
            ApplicationStatus status, ApplicationPriority priority, Pageable pageable
    );
}
