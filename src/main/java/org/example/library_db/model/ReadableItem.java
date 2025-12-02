package org.example.library_db.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publication_id", nullable = false)
    private Publication publication;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id", nullable = false)
    private Library library;

    @Column(nullable = false, unique = true, length = 100)
    private String barcode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReadableItemStatus status;

    @ManyToMany(mappedBy = "items")
    private List<Loan> loans = new ArrayList<>();

    public ReadableItem(Publication publication, Library library, String barcode, ReadableItemStatus status) {
        this.publication = publication;
        this.library = library;
        this.barcode = barcode;
        this.status = status;
    }
}