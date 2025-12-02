package org.example.library_db.repository;

import org.example.library_db.model.ReadableItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadableItemRepository extends JpaRepository<ReadableItem, Long> {}