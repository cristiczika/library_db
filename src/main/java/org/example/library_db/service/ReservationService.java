package org.example.library_db.service;

import org.example.library_db.model.Reservation;
import org.example.library_db.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository repository;

    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
    }

    public Reservation addReservation(Reservation reservation) {
        return repository.save(reservation);
    }

    public void updateReservation(Long id, Reservation update) {
        update.setId(id);
        repository.save(update);
    }

    public void removeReservation(Long id) {
        repository.findById(id).ifPresent(repository::delete);
    }

    public Reservation getReservationById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Reservation> getAllReservations() {
        return repository.findAll();
    }

}