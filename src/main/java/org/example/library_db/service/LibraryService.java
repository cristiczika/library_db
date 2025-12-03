package org.example.library_db.service;

import org.example.library_db.model.Library;
import org.example.library_db.model.Member;
import org.example.library_db.model.ReadableItem;
import org.example.library_db.repository.LibraryRepository;
import org.example.library_db.repository.MemberRepository;
import org.example.library_db.repository.ReadableItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryService {

    private final LibraryRepository repository;
    private final MemberRepository memberRepository;
    private final ReadableItemRepository readableItemRepository;

    public LibraryService(LibraryRepository repository, MemberRepository memberRepository, ReadableItemRepository readableItemRepository) {
        this.repository = repository;
        this.memberRepository = memberRepository;
        this.readableItemRepository = readableItemRepository;
    }

    public Library addLibrary(Library library) {
        validateLibrary(library, null);
        return repository.save(library);
    }

    public void updateLibrary(Long id, Library update) {
        validateLibrary(update, id);
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

    public List<Member> getMembersByLibraryId(Long id) {
        return memberRepository.findByLibraryId(id);
    }

    public List<ReadableItem> getItemsByLibraryId(Long id) {
        return readableItemRepository.findByLibraryId(id);
    }

    private void validateLibrary(Library library, Long currentId) {
        if (library.getName() == null || library.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Library name cannot be empty.");
        }
        if (library.getAddress() == null || library.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Library address cannot be empty.");
        }

        boolean exists = (currentId == null)
                ? repository.existsByNameIgnoreCase(library.getName())
                : repository.existsByNameIgnoreCaseAndIdNot(library.getName(), currentId);

        if (exists) {
            throw new IllegalArgumentException("A library with this name already exists.");
        }
    }
}