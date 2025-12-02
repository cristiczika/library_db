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
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "publications")
@Getter
@Setter
@NoArgsConstructor
public abstract class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be empty")
    @Size(min = 2, max = 200)
    @Column(nullable = false)
    private String title;

    @Valid
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