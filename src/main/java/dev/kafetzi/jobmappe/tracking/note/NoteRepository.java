package dev.kafetzi.jobmappe.tracking.note;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<Note, UUID> {
    @Query(
            value = """
                    SELECT n FROM Note n
                    WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :q, '%'))
                    OR LOWER(n.content) LIKE LOWER(CONCAT('%', :q, '%'))
                    """,
            countQuery = """
                    SELECT COUNT(n) FROM Note n
                    WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :q, '%'))
                    OR LOWER(n.content) LIKE LOWER(CONCAT('%', :q, '%'))
                    """
    )
    @EntityGraph(attributePaths = {
            "noteCategory",
            "application",
            "application.jobPosting",
            "application.jobPosting.company"
    })
    Page<Note> findAllWithDetails(@Param("q") String q, Pageable pageable);

    @EntityGraph(attributePaths = {
            "noteCategory",
            "application",
            "application.jobPosting",
            "application.jobPosting.company"
    })
    Page<Note> findAllByNoteCategoryId(UUID noteCategoryId, Pageable pageable);

    @EntityGraph(attributePaths = {
            "noteCategory",
            "application",
            "application.jobPosting",
            "application.jobPosting.company"
    })
    Page<Note> findAllByApplicationId(UUID applicationId, Pageable pageable);

    @EntityGraph(attributePaths = {
            "noteCategory",
            "application",
            "application.jobPosting",
            "application.jobPosting.company"
    })
    Page<Note> findAllByApplicationIdAndNoteCategoryId(UUID applicationId, UUID noteCategoryId, Pageable pageable);

    @Query("""
            SELECT n FROM Note n
            WHERE n.id = :noteId
            """)
    @EntityGraph(attributePaths = {
            "noteCategory",
            "application",
            "application.jobPosting",
            "application.jobPosting.company"
    })
    Optional<Note> findByIdWithDetails(@Param("noteId") UUID noteId);
}
