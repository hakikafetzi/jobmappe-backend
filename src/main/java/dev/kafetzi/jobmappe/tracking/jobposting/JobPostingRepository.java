package dev.kafetzi.jobmappe.tracking.jobposting;

import dev.kafetzi.jobmappe.tracking.jobposting.projection.CountByCompanyProjection;
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
public interface JobPostingRepository extends JpaRepository<JobPosting, UUID> {

    @Query(
            value = """
                    SELECT j FROM JobPosting j
                    WHERE LOWER(j.title) LIKE LOWER(CONCAT('%', :q, '%'))
                    OR LOWER(j.company.name) LIKE LOWER(CONCAT('%', :q, '%'))
                    """,
            countQuery = """
                    SELECT COUNT(j) FROM JobPosting j
                    WHERE LOWER(j.title) LIKE LOWER(CONCAT('%', :q, '%'))
                    OR LOWER(j.company.name) LIKE LOWER(CONCAT('%', :q, '%'))
                    """
    )
    @EntityGraph(attributePaths = "company")
    Page<JobPosting> findAllWithCompany(@Param("q") String q, Pageable pageable);

    @Query("""
            SELECT j FROM JobPosting j
            WHERE j.id = :id
            """)
    @EntityGraph(attributePaths = "company")
    Optional<JobPosting> findByIdWithCompany(@Param("id") UUID id);

    @Query(
            value = """
                    SELECT j FROM JobPosting j
                    WHERE j.company.id = :companyId
                    """,
            countQuery = """
                    SELECT COUNT(j) FROM JobPosting j
                    WHERE j.company.id = :companyId
                    """
    )
    @EntityGraph(attributePaths = "company")
    Page<JobPosting> findAllByCompanyId(@Param("companyId") UUID companyId, Pageable pageable);

    @Query("""
            SELECT j.company.id AS companyId, COUNT(j) AS count FROM JobPosting j 
            WHERE j.company.id IN :companyIds 
            GROUP BY j.company.id
            """)
    List<CountByCompanyProjection> countJobPostingsPerCompanyIds(@Param("companyIds") Collection<UUID> companyIds);

    long countJobPostingsByCompanyId(UUID companyId);

    @EntityGraph(attributePaths = "company")
    Page<JobPosting> findAllByStatus(JobPostingStatus status, Pageable pageable);

    @EntityGraph(attributePaths = "company")
    Page<JobPosting> findAllByArchivedAtIsNotNull(Pageable pageable);

    @Query(
            value = """
                    SELECT j FROM JobPosting j
                    WHERE j.status = :status
                    AND j.archivedAt IS NULL
                    AND NOT EXISTS (
                            SELECT a.id FROM Application a WHERE a.jobPosting = j
                    )
                    """,
            countQuery = """
                    SELECT COUNT(j) FROM JobPosting j
                    WHERE j.status = :status
                    AND j.archivedAt IS NULL
                    AND NOT EXISTS (
                        SELECT a.id FROM Application a WHERE a.jobPosting = j
                    )
                    """
    )
    @EntityGraph(attributePaths = "company")
    Page<JobPosting> findAllWithoutApplications(
            @Param("status") JobPostingStatus status,
            Pageable pageable
    );

    @Query(
            value = """
                    SELECT j FROM JobPosting j
                    WHERE EXISTS (
                            SELECT a.id FROM Application a WHERE a.jobPosting = j
                    )
                    """,
            countQuery = """
                    SELECT COUNT(j) FROM JobPosting j 
                    WHERE EXISTS (
                            SELECT a.id FROM Application a WHERE a.jobPosting = j
                    )
                    """
    )
    @EntityGraph(attributePaths = "company")
    Page<JobPosting> findAllWithApplications(Pageable pageable);
}
