package org.example.library_db.service;

import org.example.library_db.model.MagazineDetails;
import org.example.library_db.repository.MagazineDetailsRepository;
import org.example.library_db.repository.PublicationRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MagazineDetailsService {

    private final MagazineDetailsRepository repository;
    private final PublicationRepository publications;

    public MagazineDetailsService(MagazineDetailsRepository repository,
                                  PublicationRepository publications) {
        this.repository = repository;
        this.publications = publications;
    }

    public MagazineDetails addMagazineDetails(MagazineDetails magazine) {
        validateMagazine(magazine, null);
        return repository.save(magazine);
    }

    public void updateMagazineDetails(Long id, MagazineDetails update) {
        validateMagazine(update, id);
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

    private void validateMagazine(MagazineDetails magazine, Long currentId) {
        if (magazine.getTitle() == null || magazine.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Magazine title cannot be empty.");
        }
        if (magazine.getPublisher() == null || magazine.getPublisher().trim().isEmpty()) {
            throw new IllegalArgumentException("Publisher cannot be empty.");
        }

        boolean exists = (currentId == null)
                ? publications.existsByTitleIgnoreCase(magazine.getTitle())
                : publications.existsByTitleIgnoreCaseAndIdNot(magazine.getTitle(), currentId);

        if (exists) {
            throw new IllegalArgumentException("A publication with this title already exists.");
        }
    }

    public List<MagazineDetails> filter(String title, String publisher, String sort, String dir) {

        Sort s = dir.equalsIgnoreCase("desc")
                ? Sort.by(sort).descending()
                : Sort.by(sort).ascending();

        boolean hasTitle = title != null && !title.isBlank();
        boolean hasPublisher = publisher != null && !publisher.isBlank();

        if (hasTitle && hasPublisher) {
            return repository.findByTitleContainingIgnoreCaseAndPublisherContainingIgnoreCase(
                    title, publisher, s
            );
        }

        if (hasTitle) {
            return repository.findByTitleContainingIgnoreCase(title, s);
        }

        if (hasPublisher) {
            return repository.findByPublisherContainingIgnoreCase(publisher, s);
        }

        return repository.findAll(s);
    }
}