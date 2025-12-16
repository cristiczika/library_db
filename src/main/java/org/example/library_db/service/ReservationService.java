package org.example.library_db.service;

import org.example.library_db.model.*;
import org.example.library_db.repository.LoanRepository;
import org.example.library_db.repository.ReadableItemRepository;
import org.example.library_db.repository.ReservationRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository repository;
    private final LoanRepository loans;
    private final ReadableItemRepository items;

    public ReservationService(ReservationRepository repository, LoanRepository loans, ReadableItemRepository items) {
        this.repository = repository;
        this.loans = loans;
        this.items = items;
    }

    public Reservation addReservation(Reservation reservation) {
        validateReservation(reservation, null);
        ReadableItem item = reservation.getReadableItem();
        item.setStatus(ReadableItemStatus.RESERVED);
        return repository.save(reservation);
    }

    public void updateReservation(Long id, Reservation update) {
        validateReservation(update, id);
        update.setId(id);
        repository.save(update);
    }

    public void removeReservation(Long id) {
        repository.findById(id).ifPresent(res -> {
            ReadableItem item = res.getReadableItem();
            if (item != null && item.getStatus() == ReadableItemStatus.RESERVED) {
                item.setStatus(ReadableItemStatus.AVAILABLE);
            }
            repository.delete(res);
        });
    }

    public Reservation getReservationById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Reservation> getAllReservations() {
        return repository.findAll();
    }

    private void validateReservation(Reservation r, Long currentId) {
        if (r.getMember() == null || r.getMember().getId() == null) {
            throw new IllegalArgumentException("Reservation must have a member.");
        }
        if (r.getReadableItem() == null || r.getReadableItem().getId() == null) {
            throw new IllegalArgumentException("Reservation must have a readable item.");
        }

        if (r.getDate() == null) {
            r.setDate(LocalDate.now());
        }

        if (r.getDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Reservation date cannot be in the future.");
        }

        if (r.getDate().isBefore(LocalDate.now().minusDays(30))) {
            throw new IllegalArgumentException("Reservation date cannot be older than 30 days.");
        }

        boolean borrowed = loans.existsByMemberIdAndItemsId(
                r.getMember().getId(),
                r.getReadableItem().getId()
        );
        if (borrowed) {
            throw new IllegalArgumentException("You already have this item borrowed.");
        }

        ReadableItem item = items.findById(r.getReadableItem().getId()).orElse(null);
        if (item == null) {
            throw new IllegalArgumentException("Readable item does not exist.");
        }

        if (item.getStatus() != ReadableItemStatus.AVAILABLE &&
                (currentId == null || !isSameItemForReservation(currentId, item.getId()))) {
            throw new IllegalArgumentException("Item is not available for reservation.");
        }

        long activeReservations = repository.countByMemberIdAndStatus(
                r.getMember().getId(),
                ReservationStatus.ACTIVE
        );
        if (activeReservations >= 5 && (currentId == null)) {
            throw new IllegalArgumentException("You cannot have more than 5 active reservations.");
        }
    }

    public List<Reservation> filter(
            String member,
            String barcode,
            ReservationStatus status,
            LocalDate date,
            String sort,
            String dir
    ) {
        Sort s = dir.equalsIgnoreCase("desc")
                ? Sort.by(sort).descending()
                : Sort.by(sort).ascending();

        boolean hasAny =
                (member != null && !member.isBlank()) ||
                        (barcode != null && !barcode.isBlank()) ||
                        status != null ||
                        date != null;

        if (hasAny) {
            return repository.filterAll(
                    member,
                    barcode,
                    status,
                    date,
                    s
            );
        }

        return repository.findAll(s);
    }

    private boolean isSameItemForReservation(Long reservationId, Long itemId) {
        return repository.findById(reservationId)
                .map(res -> res.getReadableItem() != null &&
                        itemId.equals(res.getReadableItem().getId()))
                .orElse(false);
    }
}