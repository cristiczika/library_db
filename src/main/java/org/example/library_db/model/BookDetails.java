package org.example.library_db.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book_details")
@Getter
@Setter
@NoArgsConstructor
public class BookDetails extends Publication {

    @Valid
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookAuthor> bookAuthors = new ArrayList<>();

    public BookDetails(String title) {
        super(title);
    }

    public void addBookAuthor(BookAuthor ba) {
        bookAuthors.add(ba);
        ba.setBook(this);
    }
}