package org.example.library_db.service;

import org.example.library_db.model.ReadableItem;
import org.example.library_db.repository.ReadableItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReadableItemService {

    private final ReadableItemRepository repository;

    public ReadableItemService(ReadableItemRepository repository) {
        this.repository = repository;
    }

    public ReadableItem addReadableItem(ReadableItem item) {
        return repository.save(item);
    }

    public void updateReadableItem(Long id, ReadableItem update) {
        update.setId(id);
        repository.save(update);
    }

    public void removeReadableItem(Long id) {
        repository.findById(id).ifPresent(repository::delete);
    }

    public ReadableItem getReadableItemById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<ReadableItem> getAllReadableItems() {
        return repository.findAll();
    }

}