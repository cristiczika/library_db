package org.example.library_db.controller;

import jakarta.validation.Valid;
import org.example.library_db.model.Author;
import org.example.library_db.service.AuthorService;
import org.example.library_db.service.BookAuthorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final BookAuthorService bookAuthorService;

    public AuthorController(AuthorService authorService, BookAuthorService bookAuthorService) {
        this.authorService = authorService;
        this.bookAuthorService = bookAuthorService;
    }


    @GetMapping
    public String index(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            Model model
    ) {
        model.addAttribute("authors", authorService.filter(name, sort, dir));
        model.addAttribute("name", name);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        return "authors/index";
    }

    @GetMapping("/{id}")
    public String getAuthorById(@PathVariable Long id, Model model) {
        Author author = authorService.getAuthorById(id);
        if (author == null) {
            return "redirect:/authors";
        }

        model.addAttribute("author", author);
        model.addAttribute("books", bookAuthorService.getBooksByAuthor(id));
        return "authors/details";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("author", new Author());
        return "authors/form";
    }

    @PostMapping
    public String addAuthor(@Valid @ModelAttribute("author") Author author, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "authors/form";
        }

        authorService.addAuthor(author);
        return "redirect:/authors";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Author author = authorService.getAuthorById(id);
        if (author == null) {
            return "redirect:/authors";
        }

        model.addAttribute("author", author);
        return "authors/form";
    }

    @PostMapping("/{id}/edit")
    public String updateAuthor(@PathVariable Long id, @Valid @ModelAttribute("author") Author author, BindingResult result) {
        if (result.hasErrors()) {
            return "authors/form";
        }

        authorService.updateAuthor(id, author);
        return "redirect:/authors";
    }

    @PostMapping("/{id}/delete")
    public String deleteAuthor(@PathVariable Long id) {
        authorService.removeAuthor(id);
        return "redirect:/authors";
    }
}