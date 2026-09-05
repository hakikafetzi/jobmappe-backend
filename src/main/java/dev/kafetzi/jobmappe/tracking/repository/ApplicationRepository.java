package dev.kafetzi.jobmappe.tracking.repository;

import dev.kafetzi.jobmappe.tracking.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    @Query("SELECT a FROM Application a JOIN FETCH a.jobPosting jp JOIN FETCH jp.company c")
    List<Application> findAllWithJobPostingAndCompany();

    boolean existsApplicationByJobPostingId(UUID jobPostingId);

    @Query("SELECT a.jobPosting.id FROM Application a")
    List<UUID> findAllAppliedJobPostingIds();
}
