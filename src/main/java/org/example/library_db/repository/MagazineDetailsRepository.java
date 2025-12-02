package org.example.library_db.repository;

import org.example.library_db.model.MagazineDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MagazineDetailsRepository extends JpaRepository<MagazineDetails, Long> {}