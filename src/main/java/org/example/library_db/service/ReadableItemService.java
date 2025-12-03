package org.example.library_db.service;

import org.example.library_db.model.Loan;
import org.example.library_db.model.ReadableItem;
import org.example.library_db.model.Reservation;
import org.example.library_db.repository.LoanRepository;
import org.example.library_db.repository.ReadableItemRepository;
import org.example.library_db.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReadableItemService {

    private final ReadableItemRepository repository;
    private final LoanRepository loans;
    private final ReservationRepository reservations;

    public ReadableItemService(ReadableItemRepository repository, LoanRepository loans, ReservationRepository reservations) {
        this.repository = repository;
        this.loans = loans;
        this.reservations = reservations;
    }

    public ReadableItem addReadableItem(ReadableItem item) {
        validateItem(item, null);
        return repository.save(item);
    }

    public void updateReadableItem(Long id, ReadableItem update) {
        validateItem(update, id);
        update.setId(id);
        repository.save(update);
    }

    public void removeReadableItem(Long id) {
        repository.findById(id).ifPresent(repository::delete);
    }

    public ReadableItem getReadableItemById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<ReadableItem> getAllReadableItems() {
        return repository.findAll();
    }

    public List<Loan> getLoansByItemId(Long itemId) {
        return loans.findByItemsId(itemId);
    }

    public List<Reservation> getReservationsByItemId(Long itemId) {
        return reservations.findByReadableItemId(itemId);
    }

    private void validateItem(ReadableItem item, Long currentId) {
        if (item.getPublication() == null || item.getPublication().getId() == null) {
            throw new IllegalArgumentException("Readable item must be associated with a publication.");
        }
        if (item.getLibrary() == null || item.getLibrary().getId() == null) {
            throw new IllegalArgumentException("Readable item must belong to a library.");
        }
        if (item.getBarcode() == null || item.getBarcode().trim().isEmpty()) {
            throw new IllegalArgumentException("Barcode cannot be empty.");
        }

        boolean exists = (currentId == null)
                ? repository.existsByBarcode(item.getBarcode())
                : repository.existsByBarcodeAndIdNot(item.getBarcode(), currentId);

        if (exists) {
            throw new IllegalArgumentException("A readable item with this barcode already exists.");
        }
    }
}