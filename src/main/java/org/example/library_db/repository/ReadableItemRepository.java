package org.example.library_db.repository;

import org.example.library_db.model.ReadableItem;
import org.example.library_db.model.ReadableItemStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReadableItemRepository extends JpaRepository<ReadableItem, Long> {
    boolean existsByIdAndStatus(Long id, ReadableItemStatus status);
    boolean existsByBarcode(String barcode);
    boolean existsByBarcodeAndIdNot(String barcode, Long id);
    Optional<ReadableItem> findById(Long id);
    List<ReadableItem> findByLibraryId(Long libraryId);

    @Query("""
        SELECT ri
        FROM ReadableItem ri
        WHERE (:barcode IS NULL OR LOWER(ri.barcode) LIKE LOWER(CONCAT('%', :barcode, '%')))
          AND (:publication IS NULL OR LOWER(ri.publication.title) LIKE LOWER(CONCAT('%', :publication, '%')))
          AND (:library IS NULL OR LOWER(ri.library.name) LIKE LOWER(CONCAT('%', :library, '%')))
    """)
    List<ReadableItem> filter(
            @Param("barcode") String barcode,
            @Param("publication") String publication,
            @Param("library") String library,
            Sort sort
    );
}

