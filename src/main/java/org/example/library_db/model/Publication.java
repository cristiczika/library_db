package org.example.library_db.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "publications")
@Getter
@Setter
@NoArgsConstructor
public abstract class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "publication", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReadableItem> copies = new ArrayList<>();

    public Publication(String title) {
        this.title = title;
    }

    public void addCopy(ReadableItem copy) {
        copies.add(copy);
        copy.setPublication(this);
    }
}