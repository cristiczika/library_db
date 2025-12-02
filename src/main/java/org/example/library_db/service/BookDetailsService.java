package org.example.library_db.service;

import org.example.library_db.model.BookDetails;
import org.example.library_db.repository.BookDetailsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookDetailsService {

    private final BookDetailsRepository repository;

    public BookDetailsService(BookDetailsRepository repository) {
        this.repository = repository;
    }

    public BookDetails addBookDetails(BookDetails book) {
        return repository.save(book);
    }

    public void updateBookDetails(Long id, BookDetails update) {
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

}