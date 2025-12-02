package org.example.library_db.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "readable_items")
@Getter
@Setter
@NoArgsConstructor
public class ReadableItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Publication is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publication_id", nullable = false)
    private Publication publication;

    @NotNull(message = "Library is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id", nullable = false)
    private Library library;

    @NotBlank(message = "Barcode cannot be empty")
    @Size(min = 3, max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String barcode;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReadableItemStatus status;

    @Valid
    @ManyToMany(mappedBy = "items")
    private List<Loan> loans = new ArrayList<>();

    public ReadableItem(Publication publication, Library library, String barcode, ReadableItemStatus status) {
        this.publication = publication;
        this.library = library;
        this.barcode = barcode;
        this.status = status;
    }
}