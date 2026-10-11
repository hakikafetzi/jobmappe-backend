package dev.kafetzi.jobmappe.tracking.jobposting;

import dev.kafetzi.jobmappe.tracking.jobposting.projection.CountByCompanyProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            value = "SELECT j FROM JobPosting j LEFT JOIN FETCH j.company",
            countQuery = "SELECT COUNT(j) FROM JobPosting j"
    )
    Page<JobPosting> findAllWithCompany(Pageable pageable);

    @Query("SELECT j FROM JobPosting j LEFT JOIN FETCH j.company WHERE j.id = :id")
    Optional<JobPosting> findByIdWithCompany(@Param("id") UUID id);

    @Query(
            value = "SELECT j FROM JobPosting j LEFT JOIN FETCH j.company WHERE j.company.id = :companyId",
            countQuery = "SELECT COUNT(j) FROM JobPosting j WHERE j.company.id = :companyId"
    )
    Page<JobPosting> findAllByCompanyId(@Param("companyId") UUID companyId, Pageable pageable);

    @Query("SELECT j.company.id AS companyId, COUNT(j) AS count FROM JobPosting j WHERE j.company.id IN :companyIds GROUP BY j.company.id")
    List<CountByCompanyProjection> countJobPostingsPerCompanyIds(@Param("companyIds") Collection<UUID> companyIds);

    long countJobPostingsByCompanyId(UUID companyId);
}
