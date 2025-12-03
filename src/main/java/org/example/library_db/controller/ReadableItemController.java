package org.example.library_db.controller;

import jakarta.validation.Valid;
import org.example.library_db.model.Publication;
import org.example.library_db.model.ReadableItem;
import org.example.library_db.model.ReadableItemStatus;
import org.example.library_db.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/items")
public class ReadableItemController {

    private final ReadableItemService items;
    private final LibraryService libraries;
    private final BookDetailsService books;
    private final MagazineDetailsService magazines;

    public ReadableItemController(ReadableItemService items, LibraryService libraries, BookDetailsService books, MagazineDetailsService magazines) {
        this.items = items;
        this.libraries = libraries;
        this.books = books;
        this.magazines = magazines;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("items", items.getAllReadableItems());
        return "items/index";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        ReadableItem item = items.getReadableItemById(id);
        if (item == null)
            return "redirect:/items";

        model.addAttribute("item", item);
        model.addAttribute("loans", items.getLoansByItemId(id));
        model.addAttribute("reservations", items.getReservationsByItemId(id));

        return "items/details";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("item", new ReadableItem());
        model.addAttribute("libraries", libraries.getAllLibraries());
        model.addAttribute("publications", getAllPublications());
        model.addAttribute("statuses", ReadableItemStatus.values());
        return "items/form";
    }

    @PostMapping
    public String addItem(@Valid @ModelAttribute("item") ReadableItem item, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("libraries", libraries.getAllLibraries());
            model.addAttribute("publications", getAllPublications());
            model.addAttribute("statuses", ReadableItemStatus.values());
            return "items/form";
        }

        items.addReadableItem(item);
        return "redirect:/items";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        ReadableItem item = items.getReadableItemById(id);
        if (item == null)
            return "redirect:/items";

        model.addAttribute("item", item);
        model.addAttribute("libraries", libraries.getAllLibraries());
        model.addAttribute("publications", getAllPublications());
        model.addAttribute("statuses", ReadableItemStatus.values());

        return "items/form";
    }

    @PostMapping("/{id}/edit")
    public String updateItem(@PathVariable Long id, @Valid @ModelAttribute("item") ReadableItem item, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("libraries", libraries.getAllLibraries());
            model.addAttribute("publications", getAllPublications());
            model.addAttribute("statuses", ReadableItemStatus.values());
            return "items/form";
        }

        items.updateReadableItem(id, item);
        return "redirect:/items";
    }

    @PostMapping("/{id}/delete")
    public String deleteItem(@PathVariable Long id) {
        items.removeReadableItem(id);
        return "redirect:/items";
    }

    private List<Publication> getAllPublications() {
        List<Publication> all = new ArrayList<>();
        all.addAll(books.getAllBookDetails());
        all.addAll(magazines.getAllMagazineDetails());
        return all;
    }
}