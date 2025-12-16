package org.example.library_db.service;

import org.example.library_db.model.Author;
import org.example.library_db.repository.AuthorRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author addAuthor(Author author) {
        validateAuthor(author, null);
        return authorRepository.save(author);
    }

    public void updateAuthor(Long id, Author update) {
        validateAuthor(update, id);
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

    private void validateAuthor(Author author, Long currentId) {
        if (author.getName() == null || author.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be empty.");
        }

        if (author.getDateOfBirth() != null &&
                author.getDateOfBirth().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Author date of birth cannot be in the future.");
        }

        boolean exists = (currentId == null)
                ? authorRepository.existsByNameIgnoreCase(author.getName())
                : authorRepository.existsByNameIgnoreCaseAndIdNot(author.getName(), currentId);

        if (exists) {
            throw new IllegalArgumentException("An author with this name already exists.");
        }
    }

    public List<Author> filter(String name, String sort, String dir) {
        Sort s = dir.equals("desc") ? Sort.by(sort).descending() : Sort.by(sort).ascending();
        return (name == null || name.isBlank())
                ? authorRepository.findAll(s)
                : authorRepository.findByNameContainingIgnoreCase(name, s);
    }
}