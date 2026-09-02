package dev.kafetzi.jobmappe.application.repository;

import dev.kafetzi.jobmappe.application.model.NoteCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NoteCategoryRepository extends JpaRepository<NoteCategory, UUID> {

    boolean existsByNameIgnoreCase(String name);
}
