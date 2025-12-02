package org.example.library_db.service;

import org.example.library_db.model.MagazineDetails;
import org.example.library_db.repository.MagazineDetailsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MagazineDetailsService {

    private final MagazineDetailsRepository repository;

    public MagazineDetailsService(MagazineDetailsRepository repository) {
        this.repository = repository;
    }

    public MagazineDetails addMagazineDetails(MagazineDetails magazine) {
        return repository.save(magazine);
    }

    public void updateMagazineDetails(Long id, MagazineDetails update) {
        update.setId(id);
        repository.save(update);
    }

    public void removeMagazineDetails(Long id) {
        repository.findById(id).ifPresent(repository::delete);
    }

    public MagazineDetails getMagazineDetailsById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<MagazineDetails> getAllMagazineDetails() {
        return repository.findAll();
    }

}