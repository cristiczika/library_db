package org.example.library_db.service;

import org.example.library_db.model.BookDetails;
import org.example.library_db.repository.BookDetailsRepository;
import org.example.library_db.repository.PublicationRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookDetailsService {

    private final BookDetailsRepository repository;
    private final PublicationRepository publications;

    public BookDetailsService(BookDetailsRepository repository, PublicationRepository publications) {
        this.repository = repository;
        this.publications = publications;
    }

    public BookDetails addBookDetails(BookDetails book) {
        validateTitle(book.getTitle(), null);
        return repository.save(book);
    }

    public void updateBookDetails(Long id, BookDetails update) {
        validateTitle(update.getTitle(), id);
        update.setId(id);
        repository.save(update);
    }

    public void removeBookDetails(Long id) {
        repository.findById(id).ifPresent(repository::delete);
    }

    public BookDetails getBookDetailsById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<BookDetails> getAllBookDetails() {
        return repository.findAll();
    }

    public List<BookDetails> filter(String title, String sort, String dir) {
        Sort s = dir.equals("desc")
                ? Sort.by(sort).descending()
                : Sort.by(sort).ascending();

        return (title == null || title.isBlank())
                ? repository.findAll(s)
                : repository.findByTitleContainingIgnoreCase(title, s);
    }

    private void validateTitle(String title, Long currentId) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be empty.");
        }

        boolean exists = (currentId == null)
                ? publications.existsByTitleIgnoreCase(title)
                : publications.existsByTitleIgnoreCaseAndIdNot(title, currentId);

        if (exists) {
            throw new IllegalArgumentException("A publication with this title already exists.");
        }
    }
}