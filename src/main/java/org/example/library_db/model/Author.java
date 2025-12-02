package org.example.library_db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.*;

@Entity
@Table(name = "authors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 3, max = 200, message = "Name must be between 3 and 200 characters")
    @Column(nullable = false, length = 200)
    private String name;

    @Past(message = "Date of birth must be in the past")
    @NotNull(message = "Date of birth is required")
    @Column(columnDefinition = "DATE")
    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookAuthor> bookAuthors = new ArrayList<>();

    public void addBook(BookDetails book) {
        BookAuthor ba = new BookAuthor();
        ba.setAuthor(this);
        ba.setBook(book);

        this.bookAuthors.add(ba);
        book.getBookAuthors().add(ba);
    }
}