package org.example.library_db.repository;

import org.example.library_db.model.Library;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryRepository extends JpaRepository<Library, Long> {
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
    void deleteByName(String name);
    List<Library> findByNameContainingIgnoreCase(String name, Sort sort);
    List<Library> findByAddressContainingIgnoreCase(String address, Sort sort);
}