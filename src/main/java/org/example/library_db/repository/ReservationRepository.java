package org.example.library_db.repository;

import java.util.List;

import org.example.library_db.model.Reservation;
import org.example.library_db.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    long countByMemberIdAndStatus(Long memberId, ReservationStatus status);
    List<Reservation> findByMemberId(Long memberId);
    List<Reservation> findByReadableItemId(Long itemId);
}