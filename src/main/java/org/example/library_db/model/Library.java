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
@Table(name = "libraries")
@Getter
@Setter
@NoArgsConstructor
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Library name cannot be empty")
    @Size(min = 3, max = 200)
    @Column(nullable = false, length = 200)
    private String name;

    @NotBlank(message = "Address is required")
    @Size(min = 5, max = 300)
    @Column(nullable = false, length = 300)
    private String address;

    @Valid
    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member> members = new ArrayList<>();

    @Valid
    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReadableItem> readableItems = new ArrayList<>();

    public Library(String name, String address) {
        this.name = name;
        this.address = address;
    }
}