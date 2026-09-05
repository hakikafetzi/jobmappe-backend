package dev.kafetzi.jobmappe.tracking.repository;

import dev.kafetzi.jobmappe.tracking.model.NoteCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NoteCategoryRepository extends JpaRepository<NoteCategory, UUID> {

    boolean existsByNameIgnoreCase(String name);
}
