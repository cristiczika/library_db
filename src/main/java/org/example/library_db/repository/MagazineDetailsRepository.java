package org.example.library_db.repository;

import org.example.library_db.model.MagazineDetails;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MagazineDetailsRepository extends JpaRepository<MagazineDetails, Long> {
    List<MagazineDetails> findByTitleContainingIgnoreCase(String title, Sort sort);
    List<MagazineDetails> findByPublisherContainingIgnoreCase(String publisher, Sort sort);
    List<MagazineDetails> findByTitleContainingIgnoreCaseAndPublisherContainingIgnoreCase(
            String title,
            String publisher,
            Sort sort
    );
}