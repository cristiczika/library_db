package org.example.library_db.service;

import org.example.library_db.model.Library;
import org.example.library_db.repository.LibraryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryService {

    private final LibraryRepository repository;

    public LibraryService(LibraryRepository repository) {
        this.repository = repository;
    }

    public Library addLibrary(Library library) {
        return repository.save(library);
    }

    public void updateLibrary(Long id, Library update) {
        update.setId(id);
        repository.save(update);
    }

    public void removeLibrary(Long id) {
        repository.findById(id).ifPresent(repository::delete);
    }

    public Library getLibraryById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Library> getAllLibraries() {
        return repository.findAll();
    }

}