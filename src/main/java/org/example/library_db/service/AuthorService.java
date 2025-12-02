package org.example.library_db.service;

import org.example.library_db.model.Author;
import org.example.library_db.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author addAuthor(Author author) {
        return authorRepository.save(author);
    }

    public void updateAuthor(Long id, Author update) {
        update.setId(id);
        authorRepository.save(update);
    }

    public void removeAuthor(Long id) {
        authorRepository.findById(id).ifPresent(authorRepository::delete);
    }

    public Author getAuthorById(Long id) {
        return authorRepository.findById(id).orElse(null);
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

}
