package org.example.library_db.controller;

import org.example.library_db.model.Author;
import org.example.library_db.model.BookAuthor;
import org.example.library_db.model.BookDetails;
import org.example.library_db.service.AuthorService;
import org.example.library_db.service.BookAuthorService;
import org.example.library_db.service.BookDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bookauthors")
public class BookAuthorController {

    private final BookAuthorService service;
    private final BookDetailsService books;
    private final AuthorService authors;

    public BookAuthorController(BookAuthorService service, BookDetailsService books, AuthorService authors) {
        this.service = service;
        this.books = books;
        this.authors = authors;
    }

    @GetMapping
    public String index(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String book,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            Model model
    ) {
        model.addAttribute("bookauthors",
                service.filter(author, book, sort, dir));

        model.addAttribute("author", author);
        model.addAttribute("book", book);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);

        return "bookauthors/index";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        BookAuthor ba = service.getById(id);
        if (ba == null) return "redirect:/bookauthors";

        model.addAttribute("bookauthor", ba);
        return "bookauthors/details";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("bookauthor", new BookAuthor());
        model.addAttribute("books", books.getAllBookDetails());
        model.addAttribute("authors", authors.getAllAuthors());
        return "bookauthors/form";
    }

    @PostMapping
    public String create(@RequestParam Long bookId, @RequestParam Long authorId, Model model) {
        BookDetails book = books.getBookDetailsById(bookId);
        Author author = authors.getAuthorById(authorId);

        if (book == null || author == null) {
            model.addAttribute("books", books.getAllBookDetails());
            model.addAttribute("authors", authors.getAllAuthors());
            model.addAttribute("error", "Invalid book or author selection");
            return "bookauthors/form";
        }

        BookAuthor ba = new BookAuthor();
        ba.setBook(book);
        ba.setAuthor(author);

        service.add(ba);
        return "redirect:/bookauthors";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        BookAuthor ba = service.getById(id);
        if (ba == null) return "redirect:/bookauthors";

        model.addAttribute("bookauthor", ba);
        model.addAttribute("books", books.getAllBookDetails());
        model.addAttribute("authors", authors.getAllAuthors());
        return "bookauthors/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @RequestParam Long bookId, @RequestParam Long authorId, Model model) {
        BookDetails book = books.getBookDetailsById(bookId);
        Author author = authors.getAuthorById(authorId);

        if (book == null || author == null) {
            model.addAttribute("books", books.getAllBookDetails());
            model.addAttribute("authors", authors.getAllAuthors());
            model.addAttribute("error", "Invalid selection");
            return "bookauthors/form";
        }

        service.update(id, book, author);
        return "redirect:/bookauthors";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.remove(id);
        return "redirect:/bookauthors";
    }
}