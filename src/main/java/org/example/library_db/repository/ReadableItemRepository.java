package org.example.library_db.repository;

import org.example.library_db.model.ReadableItem;
import org.example.library_db.model.ReadableItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReadableItemRepository extends JpaRepository<ReadableItem, Long> {
    boolean existsByIdAndStatus(Long id, ReadableItemStatus status);
    boolean existsByBarcode(String barcode);
    boolean existsByBarcodeAndIdNot(String barcode, Long id);
    Optional<ReadableItem> findById(Long id);
}