package org.example.library_db.repository;

import java.time.LocalDate;
import java.util.List;

import org.example.library_db.model.Reservation;
import org.example.library_db.model.ReservationStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    long countByMemberIdAndStatus(Long memberId, ReservationStatus status);
    List<Reservation> findByMemberId(Long memberId);
    List<Reservation> findByReadableItemId(Long itemId);

    @Query("""
        SELECT r
        FROM Reservation r
        JOIN r.member m
        WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))
    """)
    List<Reservation> findByMemberName(
            @Param("name") String name,
            Sort sort
    );

    @Query("""
        SELECT r
        FROM Reservation r
        JOIN r.readableItem i
        WHERE LOWER(i.barcode) LIKE LOWER(CONCAT('%', :barcode, '%'))
    """)
    List<Reservation> findByItemBarcode(
            @Param("barcode") String barcode,
            Sort sort
    );

    List<Reservation> findByStatus(ReservationStatus status, Sort sort);

    List<Reservation> findByDate(LocalDate date, Sort sort);

    @Query("""
        SELECT r
        FROM Reservation r
        JOIN r.member m
        JOIN r.readableItem i
        WHERE (:name IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (:barcode IS NULL OR LOWER(i.barcode) LIKE LOWER(CONCAT('%', :barcode, '%')))
          AND (:status IS NULL OR r.status = :status)
          AND (:date IS NULL OR r.date = :date)
    """)
    List<Reservation> filterAll(
            @Param("name") String name,
            @Param("barcode") String barcode,
            @Param("status") ReservationStatus status,
            @Param("date") LocalDate date,
            Sort sort
    );
}