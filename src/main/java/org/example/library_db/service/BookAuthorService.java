package org.example.library_db.service;

import org.example.library_db.model.Author;
import org.example.library_db.model.BookAuthor;
import org.example.library_db.model.BookDetails;
import org.example.library_db.repository.BookAuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookAuthorService {

    private final BookAuthorRepository repo;

    public BookAuthorService(BookAuthorRepository repo) {
        this.repo = repo;
    }

    public BookAuthor add(BookAuthor ba) {
        return repo.save(ba);
    }

    public List<BookAuthor> getAll() {
        return repo.findAll();
    }

    public BookAuthor getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public BookAuthor update(Long id, BookDetails book, Author author) {

        BookAuthor existing = repo.findById(id).orElse(null);
        if (existing == null) return null;

        existing.setBook(book);
        existing.setAuthor(author);

        return repo.save(existing);
    }

    public void remove(Long id) {
        repo.findById(id).ifPresent(repo::delete);
    }
}