package dev.kafetzi.jobmappe.tracking.notecategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NoteCategoryRepository extends JpaRepository<NoteCategory, UUID> {
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndIdNot(String name, UUID id);
    List<NoteCategory> findAllByOrderByNameAsc();
}
