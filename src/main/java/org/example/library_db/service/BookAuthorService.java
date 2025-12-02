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
        validateLink(ba.getBook(), ba.getAuthor(), null);
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

        validateLink(book, author, id);

        existing.setBook(book);
        existing.setAuthor(author);

        return repo.save(existing);
    }

    public void remove(Long id) {
        repo.findById(id).ifPresent(repo::delete);
    }

    private void validateLink(BookDetails book, Author author, Long currentId) {
        if (book == null || book.getId() == null) {
            throw new IllegalArgumentException("Book must be selected.");
        }
        if (author == null || author.getId() == null) {
            throw new IllegalArgumentException("Author must be selected.");
        }

        boolean exists = (currentId == null)
                ? repo.existsByBookIdAndAuthorId(book.getId(), author.getId())
                : repo.existsByBookIdAndAuthorIdAndIdNot(book.getId(), author.getId(), currentId);

        if (exists) {
            throw new IllegalArgumentException("This book is already linked to this author.");
        }
    }
}