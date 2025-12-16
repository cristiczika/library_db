package org.example.library_db.controller;

import jakarta.validation.Valid;
import org.example.library_db.model.BookDetails;
import org.example.library_db.service.BookDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookDetailsController {

    private final BookDetailsService books;

    public BookDetailsController(BookDetailsService books) {
        this.books = books;
    }

    @GetMapping
    public String index(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            Model model
    ) {
        model.addAttribute("books", books.filter(title, sort, dir));
        model.addAttribute("title", title);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        return "books/index";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        BookDetails book = books.getBookDetailsById(id);
        if (book == null)
            return "redirect:/books";

        model.addAttribute("book", book);
        return "books/details";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("book", new BookDetails());
        return "books/form";
    }

    @PostMapping
    public String add(@Valid @ModelAttribute("book") BookDetails book, BindingResult result) {
        if (result.hasErrors()) {
            return "books/form";
        }

        books.addBookDetails(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        BookDetails book = books.getBookDetailsById(id);
        if (book == null)
            return "redirect:/books";

        model.addAttribute("book", book);
        return "books/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("book") BookDetails book, BindingResult result) {
        if (result.hasErrors()) {
            return "books/form";
        }

        books.updateBookDetails(id, book);
        return "redirect:/books";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        books.removeBookDetails(id);
        return "redirect:/books";
    }
}