package org.example.library_db.controller;

import jakarta.validation.Valid;
import org.example.library_db.model.Library;
import org.example.library_db.service.LibraryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/libraries")
public class LibraryController {

    private final LibraryService service;

    public LibraryController(LibraryService service) {
        this.service = service;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("libraries", service.getAllLibraries());
        return "libraries/index";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        Library library = service.getLibraryById(id);
        if (library == null) return "redirect:/libraries";

        model.addAttribute("library", library);
        Library lib = service.getLibraryById(id);
        model.addAttribute("members", service.getMembersByLibraryId(id));
        model.addAttribute("items", service.getItemsByLibraryId(id));
        return "libraries/details";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("library", new Library());
        return "libraries/form";
    }

    @PostMapping
    public String add(@Valid @ModelAttribute("library") Library library, BindingResult result) {
        if (result.hasErrors()) {
            return "libraries/form";
        }

        service.addLibrary(library);
        return "redirect:/libraries";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Library library = service.getLibraryById(id);
        if (library == null) return "redirect:/libraries";

        model.addAttribute("library", library);
        return "libraries/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("library") Library library, BindingResult result) {
        if (result.hasErrors()) {
            return "libraries/form";
        }

        service.updateLibrary(id, library);
        return "redirect:/libraries";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.removeLibrary(id);
        return "redirect:/libraries";
    }
}