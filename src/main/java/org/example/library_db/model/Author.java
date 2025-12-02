package org.example.library_db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false, length = 200)
    private String name;

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